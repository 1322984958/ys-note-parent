package com.gkys.common.service;

public interface IMailService {
    /**
     * 发送邮件
     * @param obj
     * @param text
     */
    public void sendEmail(Object obj,String text);
}