package com.gemicle.session;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

public class SessionService {
	private SessionFactoryBuild connector = new SessionFactoryBuild();
	
	public Session openSession(){
		SessionFactory sessionFactory = connector.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		return session;
	}
	
	public void commitAndCloseSession(Session session){
		session.getTransaction().commit();
	}
}
