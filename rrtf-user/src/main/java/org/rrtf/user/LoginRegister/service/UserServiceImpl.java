package org.rrtf.user.LoginRegister.service;

import java.sql.Date;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.rrtf.user.dao.UserRepository;
import org.rrtf.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userInfoRepository;

	@Override
	public User findService(String username) {
		User user = userInfoRepository.findByUsername(username);
		return user;
	}

	/*
	 * 判断数据库中是否存在该账户
	 */
	@Override
	public boolean checkUsername(String username) {
		User user = userInfoRepository.findByUsername(username);
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 添加新用户
	 */
	@Override
	public boolean registerData(String username, String password) {
		if (!checkUsername(username)) {// 看数据库中是否存在该账户
			return false;
		}
		String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); // 生成盐值
		String newPassword = new Md5Hash(password, salt, 2).toString(); // 生成的密文
		User user = new User();// 创建user对象保存注册用户信息
		user.setSalt(salt);
		user.setUsername(username);
		user.setPassword(newPassword);
		user.setName("vip");// 注册的用户都为会员
		Date regTime = new Date(System.currentTimeMillis());
		user.setRegtime(regTime);
		user.setStatus(1);
		userInfoRepository.save(user);// 将注册用户保存到数据库
		return true;
	}

	/*
	 * 添加讲师
	 */
	@Override
	public boolean registerTeacher(String username) {
		if (!checkUsername(username)) {// 看数据库中是否存在该账户
			return false;
		}
		String password = "123456";
		String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); // 生成盐值
		String newPassword = new Md5Hash(password, salt, 2).toString(); // 生成的密文
		User user = new User();// 创建user对象保存注册用户信息
		user.setUsername(username);
		user.setPassword(newPassword);
		user.setSalt(salt);
		user.setName("teacher");// 注册的用户都为讲师
		Date regTime = new Date(System.currentTimeMillis());
		user.setRegtime(regTime);
		user.setStatus(1);
		userInfoRepository.save(user);// 将注册用户保存到数据库
		return true;
	}

}
