package org.rrtf.user.dao;

import java.sql.Date;

import javax.transaction.Transactional;

import org.rrtf.user.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	// 通过用户ID查询用户
	Teacher findByUserId(int id);

	// 更新教师信息
	@Transactional
	@Modifying
	@Query(value = "update teacher set realname = ?1,sex = ?2,edu = ?3,email = ?4,tel = ?5,birth = ?6,introduce = ?7 where user_id = ?8", nativeQuery = true)
	int saveTeacher(int userId);

	// 更新教师信息
	@Transactional
	@Modifying
	@Query(value = "update teacher set realname = ?1,sex = ?2,edu = ?3,email = ?4,tel = ?5,birth = ?6,introduce = ?7 where user_id = ?8", nativeQuery = true)
	int modifyTeacher(String realname, String sex, String edu, String email, String tel, Date birth, String introduce,
			int userId);

	// 添加教师信息
	@Transactional
	@Modifying
	@Query(value = "update teacher set realname = ?1,sex = ?2,edu = ?3,email = ?4,tel = ?5,introduce = ?6 where user_id = ?7", nativeQuery = true)
	int addTeacher(String realname, String sex, String edu, String email, String tel, String introduce, int userId);
}
