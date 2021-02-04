package com.siyueren.demo.redisdistributedlocker.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 * @author 乔健勇
 * @date 16:27 2021/2/4
 * @email qjyoung@163.com
 */
@Slf4j
public class DynamicProxy extends Main {
    
    public static void main(String[] args) {
        Arrays.asList(new Parent(), new Child()).forEach(human -> {
            Class<?> cls = human.getClass();
            ClassLoader loader = cls.getClassLoader();
            Human humanProxyInstance = (Human) Proxy.newProxyInstance(loader, cls.getInterfaces(), new LogProxy(human));
            
            List<Human.Fruit> fruits1 = humanProxyInstance.buyFruit();
            check(fruits1);
            for (Human.Fruit fruit : fruits1) {
                humanProxyInstance.eatFruit(fruit);
            }
            humanProxyInstance.sleep();
            System.out.println();
        });
        
    }
}
