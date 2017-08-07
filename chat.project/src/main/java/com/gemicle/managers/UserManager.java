package com.gemicle.managers;

import java.util.ArrayList;
import java.util.List;

import com.gemicle.pojo.User;

public class UserManager {

	private static List<User> users = new ArrayList<User>();

	public static void add(User user) {
		users.add(user);
	}

	public static List<User> getUsers() {
		return users;
	}

	public static void delete(User user) {
		for (int i = 0; i < users.size(); i++) {
			if(users.get(i).equals(user)){
				users.remove(i);
			}
		}
	}

}
