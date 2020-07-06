package com.siyueren.demo.redisdistributedlocker.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 乔健勇
 * @date 16:54 2020/1/10
 * @email qjyoung@163.com
 * @description 锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Mutex {
    /** key 模板 */
    String keyTemplate();
    
    /** key 参数名称 */
    String[] argNames() default {};
    
    /** key 过期时间，默认5秒 */
    int expireSeconds() default 5;
    
    /** 获取锁重试时长 */
    int trySeconds() default 1;
    
    /** key 获取锁失败，是否抛异常 */
    boolean throwIfLockFail() default true;
}
