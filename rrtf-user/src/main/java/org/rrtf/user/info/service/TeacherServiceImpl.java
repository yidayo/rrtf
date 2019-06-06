package org.rrtf.user.info.service;

import java.sql.Date;

import javax.annotation.Resource;

import org.rrtf.user.dao.TeacherRepository;
import org.rrtf.user.entity.Teacher;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
	@Resource
	TeacherRepository teacherRepository;

	// 保存教师修改信息
	@Override
	public int saveTeacher(Teacher teacher) {
		int userId = teacher.getUserId();
		String realname = teacher.getRealname();
		String sex = teacher.getSex();
		String edu = teacher.getEdu();
		String email = teacher.getEmail();
		String tel = teacher.getTel();
		Date birth = teacher.getBirth();
		String introduce = teacher.getIntroduce();
		teacherRepository.modifyTeacher(realname, sex, edu, email, tel, birth, introduce, userId);
		return 1;
	}

	@Override
	public UserInfo teacherInfo(User user, Teacher teacher) {
		UserInfo userInfo = new UserInfo();
		userInfo.setSex(teacher.getSex());
		userInfo.setEmail(teacher.getEmail());
		userInfo.setTel(teacher.getTel());
		userInfo.setBirth(teacher.getBirth());
		userInfo.setPicture(user.getPicture());
		userInfo.setUsername(user.getUsername());
		userInfo.setStatus(user.getStatus());
		userInfo.setRegtime(user.getRegtime());
		return userInfo;
	}

}
