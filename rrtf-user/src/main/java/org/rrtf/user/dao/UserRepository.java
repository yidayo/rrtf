package org.rrtf.user.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.rrtf.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	// 通过用户名查询用户
	User findByUsername(String username);

	// 通过用户ID查询用户
	User findByUserId(int userId);

	// 保存用户头像方法
	@Transactional
	@Modifying
	@Query("update User set picture = ?1 where username = ?2")
	int savePicture(String picture, String username);

	// 更新用户密码方法
	@Transactional
	@Modifying
	@Query("update User set password = ?1,salt = ?2 where username = ?3")
	int updatePassword(String password, String salt, String username);

	// 更新用户状态
	@Transactional
	@Modifying
	@Query("update User set status = ?1 where username in (?2)")
	int updateStatus(int status, List<String> username);

	// 删除用户
	@Transactional
	@Modifying
	@Query("delete from User where username in (?1)")
	int deleteUser(List<String> username);

}
