package com.example.dbmanager.client;

import java.io.Serializable;
import java.util.Set;

public class PersonDTO implements Serializable{

	private Long id;
	private int age;
	private String firstName;
	private String lastName;
    private int role;
    private String login;
    private String password;
    private Set<ProjectDTO> projects;
    
    public PersonDTO(Long id, int age, String firstName, String lastName, int role, String login, String password, Set<ProjectDTO> projects) {
        this.id = id;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.login = login;
        this.password = password;
        this.projects = projects;
    }

    public Set<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectDTO> projects) {
        this.projects = projects;
    }

    public enum Role {
        Admin,
        Developer,
        ProjectMennager;
    }

	public PersonDTO() {}
	
	public Long getId() {
		return this.id;
	}
	
	private void setId(Long aId) {
		this.id = aId;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getFirstName() {
		return this.firstName;
	}

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

	public void setFirstName(String aFirstName) {
		this.firstName = aFirstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String aLastName) {
		this.lastName = aLastName;
	}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
