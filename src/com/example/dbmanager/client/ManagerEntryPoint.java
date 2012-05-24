package com.example.dbmanager.client;

import com.example.dbmanager.domain.AppContext;
import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class ManagerEntryPoint implements EntryPoint {
    private DBManagerServiceAsync dbmanagerService;
    private ManagerProjectWindow managerProjectWindow = new ManagerProjectWindow();
    private AppContext context;

    public void init(DBManagerServiceAsync dbManagerService, AppContext appContext) {
        this.dbmanagerService = dbManagerService;
        context = appContext;
    }

    public void onModuleLoad() {

        final List<Project> projectList = new ArrayList<Project>();
        dbmanagerService.findProjectById(new Long(1), new AsyncCallback<Project>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Project result) {
                projectList.add(result);
            }
        });
//        context.setCurrentProject(projectList.get(0));
        Long id = new Long(4);
        final List<Person> personList = new ArrayList<Person>();
        dbmanagerService.findPersonById(id, new AsyncCallback<Person>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Person result) {
                personList.add(result);
                managerProjectWindow =  new ManagerProjectWindow();
                managerProjectWindow.reloadProjects(context);
                managerProjectWindow.close();
            }
        });
    }
}