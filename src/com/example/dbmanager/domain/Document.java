package com.example.dbmanager.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Document implements Serializable {

    public enum State {
        NEW, ACCEPTED, TEST, FEEDBACK, FIXED
    }

    private Long id;
    private String name;
    private State state;
    private Long projectId;
    private Long performerId;


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
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
