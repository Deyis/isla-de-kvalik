package com.example.dbmanager.client;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DBManagerLogin implements EntryPoint {
    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private PersonWindow personWindow = new PersonWindow();

    public void onModuleLoad() {
        final Window loginWindow = new Window();
        loginWindow.setPlain(true);
        loginWindow.setSize(300, 200);
        loginWindow.setLayout(new FitLayout());

        loginWindow.setHeading("Login");
        final TextField loginTF = new TextField();
        final TextField passwordTF = new TextField();
        final FormPanel formPanel = new FormPanel();
        loginTF.setEmptyText("Login");
        passwordTF.setEmptyText("Password");

        formPanel.setHeading("Edit createPerson");
        formPanel.setWidth(350);
        loginTF.setAllowBlank(false);
        loginTF.setFieldLabel("Login");
        passwordTF.setAllowBlank(false);
        passwordTF.setFieldLabel("Password");

        formPanel.add(loginTF, new FormData("100%"));
        formPanel.add(passwordTF, new FormData("100%"));

        loginWindow.add(formPanel);
        Button loginButton = new Button("Login");
        loginButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                dbmanagerService.login(loginTF.getValue().toString(),passwordTF.getValue().toString(), new AsyncCallback<PersonDTO>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        loginWindow.setHeading("Fail");
                    }

                    @Override
                    public void onSuccess(PersonDTO result) {
                        loginWindow.setHeading("Success " + result.getLogin());
                        if (result.getRole() == 1) {

                        }
                    }
                });
            }
        });
        loginWindow.addButton(loginButton);
        loginWindow.show();

    }

    private void reloadPersons(){
        personWindow =  new PersonWindow();
        personWindow.reloadPersons();
        personWindow.close();
    }

   /* private void createPerson() {
        final EditPersonWindow editPersonWindow = new EditPersonWindow();
        Button saveButton = new Button("Save");
        saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                final createPerson newPerson = new createPerson();
                newPerson.setFirstName(editPersonWindow.getFirstName());
                newPerson.setLastName(editPersonWindow.getLastName());
                newPerson.setAge(editPersonWindow.getAge());
                dbmanagerService.savePerson(newPerson, new AsyncCallback<Long>() {
                    @Override
                    public void onFailure(Throwable caught) {}
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
    }*/
}