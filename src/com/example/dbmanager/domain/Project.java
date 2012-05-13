package com.example.dbmanager.domain;

import com.example.dbmanager.client.PersonDTO;
import com.example.dbmanager.client.ProjectDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Project implements Serializable {
    private Long id;
    private String name;
    private Set<Person> persons = new HashSet<Person>(0);

    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        Set<PersonDTO> personDTOs = projectDTO.getPersons();
        if(personDTOs != null) {
            Set<Person> persons = new HashSet<Person>(personDTOs.size());
            for(PersonDTO personDTO: personDTOs) {
                persons.add(new Person().createPerson(personDTO));
            }
            project.setPersons(persons);
        }
        return project;
    }

    public Project(){}

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Long getId() {
        return this.id;
    }

    private void setId(Long aId) {
        this.id = aId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String aName) {
        this.name = aName;
    }
}
