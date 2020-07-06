package com.siyueren.demo.redisdistributedlocker.redis;

import com.siyueren.demo.redisdistributedlocker.exception.BusinessException;
import com.siyueren.demo.redisdistributedlocker.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RedisDistributedLocker {
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String UNLOCK_LUA;
    
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }
    
    public String lock(String key, int expireSeconds) {
        try {
            String result = redisTemplate.execute((RedisCallback<String>) (connection) -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                String uuid = SystemUtil.generateUUID();
                String resultIn;
                if (expireSeconds == -1) {
                    resultIn = commands.set(key, uuid, "NX");
                } else {
                    resultIn = commands.set(key, uuid, "NX", "EX", expireSeconds);
                }
                return StringUtils.isNotBlank(resultIn) ? uuid : null;
            });
            if (StringUtils.isNotBlank(result)) {
                log.debug("lock() lock success key:[{}], lockId:[{}], expireAfter:[{}]", key, result, expireSeconds);
                return result;
            }
        } catch (Exception e) {
            log.error("lock() exception occurred", e);
        }
        return null;
    }
    
    /** 重试锁 */
    public String retryLock(String key, int expireSeconds, int trySeconds) {
        try {
            long finalRetryTime = System.currentTimeMillis() + trySeconds * 1000;
            do {
                String result = lock(key, expireSeconds);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
                log.info("retryLock() key:[{}] failure, will retry after 100ms", key);
                Thread.sleep(100);
            } while (System.currentTimeMillis() < finalRetryTime);
        } catch (Exception e) {
            log.error("retryLock() exception occurred", e);
        }
        return null;
    }
    
    /** 释放锁 */
    public boolean unlock(String key, String lockId) {
        try {
            // 使用Lua脚本：先判断是否是自己设置的锁，再执行删除-原子操作
            // 如果用java方法判断，则判断和删除属于两个原子操作，如果中间锁自动失效，其他线程加锁，则会导致误删其他线程锁
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(lockId);
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                if (nativeConnection instanceof JedisCluster) { // 集群模式
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                } else if (nativeConnection instanceof Jedis) { // 单机模式
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                return 0L;
            });
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("unlock() exception occurred", e);
            return false;
        }
    }
    
    public boolean executeIfLocked(String key, int expireSeconds, CallBack callBack) {
        return executeIfLocked(key, expireSeconds, null, callBack);
    }
    
    public boolean executeIfLocked(String key, int expireSeconds, Integer trySeconds, CallBack callBack) {
        final String lockId;
        if (trySeconds != null && trySeconds > 0) {
            lockId = retryLock(key, expireSeconds, trySeconds);
        } else {
            lockId = lock(key, expireSeconds);
        }
        if (StringUtils.isNotBlank(lockId)) {
            try {
                callBack.execute();
            } catch (Exception e) {
                log.error("executeIfLocked() error", e);
                if (e instanceof BusinessException) {
                    throw (BusinessException) e;
                }
                throw new BusinessException(e);
            } finally {
                unlock(key, lockId);
            }
            return true;
        }
        return false;
    }
    
    public interface CallBack {
        void execute() throws Exception;
    }
}

