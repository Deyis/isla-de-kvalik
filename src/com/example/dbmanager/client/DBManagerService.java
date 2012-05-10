package com.example.dbmanager.client;

import java.util.ArrayList;
import java.util.List;

import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("dbmanager")
public interface DBManagerService extends RemoteService {

    //persons
    public Person findPersonById(Long id);
	public List<Person> getPeople();
	public Long savePerson(Person aPerson);
	public void updatePerson(Person aPerson);
	public Integer removePerson(Long id);
    public Person login(String login, String password);
	//String greetServer(String name) throws IllegalArgumentException;
    // projects
    public List<Project> getProjects();
    public Project findProjectById(Long id);
    public Long saveProject(Project aProject);
    public void updateProject(Project aProject);
    public Integer removeProject(Long id);

}
