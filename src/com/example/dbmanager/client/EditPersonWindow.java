package com.example.dbmanager.client;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class EditPersonWindow extends Window {

    private final TextField firstNameTF = new TextField();
    private final TextField lastNameTF = new TextField();
    private final TextField ageTF = new TextField();
    private final TextField roleTF = new TextField();
    private final FormPanel formPanel = new FormPanel();

    public int getRoleTF() {
        return Integer.decode(roleTF.getValue().toString());
    }
    public int getAge() {
        return Integer.decode(ageTF.getValue().toString());
    }
    public String getFirstName(){
        return firstNameTF.getValue().toString();
    }
    public String getLastName(){
        return lastNameTF.getValue().toString();
    }

    public  EditPersonWindow(){
        init();
        this.setHeading("Створення");
        firstNameTF.setEmptyText("Введіть ім*я");
        lastNameTF.setEmptyText("Введіть прізвище");
        ageTF.setEmptyText("Введіть вік");
        roleTF.setEmptyText("Введіть роль");
        this.add(formPanel);
    }
    
    public EditPersonWindow(String firstName, String lastName, int age, int role) {
        init();
        this.setHeading("Редагування");
        firstNameTF.setValue(firstName);
        lastNameTF.setValue(lastName);
        ageTF.setValue(age);
        roleTF.setValue(role);
        this.add(formPanel);
    }

    private void init(){
        this.setPlain(true);
        this.setSize(350, 250);
        this.setLayout(new FitLayout());

        formPanel.setHeading("Edit Person");
        formPanel.setWidth(350);
        firstNameTF.setAllowBlank(false);
        firstNameTF.setFieldLabel("First Name");
        lastNameTF.setAllowBlank(false);
        lastNameTF.setFieldLabel("Last Name");
        ageTF.setAllowBlank(false);
        ageTF.setFieldLabel("Age");
        roleTF.setAllowBlank(false);
        roleTF.setFieldLabel("Role");

        formPanel.add(firstNameTF, new FormData("100%"));
        formPanel.add(lastNameTF, new FormData("100%"));
        formPanel.add(ageTF, new FormData("100%"));
        formPanel.add(roleTF, new FormData("100%"));
    }
}