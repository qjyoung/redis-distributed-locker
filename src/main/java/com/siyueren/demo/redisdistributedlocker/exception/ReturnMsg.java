package com.siyueren.demo.redisdistributedlocker.exception;

import com.alibaba.fastjson.annotation.JSONField;
import com.siyueren.demo.redisdistributedlocker.util.SpringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

public class ReturnMsg {
    
    private boolean status;
    
    @JSONField(ordinal = 1)
    private Integer code;
    
    @JSONField(ordinal = 2)
    private String msg;
    
    @JSONField(ordinal = 3)
    private Object data;
    
    @JSONField(serialize = false)
    private ICodeEnum returnEnum;
    
    @JSONField(serialize = false)
    private Object[] args;
    
    public ReturnMsg() {
        status = true;
        this.returnEnum = CodeEnumBase.SUCCESS;
    }
    
    public ReturnMsg(Object data) {
        status = true;
        this.returnEnum = CodeEnumBase.SUCCESS;
        this.data = data;
    }
    
    public ReturnMsg(ICodeEnum returnEnum) {
        this.returnEnum = returnEnum;
    }
    
    public ReturnMsg(ICodeEnum returnEnum, String msg) {
        this.msg = msg;
        this.returnEnum = returnEnum;
    }
    
    public ReturnMsg(ICodeEnum returnEnum, Object... args) {
        this.returnEnum = returnEnum;
        this.args = args;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public Integer getCode() {
        if (code != null) {
            return code;
        }
        if (returnEnum != null) {
            return returnEnum.getCode();
        }
        return null;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMsg() {
        if (!StringUtils.isEmpty(msg)) {
            return msg;
        } else if (returnEnum != null) {
            final ResourceBundleMessageSource messageSource = SpringUtils.getBean(ResourceBundleMessageSource.class);
            return messageSource.getMessage(returnEnum.getMessageKey(), this.args, LocaleContextHolder.getLocale());
        }
        return "";
    }
    
    public void setMsg(String message) {
        this.msg = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}
