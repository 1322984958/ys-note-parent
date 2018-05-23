package com.gkys.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gkys.common.mail.MailUtil;
import com.gkys.common.mail.MimeMessageDTO;



public class MailTest {

	public static void main(String[] args) {

		// //测试163邮箱
		// 测试qq邮箱
		// 新浪邮箱 13077403326m@sina.cn
		// 139邮箱 13077403326@139.com

		String userName = "13927734916@163.com"; // 用户邮箱地址
		String password = "******"; // 密码或者授权码
		String targetAddress = "1322984958@qq.com"; // 接受者邮箱地址

		// 设置邮件内容
		MimeMessageDTO mimeDTO = new MimeMessageDTO();
		mimeDTO.setSentDate(new Date());
		mimeDTO.setSubject("邮件的标题");
		mimeDTO.setText("邮件的内容<img src='http://static.oschina.net/uploads/user/111/223750_100.jpg'>"
				+ targetAddress);

//		// 发送单邮件
//		if (MailUtil.sendEmail(userName, password, targetAddress, mimeDTO)) {
//			System.out.println("邮件发送成功！");
//		} else {
//			System.out.println("邮件发送失败!!!");
//		}
		// 发送单邮件(附件)
		List<String> filepath=new ArrayList<String>();
		filepath.add("D:/test.xls");
//		if (MailUtil.sendEmailByFile(userName, password, targetAddress, mimeDTO,filepath)) {
//			System.out.println("邮件发送成功！");
//		} else {
//			System.out.println("邮件发送失败!!!");
//		}
		// 群发邮件
		targetAddress = "1322984958@qq.com,1322984958@qq.com";
//		if (MailUtil.sendGroupEmail(userName, password, targetAddress, mimeDTO)) {
//			System.out.println("邮件发送成功！");
//		} else {
//			System.out.println("邮件发送失败!!!");
//		}
//		// 群发邮件(附件)
		if (MailUtil.sendGroupEmailByFile(userName, password, targetAddress, mimeDTO,filepath)) {
			System.out.println("邮件发送成功！");
		} else {
			System.out.println("邮件发送失败!!!");
		}

	}

}
