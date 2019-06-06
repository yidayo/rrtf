package org.rrtf.user.info.service;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.rrtf.user.dao.MemberRepository;
import org.rrtf.user.dao.UserRepository;
import org.rrtf.user.entity.Member;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;
import org.springframework.data.annotation.Reference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

	@Resource
	MemberRepository memberRepository;

	@Reference
	UserRepository userRepository;

	// 保存会员修改信息
	@Override
	public int saveMember(Member member) {
		int userId = member.getUserId();
		String sex = member.getSex();
		String email = member.getEmail();
		String telephone = member.getTelephone();
		Date birthday = member.getBirthday();
		String signature = member.getSignature();
		memberRepository.modifyMember(sex, email, telephone, birthday, signature, userId);
		return 1;
	}

	// 获取会员信息
	@Override
	public List<Object> getMember(int no, int pages) {
		UserInfo userInfo = new UserInfo();
		Pageable pageable = new PageRequest(no, pages);
		System.out.println(1111);
		Page<Member> member = memberRepository.findAll(pageable);
		System.out.println(1111);
		int totalPages = member.getTotalPages();
		List<Object> listMember = new LinkedList<Object>();
		listMember.add(totalPages);
		for (Member m : member) {
			userInfo.setSex(m.getSex());
			userInfo.setEmail(m.getEmail());
			userInfo.setTel(m.getTelephone());
			userInfo.setBirth(m.getBirthday());
			userInfo.setGrade(m.getGrade().getGrade());
			int userId = m.getUserId();
			User user = userRepository.findByUserId(userId);
			userInfo.setPicture(user.getPicture());
			userInfo.setUsername(user.getUsername());
			userInfo.setStatus(user.getStatus());
			userInfo.setRegtime(user.getRegtime());
			listMember.add(userInfo);
		}
		return listMember;
	}

	@Override
	public UserInfo getUserInfo(User user, Member member) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(user.getUsername());
		userInfo.setPicture(user.getPicture());
		userInfo.setRegtime(user.getRegtime());
		userInfo.setStatus(user.getStatus());
		userInfo.setSex(member.getSex());
		userInfo.setEmail(member.getEmail());
		userInfo.setTel(member.getTelephone());
		userInfo.setBirth(member.getBirthday());
		userInfo.setGrade(member.getGrade().getGrade());
		return userInfo;
	}

}
