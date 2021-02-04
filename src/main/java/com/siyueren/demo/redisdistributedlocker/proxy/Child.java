package com.siyueren.demo.redisdistributedlocker.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 乔健勇
 * @date 16:20 2021/2/4
 * @email qjyoung@163.com
 */
@Slf4j
public class Child implements Human {
    Parent parent;
    
    public Child() {
        parent = new Parent();
    }
    
    @Override
    public List<Fruit> buyFruit() {
        play(3);
        List<Fruit> fruits = parent.buyFruit();
        for (Fruit fruit : fruits) {
            eatFruit(fruit);
        }
        log.debug("buyFruit() 玩到天黑，买的水果丢到路边找不到了");
        return new ArrayList<>();
    }
    
    @Override
    public int eatFruit(Fruit fruit) {
        fruit.setCount(fruit.getCount() - 1);
        log.debug("buyFruit() 吃一个{}, 剩余:{}", fruit.getName(), fruit.getCount());
        return 1;
    }
    
    @Override
    public void sleep() {
        log.debug("sleep() 一天睡24个小时");
    }
    
    private void play(long seconds) {
        log.debug("buyFruit() 耍10分钟");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
