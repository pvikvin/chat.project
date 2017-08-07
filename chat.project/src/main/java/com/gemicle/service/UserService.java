package com.gemicle.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.gemicle.interfaces.HibernateService;
import com.gemicle.pojo.User;
import com.gemicle.session.SessionService;

public class UserService implements HibernateService<User> {

	private SessionService hibernateService = new SessionService();

	@Override
	public long save(User user) {
		Session session = hibernateService.openSession();
		long id = (Long) session.save(user);
		hibernateService.commitAndCloseSession(session);
		return id;
	}

	@Override
	public void delete(User user) {
		Session session = hibernateService.openSession();
		session.delete(user);
		hibernateService.commitAndCloseSession(session);
	}

	@Override
	public User get(long id) {
		Session session = hibernateService.openSession();
		User user = (User) session.get(User.class, id);
		hibernateService.commitAndCloseSession(session);
		return user;
	}

	@Override
	public List<User> getList() {
		Session session = hibernateService.openSession();
		Query query = session.createQuery("FROM User");
		List result = query.list();
		return result;
	}

}
