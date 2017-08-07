package com.gemicle.managers;

import java.util.Date;
import java.util.List;

import com.gemicle.interfaces.ServiceHibernate;
import com.gemicle.pojo.User;
import com.gemicle.service.UserService;

public class UserDb {

	private static ServiceHibernate<User> userService = new UserService();

	public User save(String sessionId, String username) {
		User user = new User();
		user.setLogin(username);
		user.setSessionId(sessionId);
		user.setDateCreate(new Date());
		user.setId(userService.save(user));

		UserManager.add(user);
		return user;
	}

	public void delete(String sessionId) {

		User user = getUserFromSessionId(sessionId);
		if (user == null) {
			return;
		}
		userService.delete(user);
	}

	public User getUserFromSessionId(String sessionId) {
		List<User> users = userService.getList();
		for(User user:users){
			if(sessionId.equals(user.getSessionId())){
				return user;
			}
		}
		return null;
	}
}
