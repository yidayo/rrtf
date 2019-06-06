package org.rrtf.user.dao;

import java.sql.Date;

import javax.transaction.Transactional;

import org.rrtf.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
	// 通过用户ID查询用户
	Member findByUserId(int id);

	// 更新会员信息
	@Modifying
	@Transactional
	@Query("update Member set sex = ?1,email = ?2,telephone = ?3,birthday = ?4,signature = ?5 where user_id = ?6")
	int modifyMember(String sex, String email, String telephone, Date birthday, String signature, int userId);

}
