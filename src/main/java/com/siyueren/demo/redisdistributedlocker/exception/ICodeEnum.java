package com.siyueren.demo.redisdistributedlocker.exception;

/**
 * @author 乔健勇
 * @date 14:56 2019/12/17
 * @email qjyoung@163.com
 * @description 返回码分装抽象
 */
public interface ICodeEnum {
    Integer getCode();
    
    String getMessageKey();
}