package com.example.dbmanager.client;

import java.util.ArrayList;
import java.util.List;

import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DBManagerServiceAsync {
    // persons
    public void login(String login, String password, AsyncCallback<Person> callback);
    public void findPersonById(Long id, AsyncCallback<Person> callback);
	public void getPeople(AsyncCallback<List<Person>> callback);
	public void savePerson(Person personDTO, AsyncCallback<Long> callback);
	public void updatePerson(Person personDTO, AsyncCallback callback);
	public void removePerson(Long id, AsyncCallback<Integer> callback);
    void getPersonsByPprojectId(Long id, AsyncCallback<List<Person>> async);

    //String greetServer(String name) throws IllegalArgumentException;
    // projects
    void getProjects(AsyncCallback<List<Project>> async);
    public void findProjectById(Long id, AsyncCallback<Project> callback);
    public void saveProject(Project projectDTO, AsyncCallback<Long> callback);
    public void updateProject(Project projectDTO, AsyncCallback callback);
    public void removeProject(Long id, AsyncCallback<Integer> callback);
    void getProjectsByPersonId(Long id, AsyncCallback<List<Project>> async);
}
