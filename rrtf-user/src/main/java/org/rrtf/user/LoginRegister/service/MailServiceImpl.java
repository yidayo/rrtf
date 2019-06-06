package org.rrtf.user.LoginRegister.service;

import java.io.File;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
	@Resource
	JavaMailSender mailSender;

	// 从属性文件中获取邮件发送方的邮件地址
	@Value("${mail.fromMail.addr}")
	String from;

	//final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Override
	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
		//logger.info("邮件发送成功");
	}

	@Override
	public void sendAttachMail(String[] to, String subject, String content, String filePath) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// 创建可也包含多媒体文件的一个邮件missage
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content);
			FileSystemResource resource = new FileSystemResource(filePath);
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			System.out.println(filePath);
			System.out.println(fileName);
			System.out.println(File.separator);
			helper.addAttachment(fileName, resource);
			mailSender.send(message);// 发送邮件
		} catch (MessagingException e) {
			//logger.info("邮件发送成功" + e.getMessage());
		}

	}

	@Override
	public void sendInlineMail(String to, String subject, String content, String filePath) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// 创建可也包含多媒体文件的一个邮件missage
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			// 到文件路径下找该文件
			FileSystemResource resource = new FileSystemResource(filePath);
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
			helper.addInline("fbb", resource);// 在邮件中添加静态资源
			mailSender.send(message);// 发送邮件
		} catch (MessagingException e) {
			//logger.info("邮件发送成功" + e.getMessage());
		}
	}

}
