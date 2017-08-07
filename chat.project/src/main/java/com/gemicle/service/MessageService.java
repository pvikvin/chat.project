package com.gemicle.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.gemicle.interfaces.ServiceHibernate;
import com.gemicle.pojo.Message;
import com.gemicle.session.SessionService;

public class MessageService implements ServiceHibernate<Message> {

	private SessionService hibernateService = new SessionService();

	@Override
	public long save(Message message) {
		Session session = hibernateService.openSession();
		long id = (Long) session.save(message);
		hibernateService.commitAndCloseSession(session);
		return id;
	}

	@Override
	public void delete(Message message) {
		Session session = hibernateService.openSession();
		session.delete(message);
		hibernateService.commitAndCloseSession(session);
	}

	@Override
	public Message get(long id) {
		Session session = hibernateService.openSession();
		Message message = (Message) session.get(Message.class, id);
		hibernateService.commitAndCloseSession(session);
		return message;
	}

	@Override
	public List<Message> getList() {
		Session session = hibernateService.openSession();
		Query query = session.createQuery("FROM message");
		List messages = query.list();
		return messages;
	}

}
