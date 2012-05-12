package com.example.dbmanager.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person implements Serializable{

	private Long id;
	private int age;
	private String firstName;
	private String lastName;
    private int role;
    private String login;
    private String password;
    private Set<Project> projects = new HashSet<Project>(0);

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public enum Role {
        Admin,
        Developer,
        ProjectMennager;
    }
	
	public Person() {}
	
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
