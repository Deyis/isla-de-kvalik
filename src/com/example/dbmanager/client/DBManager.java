package com.example.dbmanager.client;

import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.List;

public class DBManager implements EntryPoint {
    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private PersonWindow personWindow = new PersonWindow();
    private ProjectWindow projectWindow = new ProjectWindow();
    private Person currentUser;

    public void onModuleLoad() {
        Menu menuPersons = new Menu();
        MenuItem item1 = new MenuItem("Show Persons");
        item1.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                reloadPersons();
            }
        });
        MenuItem item2 = new MenuItem("Create Person");
        item2.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                createPerson();
            }
        });
        menuPersons.add(item1);
        menuPersons.add(item2);

        Menu menuProjects = new Menu();
        MenuItem item11 = new MenuItem("Show Projects");
        item11.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                reloadProjects();
            }
        });
        MenuItem item21 = new MenuItem("Create Project");
        item21.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                createProject();
            }
        });
        menuProjects.add(item11);
        menuProjects.add(item21);

        MenuBar bar = new MenuBar();
        bar.add(new MenuBarItem("Persons", menuPersons));
        bar.add(new MenuBarItem("Projects", menuProjects));
        RootPanel.get().add(bar);
    }

    private void reloadProjects(){
        projectWindow =  new ProjectWindow();
        projectWindow.reloadProjects();
        projectWindow.close();
    }

    private void createProject() {
        final EditProjectWindow editProjectWindow = new EditProjectWindow();
        Button saveButton = new Button("Save");
        saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                final Project newProject = new Project();
                newProject.setName(editProjectWindow.getName());
                dbmanagerService.saveProject(newProject, new AsyncCallback<Long>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //editPersonWindow.setHeading("Fail");
                    }
                    @Override
                    public void onSuccess(Long result) {
                        editProjectWindow.close();
                        reloadProjects();
                    }
                });
            }
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                editProjectWindow.close();
            }
        });
        editProjectWindow.addButton(saveButton);
        editProjectWindow.addButton(cancelButton);
        editProjectWindow.show();
    }

    private void reloadPersons(){
        personWindow =  new PersonWindow();
        personWindow.reloadPersons();
        personWindow.close();
    }

    private void createPerson() {
        final EditPersonWindow editPersonWindow = new EditPersonWindow();
        Button saveButton = new Button("Save");
        saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                final Person newPerson = new Person();
                newPerson.setFirstName(editPersonWindow.getFirstName());
                newPerson.setLastName(editPersonWindow.getLastName());
                newPerson.setAge(editPersonWindow.getAge());
                dbmanagerService.savePerson(newPerson, new AsyncCallback<Long>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //editPersonWindow.setHeading("Fail");
                    }
                    @Override
                    public void onSuccess(Long result) {
                        editPersonWindow.close();
                        reloadPersons();
                    }
                });
            }
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                editPersonWindow.close();
            }
        });
        editPersonWindow.addButton(saveButton);
        editPersonWindow.addButton(cancelButton);
        editPersonWindow.show();
    }
}