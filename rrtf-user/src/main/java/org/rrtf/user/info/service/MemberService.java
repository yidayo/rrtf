package org.rrtf.user.info.service;

import java.util.List;

import org.rrtf.user.entity.Member;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;

public interface MemberService {
	int saveMember(Member member);

	List<Object> getMember(int no, int pages);
	
	UserInfo getUserInfo(User user,Member member);

}
