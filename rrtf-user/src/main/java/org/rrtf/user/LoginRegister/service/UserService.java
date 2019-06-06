package org.rrtf.user.LoginRegister.service;

import org.rrtf.user.entity.User;

public interface UserService {
	
	User findService(String username);

	public boolean checkUsername(String username);
	
	boolean registerData(String username, String password);

	boolean registerTeacher(String username);
	
}
