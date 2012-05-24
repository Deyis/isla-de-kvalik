package com.example.dbmanager.server;

import com.example.dbmanager.client.DBManagerService;
import com.example.dbmanager.domain.Document;
import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Person_Project;
import com.example.dbmanager.domain.Project;
import com.example.dbmanager.util.HibernateUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hsqldb.persist.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DBManagerServiceImpl extends RemoteServiceServlet implements
									DBManagerService {
    //documents
    public List<Document> getDocuments() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Document> documents = session.createQuery("from Document").list();
        session.getTransaction().commit();
        return documents;
    }

    public Document findDocumentById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Document> res = session.createQuery("from Document where id = " + id).list();
        session.getTransaction().commit();
        return res.get(0);
    }

    public Long saveDocument(Document aDocument) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(aDocument);
        session.getTransaction().commit();
        return aDocument.getId();
    }

    public void updateDocument(Document aDocument) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(aDocument);
        session.getTransaction().commit();
    }

    public Integer removeDocument(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from Document where id = " + id);
        int i = new Integer(query.executeUpdate());
        session.getTransaction().commit();
        return i;
    }

    public List<Document> getDocumentsByProjectId(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Document> resId = session.createQuery("from Document where projectId = " + id).list();
        session.getTransaction().commit();
        return resId;
    }

    public List<Document> getDocumentsByPersonIdAndProjectId(Long personId, Long projectId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Document> resId = session.createQuery("from Document where personId = " + personId + " and projectId = " + projectId).list();
        session.getTransaction().commit();
        return resId;
    }

    public List<Document> getDocumentsByOwnerId(Long ownerId) {
        List<Document> list = getDocuments();
        List<Document> resList = new ArrayList<Document>();
        
        for(Document document: list) {
            Project tmp = findProjectById(document.getProjectId());
            if(tmp.getManagerId() == ownerId) {
                resList.add(document);
            }
        }
        return resList;
    }

    //projects
    public List<Project> getProjectsByPersonId(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person_Project> resId = session.createQuery("from Person_Project where personId = " + id).list();
        session.getTransaction().commit();
        List<Project> projectList = new ArrayList<Project>();
        for(Person_Project i: resId) {
            projectList.add(findProjectById(i.getProjectId()));
        }
        return projectList;
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
//        aProject.getPersons().add(new Person());
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
    public Long addPersonToProject(Long personId, Long projectId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Person_Project save = new Person_Project();
        save.setPersonId(personId);
        save.setProjectId(projectId);
        session.save(save);
        session.getTransaction().commit();
        return save.getId();
    }

    public int removeProjectFromPersonByIds(Long personId, Long projectId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from Person_Project where projectId = " + projectId + " and personId = " +personId);
        int i = new Integer(query.executeUpdate());
        session.getTransaction().commit();
        return i;
    }

    public List<Person> getPersonsByPprojectId(Long id) {
//        Project project = findProjectById(id);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person_Project> resId = session.createQuery("from Person_Project where projectId = " + id).list();
        session.getTransaction().commit();
        List<Person> personList = new ArrayList<Person>();
        for(Person_Project i: resId) {
            personList.add(findPersonById(i.getPersonId()));
        }
        return personList;
//        return new ArrayList<Person>(project.getPersons());
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
