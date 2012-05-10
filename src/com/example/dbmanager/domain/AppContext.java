package com.example.dbmanager.domain;

public class AppContext {
    Person currentPerson;

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }
}
