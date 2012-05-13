package com.example.dbmanager.client;

import java.util.List;

import com.example.dbmanager.domain.Person;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DBManagerServiceAsync {
    // persons
    public void login(String login, String password, AsyncCallback<PersonDTO> callback);
    public void findPersonById(Long id, AsyncCallback<PersonDTO> callback);
	public void getPeople(AsyncCallback<List<PersonDTO>> callback);
	public void savePerson(PersonDTO personDTO, AsyncCallback<Long> callback);
	public void updatePerson(PersonDTO personDTO, AsyncCallback callback);
	public void removePerson(Long id, AsyncCallback<Integer> callback);
    void getPersonsByPprojectId(Long id, AsyncCallback<List<PersonDTO>> async);

    //String greetServer(String name) throws IllegalArgumentException;
    // projects
    void getProjects(AsyncCallback<List<ProjectDTO>> async);
    public void findProjectById(Long id, AsyncCallback<ProjectDTO> callback);
    public void saveProject(ProjectDTO projectDTO, AsyncCallback<Long> callback);
    public void updateProject(ProjectDTO projectDTO, AsyncCallback callback);
    public void removeProject(Long id, AsyncCallback<Integer> callback);
    void getProjectsByPersonId(Long id, AsyncCallback<List<ProjectDTO>> async);
}
