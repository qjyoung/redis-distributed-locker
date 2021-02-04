package com.siyueren.demo.redisdistributedlocker.proxy;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 乔健勇
 * @date 16:52 2021/2/4
 * @email qjyoung@163.com
 */
@Slf4j
public class LogProxy implements InvocationHandler {
    private Object target;
    
    public LogProxy(Object target) {
        super();
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.debug("invoke() method：{}, start", method.getName());
        long startAt = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        log.debug("invoke() method：{}, result:{}, time elapsed:{}ms\n", method.getName(), JSON.toJSON(result), System.currentTimeMillis() - startAt);
        return result;
    }
}