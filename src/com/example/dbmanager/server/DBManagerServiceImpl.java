package com.example.dbmanager.server;

import com.example.dbmanager.client.DBManagerService;
import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.example.dbmanager.util.HibernateUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DBManagerServiceImpl extends RemoteServiceServlet implements
									DBManagerService {
    //projects

    public List<Project> getProjectsByPersonId(Long id) {
        Person person = findPersonById(id);
        return person.getProjects();
    }

    public Project findProjectById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Project> res = session.createQuery("from Project where id = " + id).list();
        session.getTransaction().commit();
        return res.get(0);
    }

    public List<Project> getProjects() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Project> projects = session.createQuery("from Project").list();
        session.getTransaction().commit();
        return projects;
    }

    public Long saveProject(Project aProject) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        aProject.getPersons().add(new Person());
        session.save(aProject);
        session.getTransaction().commit();
        return aProject.getId();
    }

    public void updateProject(Project aProject) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(aProject);
        session.getTransaction().commit();
    }


    public Integer removeProject(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from Project where id = " + id);
        int i = new Integer(query.executeUpdate());
        session.getTransaction().commit();
        return i;
    }

    // persons

    public List<Person> getPersonsByPprojectId(Long id) {
        Project project = findProjectById(id);
        return project.getPersons();
    }

    public Person login(String login, String password){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person> res = session.createQuery("from Person where login = '" + login+"' and password = '"+password+"'").list();
        session.getTransaction().commit();
        return res.get(0);
    }

    public Person findPersonById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person> res = session.createQuery("from Person where id = " + id).list();
        session.getTransaction().commit();
        return res.get(0);
    }

  	public List<Person> getPeople() {
  		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
  		session.beginTransaction();
  		List<Person> people = session.createQuery("from Person").list();
  		session.getTransaction().commit();
	  	return people;
  	}

	public Long savePerson(Person aPerson) {
	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    session.beginTransaction();
	    session.save(aPerson);
	    session.getTransaction().commit();
	    return aPerson.getId();
	}

	public void updatePerson(Person aPerson) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.update(aPerson);
		session.getTransaction().commit();
	}

	
	public Integer removePerson(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("delete from Person where id = " + id);
        int i = new Integer(query.executeUpdate());
        session.getTransaction().commit();
		return i;
	}
}
