package org.rrtf.user.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 保存用户信息类
 * 
 * @author yubing
 *
 */
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;// 用户名
	private String name;// 用户类别
	private String sex;// 性别
	private String email;// 邮箱
	private String grade;// 等级
	private String nextGrade;// 下一等级
	private int rpb;// 托福币
	private int nextRpb;// 升级为下一等级还需要的托福币
	private String edu;// 学历
	private String realname;// 真实姓名
	private String tel;// 电话
	private Date birth;// 生日
	private String signature;// 个性签名
	private String picture;// 用户头像
	private Date regtime;// 用户注册时间
	private int status;// 用户状态

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getNextGrade() {
		return nextGrade;
	}

	public void setNextGrade(String nextGrade) {
		this.nextGrade = nextGrade;
	}

	public int getRpb() {
		return rpb;
	}

	public void setRpb(int rpb) {
		this.rpb = rpb;
	}

	public int getNextRpb() {
		return nextRpb;
	}

	public void setNextRpb(int nextRpb) {
		this.nextRpb = nextRpb;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date date) {
		this.regtime = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", name=" + name + ", sex=" + sex + ", email=" + email + ", grade="
				+ grade + ", nextGrade=" + nextGrade + ", rpb=" + rpb + ", nextRpb=" + nextRpb + ", edu=" + edu
				+ ", realname=" + realname + ", tel=" + tel + ", birth=" + birth + ", signature=" + signature
				+ ", picture=" + picture + ", regtime=" + regtime + ", status=" + status + "]";
	}

}
