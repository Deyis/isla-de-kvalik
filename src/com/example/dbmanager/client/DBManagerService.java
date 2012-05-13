package com.example.dbmanager.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("dbmanager")
public interface DBManagerService extends RemoteService {

    //persons
    public PersonDTO findPersonById(Long id);
	public List<PersonDTO> getPeople();
	public Long savePerson(PersonDTO aPersonDTO);
	public void updatePerson(PersonDTO aPersonDTO);
	public Integer removePerson(Long id);
    public PersonDTO login(String login, String password);
    public List<PersonDTO> getPersonsByPprojectId(Long id);
	//String greetServer(String name) throws IllegalArgumentException;
    // projects
    public List<ProjectDTO> getProjects();
    public ProjectDTO findProjectById(Long id);
    public Long saveProject(ProjectDTO aProjectDTO);
    public void updateProject(ProjectDTO aProjectDTO);
    public Integer removeProject(Long id);
    public List<ProjectDTO> getProjectsByPersonId(Long id);

}
