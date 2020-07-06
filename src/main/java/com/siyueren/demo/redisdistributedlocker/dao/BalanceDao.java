package com.siyueren.demo.redisdistributedlocker.dao;

import com.siyueren.demo.redisdistributedlocker.redis.Mutex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

/**
 * @author 乔健勇
 * @date 19:55 2020/7/6
 * @email qjyoung@163.com
 */
@Slf4j
@Component
public class BalanceDao {
    
    private static int BALANCE_IN_DB = 4;
    
    /** 模拟从db取值 */
    public int getBalance() throws Exception {
        Thread.sleep(20); // 模拟耗时操作
        return BALANCE_IN_DB;
    }
    
    /** 模拟更新db数据 */
    private void updateBalance(int newBalance) throws Exception {
        Thread.sleep(20); // 模拟耗时操作
        BALANCE_IN_DB = newBalance;
    }
    
    public void spend(int money) throws Exception {
        final int currentBalance = getBalance();
        if (currentBalance >= money) {
            updateBalance(currentBalance - money);
            log.debug("-----------------spend(), thread.name:[{}], currentBalance:[{}], spend:[{}]-----------------", Thread.currentThread().getName(), currentBalance, money);
        }
    }
    
    @Mutex(keyTemplate = "lock:balance:%s", argNames = "userId", throwIfLockFail = false)
    public void mutexSpend(int userId, int money) throws Exception {
        spend(money);
    }
    
    /**
     * 同一个class里面调用代理方法不生效，所以以下用法注解锁不会起作用
     */
    public void wrongTestMutexSpend(int userId, int money) throws Exception {
        mutexSpend(userId, money);
    }
    
    /**
     * 正确用法
     */
    public void rightTestMutexSpend(int userId, int money) throws Exception {
        ((BalanceDao) AopContext.currentProxy()).mutexSpend(userId, money);
    }
}
