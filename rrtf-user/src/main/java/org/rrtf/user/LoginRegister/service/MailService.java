
package org.rrtf.user.LoginRegister.service;

public interface MailService {

	void sendSimpleMail(String to, String subject, String content);

	void sendAttachMail(String[] to, String subject, String content, String filePath);

	// 发送一个显示图片的邮件
	void sendInlineMail(String to, String subject, String content, String filePath);
}
