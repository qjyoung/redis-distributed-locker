package com.siyueren.demo.redisdistributedlocker.exception;

/**
 * @author 乔健勇
 * @date 18:54 2019/11/27
 * @email qjyoung@163.com
 */
public class BusinessException extends RuntimeException {
    private ICodeEnum codeEnum;
    
    public BusinessException(ICodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(Throwable throwable) {
        super(throwable);
    }
    
    public BusinessException(String message, ICodeEnum codeEnum) {
        super(message);
        this.codeEnum = codeEnum;
    }
    
    public ICodeEnum getCodeEnum() {
        return codeEnum;
    }
    
    public void setCodeEnum(ICodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }
}
