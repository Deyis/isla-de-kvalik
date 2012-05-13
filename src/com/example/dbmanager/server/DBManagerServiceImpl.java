package com.example.dbmanager.server;

import com.example.dbmanager.client.DBManagerService;
import com.example.dbmanager.client.PersonDTO;
import com.example.dbmanager.client.ProjectDTO;
import com.example.dbmanager.domain.*;
import com.example.dbmanager.util.HibernateUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DBManagerServiceImpl extends RemoteServiceServlet implements
									DBManagerService {
    //projects

    public List<ProjectDTO> getProjectsByPersonId(Long id) {
        PersonDTO personDTO  = findPersonById(id);
        return new ArrayList<ProjectDTO>(personDTO.getProjects());
    }

    public ProjectDTO findProjectById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Project> res = session.createQuery("from createProject where id = " + id).list();
        ProjectDTO projectDTO = createProjectDTO(res.get(0));
        session.getTransaction().commit();
        return projectDTO;
    }

    public List<ProjectDTO> getProjects() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Project> projects = session.createQuery("from createProject").list();
        List<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>(projects != null ? projects.size() : 0);
        if (projects != null) {
            for (Project project : projects) {
                projectDTOs.add(createProjectDTO(project));
            }
        }
        session.getTransaction().commit();
        return projectDTOs;
    }

    public Long saveProject(ProjectDTO aProjectDTO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Project project = new Project();
        project.setName(aProjectDTO.getName());
        session.save(project);
        session.getTransaction().commit();
        return project.getId();
    }

    public void updateProject(ProjectDTO aProjectDTO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Project project = new Project().createProject(aProjectDTO);
        session.update(project);
        session.getTransaction().commit();
    }


    public Integer removeProject(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from createProject where id = " + id);
        int i = new Integer(query.executeUpdate());
        session.getTransaction().commit();
        return i;
    }

    // persons

    public List<PersonDTO> getPersonsByPprojectId(Long id) {
        ProjectDTO projectDTO = findProjectById(id);
        return new ArrayList<PersonDTO>(projectDTO.getPersons());
    }

    public PersonDTO login(String login, String password){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person> res = session.createQuery("from createPerson where login = '" + login+"' and password = '"+password+"'").list();
        PersonDTO personDTO = createPersonDTO(res.get(0));
        session.getTransaction().commit();
        return personDTO;
    }

    public PersonDTO findPersonById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Person> res = session.createQuery("from createPerson where id = " + id).list();
        PersonDTO personDTO = createPersonDTO(res.get(0));
        session.getTransaction().commit();
        return personDTO;
    }

  	public List<PersonDTO> getPeople() {
  		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
  		session.beginTransaction();
  		List<Person> people = session.createQuery("from createPerson").list();
        List<PersonDTO> personDTOs = new ArrayList<PersonDTO>(people != null ? people.size() : 0);
        if (people != null) {
            for (Person person : people) {
                personDTOs.add(createPersonDTO(person));
            }
        }
  		session.getTransaction().commit();
	  	return personDTOs;
  	}

	public Long savePerson(PersonDTO aPersonDTO) {
	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    session.beginTransaction();
        Person person = new Person();
        person.setAge(aPersonDTO.getAge());
        person.setFirstName(aPersonDTO.getFirstName());
        person.setLastName(aPersonDTO.getLastName());
        person.setLogin(aPersonDTO.getLogin());
        person.setPassword(aPersonDTO.getPassword());
        person.setRole(aPersonDTO.getRole());
	    session.save(person);
	    session.getTransaction().commit();
	    return person.getId();
	}

	public void updatePerson(PersonDTO aPersonDTO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Person person = new Person().createPerson(aPersonDTO);
		session.beginTransaction();
		session.update(person);
		session.getTransaction().commit();
	}


	public Integer removePerson(Long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("delete from createPerson where id = " + id);
        int i = new Integer(query.executeUpdate());
        session.getTransaction().commit();
		return i;
	}

    private PersonDTO createPersonDTO(Person person){
        Set<Project> projects = person.getProjects();
        Set<ProjectDTO> projectDTOs = new HashSet<ProjectDTO>(projects != null ? projects.size() : 0);
        if(projects != null) {
            for(Project project: projects) {
                projectDTOs.add(createProjectDTO(project));
            }
        }
        return new PersonDTO(person.getId(),person.getAge(), person.getFirstName(), person.getLastName(), person.getRole(), person.getLogin(), person.getPassword(), projectDTOs);
    }
    private ProjectDTO createProjectDTO(Project project){
        Set<Person> persons = project.getPersons();
        Set<PersonDTO> personDTOs = new HashSet<PersonDTO>(persons != null ? persons.size() : 0);
        if(persons != null) {
            for(Person person: persons) {
                personDTOs.add(createPersonDTO(person));
            }
        }
        return new ProjectDTO(project.getId(), project.getName(), personDTOs);
    }

}
