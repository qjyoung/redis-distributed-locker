package com.siyueren.demo.redisdistributedlocker.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 乔健勇
 * @date 16:27 2021/2/4
 * @email qjyoung@163.com
 */
@Slf4j
public class StaticProxy extends Main{
    
    public static void main(String[] args) throws Exception {
        Human dad = new Parent();
        List<Human.Fruit> fruits = dad.buyFruit();
        check(fruits);
        System.out.println();
        
        Human son = new Child();
        fruits = son.buyFruit();
        check(fruits);
    }
}
