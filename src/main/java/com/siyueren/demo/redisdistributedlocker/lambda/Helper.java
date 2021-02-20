package com.siyueren.demo.redisdistributedlocker.lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author 乔健勇
 * @date 11:16 2021/1/4
 * @email qjyoung@163.com
 */
public class Helper {
    
    private static final Logger log = LoggerFactory.getLogger(Helper.class);
    
    public static <T> T retryIfFail(Callable<T> callable) {
        return retryIfFail(5, callable);
    }
    
    public static <T> T retryIfFail(int maxTimes, Callable<T> callable) {
        return retryIfFail(maxTimes, 1, callable);
    }
    
    /**
     * @param maxTimes     最大重试次数
     * @param sleepSeconds 每次重试间隔时间
     * @param callable     处理器
     *
     * @author 乔健勇
     * @date 11:16 2021/1/4
     * @email qjyoung@163.com
     * @description 幂等操作异常重试
     */
    public static <T> T retryIfFail(int maxTimes, int sleepSeconds, Callable<T> callable) {
        int times = 1;
        do {
            try {
                return callable.call();
            } catch (Exception e) {
                log.error("retryIfFail({}) error", times, e);
                try {
                    Thread.sleep(sleepSeconds * 1000);
                } catch (InterruptedException ex) {
                    log.error("retryIfFail error", ex);
                    throw new RuntimeException("retryIfFail, error" + ex.getMessage());
                }
            }
        } while (++times <= maxTimes);
        throw new RuntimeException("retryIfFail, reach the max times:" + maxTimes);
    }
    
    public static void main(String[] args) {
        Integer res = Helper.retryIfFail(() -> {
            int[] nums = {0, 0, 1};
            return 1 / nums[(int) (System.currentTimeMillis() % 3)];
        });
        System.out.println("res=" + res);
    }
}
