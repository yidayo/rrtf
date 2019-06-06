package org.rrtf.user.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 教师实体类
 * 
 * @author yubing
 *
 */
@Entity
public class Teacher implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private int teacherId;// 教师ID
	private int userId;// 教师ID
	private String email;// 邮箱
	private String sex;// 性别
	private String edu;// 学历
	private String realname;// 真实姓名
	private String tel;// 电话
	private Date birth;// 生日
	private String introduce;// 自我描述

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Teacher [teacherId=" + teacherId + ", userId=" + userId + ", email=" + email + ", sex=" + sex + ", edu="
				+ edu + ", realname=" + realname + ", tel=" + tel + ", birth=" + birth + ", introduce=" + introduce
				+ "]";
	}
	
}
