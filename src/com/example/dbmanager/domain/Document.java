package com.example.dbmanager.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Document implements Serializable {
    private Long id;
    private String name;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
