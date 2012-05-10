package com.example.dbmanager.domain;

import java.io.Serializable;

public class Project implements Serializable {
    private Long id;
    private String name;


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
