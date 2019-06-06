package org.rrtf.user.dao;

import org.rrtf.user.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
	// 根据等级ID查询等级相关信息
	Grade findByGradeId(int gradeId);
}
