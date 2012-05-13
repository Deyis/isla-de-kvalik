package com.example.dbmanager.client;

import java.io.Serializable;
import java.util.Set;

public class ProjectDTO implements Serializable {
    private Long id;
    private String name;
    private Set<PersonDTO> persons;

    public ProjectDTO(Long id, String name, Set<PersonDTO> persons) {
        this.id = id;
        this.name = name;
        this.persons = persons;
    }

    public Set<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(Set<PersonDTO> persons) {
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
