package org.rrtf.user.info.service;

import org.rrtf.user.entity.Teacher;
import org.rrtf.user.entity.User;
import org.rrtf.user.entity.UserInfo;

public interface TeacherService {
	int saveTeacher(Teacher teacher);

	UserInfo teacherInfo(User user, Teacher teacher);
}
