package com.siyueren.demo.redisdistributedlocker.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author 乔健勇
 * @date 16:18 2021/2/4
 * @email qjyoung@163.com
 */
@Slf4j
public class Parent implements Human {
    @Override
    public List<Fruit> buyFruit() {
        log.debug("buyFruit() 买10个苹果");
        Fruit apple = new Fruit();
        apple.setName("apple");
        apple.setCount(10);
        return Collections.singletonList(apple);
    }
}
