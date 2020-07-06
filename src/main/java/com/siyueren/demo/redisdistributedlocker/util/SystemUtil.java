package com.siyueren.demo.redisdistributedlocker.util;

import java.util.UUID;

/**
 * @author 乔健勇
 * @date 21:09 2019/11/27
 * @email qjyoung@163.com
 */
public class SystemUtil {
    
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}