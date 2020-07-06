package com.siyueren.demo.redisdistributedlocker.exception;

public enum CodeEnumBase implements ICodeEnum {
    
    TEST(-999, "base.test.multiple.params"),
    
    SUCCESS(200, "base.success"),
    
    ERROR_PARAM(201, "base.error.params"),
    
    PHONE_VERIFY_ERROR(202, "base.phone.verify.error"),
    PHONE_VERIFY_ERROR_LIMIT(203, "base.phone.verify.error.limit"),
    CAPTCHA_ERROR(203, "base.captcha.error"),
    BUSY(204, "base.busy"),
    
    ERROR_FORBIDDEN(403, "base.error.forbidden"),
    
    INTERNAL_ERROR(500, "base.internal.error"),
    
    ;
    
    private Integer code;
    
    private String messageKey;
    
    CodeEnumBase(Integer code, String message) {
        this.code = code;
        this.messageKey = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessageKey() {
        return messageKey;
    }
    
    public void setMessage(String messageKey) {
        this.messageKey = messageKey;
    }
}
