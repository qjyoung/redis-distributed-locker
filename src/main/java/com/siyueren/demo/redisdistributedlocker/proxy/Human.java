package com.siyueren.demo.redisdistributedlocker.proxy;

import lombok.Data;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 乔健勇
 * @date 16:08 2021/2/4
 * @email qjyoung@163.com
 */
public interface Human {
    
    @Data
    class Fruit {
        String name;
        int count;
    }
    
    List<Fruit> buyFruit();
    
    default int eatFruit(Fruit fruit) {
        LoggerFactory.getLogger(Human.class).debug("eatFruit() 不喜欢吃水果");
        return 0;
    }
    
    default void sleep() {
        LoggerFactory.getLogger(Human.class).debug("sleep() 一天睡8个小时");
    }
}
