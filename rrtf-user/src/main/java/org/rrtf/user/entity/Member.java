package org.rrtf.user.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * 会员实体类
 * 
 * @author yubing
 *
 */
@Entity
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private int memberId;// 会员ID
	private int userId;// 用户ID
	private String email;// 邮箱
	private String sex;// 性别
	private String telephone;// 电话
	private Date birthday;// 生日
	private String signature;// 个性签名
	private int rpb;// 托福币

	@OneToOne
	@JoinColumn(name = "gradeId", referencedColumnName = "gradeId")
	private Grade grade;// 会员等级

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getRpb() {
		return rpb;
	}

	public void setRpb(int rpb) {
		this.rpb = rpb;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Member [memberId=" + memberId + ", userId=" + userId + ", email=" + email + ", sex=" + sex
				+ ", telephone=" + telephone + ", birthday=" + birthday + ", signature=" + signature + ", rpb=" + rpb
				+ ", grade=" + grade + "]";
	}
}
