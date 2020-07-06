package com.siyueren.demo.redisdistributedlocker.redis;

import com.siyueren.demo.redisdistributedlocker.exception.BusinessException;
import com.siyueren.demo.redisdistributedlocker.exception.CodeEnumBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Aspect
@Component
public class MutexAspect {
    
    @Resource
    RedisDistributedLocker locker;
    
    @Around(value = "@annotation(mutex)", argNames = "joinPoint,mutex")
    public Object doAround(ProceedingJoinPoint joinPoint, Mutex mutex) throws Throwable {
        final String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        final String[] argNames = mutex.argNames();
        Object[] args = new Object[argNames.length];
        final Object[] allArgs = joinPoint.getArgs();
        for (int i = 0; i < argNames.length; i++) {
            args[i] = allArgs[ArrayUtils.indexOf(parameterNames, argNames[i])];
        }
        final String key = String.format(mutex.keyTemplate(), args);
        final String lockId = locker.retryLock(key, mutex.expireSeconds(), mutex.trySeconds());
        if (StringUtils.isBlank(lockId)) {
            if (mutex.throwIfLockFail()) {
                throw new BusinessException(CodeEnumBase.BUSY);
            }
            return null;
        }
        
        Object object;
        try {
            object = joinPoint.proceed();
        } finally {
            locker.unlock(key, lockId);
        }
        return object;
    }
}
