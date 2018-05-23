package com.gkys.common.base;

import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.Page;

/**
 * 统一返回结果类
 */
public class BaseResult {

    // 状态码：1成功，其他为失败
    public int code;

    // 成功为success，其他为失败原因
    public String message;

    public long total;

    // 数据结果集
    public Object data;

    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public static BaseResult success() {
        return new BaseResult(1,"操作成功",null);
    }

    public static BaseResult success(Object data) {
        BaseResult baseResult=new BaseResult(1,"操作成功",data);
        if(data instanceof Page){
            Page pageData = (Page)data;
            baseResult.setTotal(pageData.getTotal());
        }
        return baseResult;
    }
    
    public static BaseResult success(Object result, int pages) {
    	BaseResult baseResult=success(result);
    	baseResult.setTotal(pages);
		return baseResult;
	}

    public boolean isSuccess() {
        return code==1;
    }

    public static BaseResult fail(Object errors) {
        return new BaseResult(-1,"操作失败",errors);
    }
    
    public BaseResult addData(String key,Object value){
    	if(this.getData()==null){
    		this.data=new HashMap<>();
    	}
    	Map data=(Map)this.data;
    	data.put(key, value);
    	return this;
    }

}
