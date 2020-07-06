package com.siyueren.demo.redisdistributedlocker.task;

import com.siyueren.demo.redisdistributedlocker.dao.BalanceDao;
import com.siyueren.demo.redisdistributedlocker.redis.RedisDistributedLocker;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 乔健勇
 * @date 19:49 2020/7/6
 * @email qjyoung@163.com
 * @description
 */
@Component
public class LockerTest {
    @Resource
    private BalanceDao balanceDao;
    @Resource
    private RedisDistributedLocker locker;
    
    @Async
    @Scheduled(fixedRate = 2)
    public void test() throws Exception { // 多线程访问
        if (balanceDao.getBalance() <= 0) {
            return;
        }
//        testSpend();
        testSpendWithCallBack();
//        testSpendWithMutexAnnotation();
    }
    
    public void testSpend() throws Exception {
        balanceDao.spend(2);
    }
    
    public void testSpendWithCallBack() throws Exception {
        final String key = String.format("lock:balance:%s", 1);
        locker.executeIfLocked(key, 5, () -> {
            balanceDao.spend(2);
        });
    }
    
    public void testSpendWithMutexAnnotation() throws Exception {
        balanceDao.mutexSpend(1, 2);
//        balanceDao.wrongTestMutexSpend(1, 2);
//        balanceDao.rightTestMutexSpend(1, 2);
    }
}
