package com.siyueren.demo.redisdistributedlocker.util;

import com.siyueren.demo.redisdistributedlocker.exception.BusinessException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author 乔健勇
 * @date 15:19 2019/11/27
 * @email qjyoung@163.com
 */
public class SpringUtils implements ApplicationContextAware {
    
    private static ApplicationContext ctx;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }
    
    /** 获取指定Bean类型的实例对象 */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        if (ctx == null) {
            throw new BusinessException("spring上下文对象未初始化，无法完成bean的查找！");
        }
        return ctx.getBean(requiredType);
    }
    
    /** 获取指定Bean名称的实例对象 */
    public static Object getBean(String name) throws BeansException {
        if (ctx == null) {
            throw new BusinessException("spring上下文对象未初始化，无法完成bean的查找！");
        }
        return ctx.getBean(name);
    }
}
