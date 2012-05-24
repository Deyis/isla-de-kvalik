package com.example.dbmanager.client;

import java.util.ArrayList;
import java.util.List;

import com.example.dbmanager.domain.Document;
import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DBManagerServiceAsync {
    // persons
    void addPersonToProject(Long personId, Long projectId, AsyncCallback<Long> async);
    void removeProjectFromPersonByIds(Long personId, Long projectId, AsyncCallback<Integer> async);
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

    //document
    void getDocuments(AsyncCallback<List<Document>> async);
    void findDocumentById(Long id, AsyncCallback<Document> async);
    void saveDocument(Document aDocument, AsyncCallback<Long> async);
    void updateDocument(Document aDocument, AsyncCallback<Void> async);
    void removeDocument(Long id, AsyncCallback<Integer> async);
    void getDocumentsByProjectId(Long id, AsyncCallback<List<Document>> async);
    void getDocumentsByPersonIdAndProjectId(Long personId, Long projectId, AsyncCallback<List<Document>> async);
    void getDocumentsByOwnerId(Long ownerId, AsyncCallback<List<Document>> async);
}
