package org.rrtf.user.info.service;

import javax.annotation.Resource;

import org.rrtf.user.dao.GradeRepository;
import org.rrtf.user.dao.MemberRepository;
import org.rrtf.user.dao.TeacherRepository;
import org.rrtf.user.dao.UserRepository;
import org.rrtf.user.entity.Grade;
import org.rrtf.user.entity.Member;
import org.rrtf.user.entity.Teacher;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	TeacherRepository teacherRepository;

	@Resource
	MemberRepository memberRepository;

	@Resource
	GradeRepository gradeRepository;

	@Resource
	UserRepository userRepository;

	// 获取用户信息
	@Override
	public UserInfo getUserInfo(User user) {
		UserInfo userInfo = new UserInfo();
		// 设置用户信息
		userInfo.setUsername(user.getUsername());
		userInfo.setStatus(user.getStatus());
		userInfo.setRegtime(user.getRegtime());
		userInfo.setPicture(user.getPicture());
		// 获取当前用户角色
		String name = user.getName();
		// 获取当前用户ID
		Integer userId = user.getUserId();
		if ("teacher".equals(name)) {// 角色为teacher时
			userInfo.setName("teacher");
			Teacher teacher = null;
			// 查询teacher_id=userId的教师
			teacher = teacherRepository.findByUserId(userId);
			// 当当前用户为第一次登录时,即上一步查询teacher=null, 则在teacher表中添加一条user_id=userId的记录
			if (teacher == null) {
				teacher = new Teacher();
				teacher.setUserId(userId);
				// 添加当前教师对象
				teacherRepository.save(teacher);
				// 获取当前教师对象
				teacher = teacherRepository.findByUserId(userId);
			}
			userInfo.setRealname(teacher.getRealname());
			userInfo.setSex(teacher.getSex());
			userInfo.setEdu(teacher.getEdu());
			userInfo.setEmail(teacher.getEmail());
			userInfo.setTel(teacher.getTel());
			userInfo.setBirth(teacher.getBirth());
			userInfo.setSignature(teacher.getIntroduce());
		}
		if ("vip".equals(name)) {// 角色为vip时
			userInfo.setName("member");
			Member member = null;
			// 查询member_id=userId的教师
			member = memberRepository.findByUserId(userId);
			// 当当前用户为第一次登录时,即上一步查询member=null, 则在member表中添加一条user_id=userId的记录
			if (member == null) {
				member = new Member();
				member.setUserId(userId);
				// 设置会员的等级
				Grade grade = new Grade();
				grade.setGradeId(1);
				member.setGrade(grade);
				// 设置新注册会员的初始托福币
				member.setRpb(500);
				// 在数据库中添加当前会员对象
				memberRepository.save(member);
				// 获取当前会员对象
				member = memberRepository.findByUserId(userId);
			}
			int rpb = member.getRpb();
			// 查询当前用户对应的等级ID
			int id = member.getGrade().getGradeId();
			if (id == 5) {
				userInfo.setGrade("一统天下");
			} else {
				// 获取当前等级的下一等级对象
				Grade grade = gradeRepository.findByGradeId(id + 1);
				userInfo.setNextGrade(grade.getGrade());
				// 设置升级还差的托福币
				userInfo.setNextRpb(grade.getRpb() - rpb);
			}
			userInfo.setRpb(rpb);
			userInfo.setSex(member.getSex());
			userInfo.setEmail(member.getEmail());
			userInfo.setTel(member.getTelephone());
			userInfo.setBirth(member.getBirthday());
			userInfo.setSignature(member.getSignature());
			userInfo.setGrade(member.getGrade().getGrade());
		}
		return userInfo;
	}

}
