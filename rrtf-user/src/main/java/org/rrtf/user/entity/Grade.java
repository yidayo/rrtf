package org.rrtf.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

/**
 * 等级实体类
 * 
 * @author yubing
 *
 */

@Entity
@Component
public class Grade implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private int gradeId;// 等级ID
	private String grade;// 会员等级
	private int rpb;// 对应等级需要的RPB

	public int getGradeId() {
		return gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getRpb() {
		return rpb;
	}

	public void setRpb(int rpb) {
		this.rpb = rpb;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Grade [gradeId=" + gradeId + ", grade=" + grade + ", rpb=" + rpb + "]";
	}
}
