package com.siyueren.demo.redisdistributedlocker.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 乔健勇
 * @date 17:05 2021/2/4
 * @email qjyoung@163.com
 */
@Slf4j
public class Main {
    
    public static void check(List<Human.Fruit> fruits) {
        if (CollectionUtils.isEmpty(fruits)) {
            log.debug("check() 啥也没有");
            return;
        }
        for (Human.Fruit fruit : fruits) {
            log.debug("check() {}:{}个", fruit.getName(), fruit.getCount());
        }
    }
}
