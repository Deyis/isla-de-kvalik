package com.example.dbmanager.domain;

import java.io.Serializable;

public class Document implements Serializable {

//    public enum String {
//        NEW, ACCEPTED, TEST, FEEDBACK, FIXED
//    }

    private Long id;
    private String name;
    private String state;
    private Long projectId;
    private Long personId;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long performerId) {
        this.personId = performerId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
