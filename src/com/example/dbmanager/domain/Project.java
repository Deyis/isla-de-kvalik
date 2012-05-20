package com.example.dbmanager.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Project implements Serializable {
    private Long id;
    private String name;
    private Long managerId;
//    private Set<Person> persons = new HashSet<Person>(0);
//
//    public Set<Person> getPersons() {
//        return persons;
//    }
//
//    public void setPersons(Set<Person> persons) {
//        this.persons = persons;
//    }
    public Long getManagerId() {
    return managerId;
}

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
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
