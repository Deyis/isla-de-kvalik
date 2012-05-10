package com.example.dbmanager.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable {
    private Long id;
    private String name;
    private List<Person> persons = new ArrayList<Person>();

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
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
