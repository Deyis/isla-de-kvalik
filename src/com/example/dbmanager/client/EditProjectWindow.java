package com.example.dbmanager.client;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class EditProjectWindow extends Window {

    private TextField nameTF = new TextField();
    private FormPanel formPanel = new FormPanel();


    public String getName(){
        return nameTF.getValue().toString();
    }

    public EditProjectWindow(){
        init();
        this.setHeading("Створення");
        nameTF.setEmptyText("Введіть ім*я");
        this.add(formPanel);
    }

    public EditProjectWindow(String name) {
        init();
        this.setHeading("Редагування");
        nameTF.setValue(name);
        this.add(formPanel);
    }

    private void init(){
        this.setPlain(true);
        this.setSize(350, 250);
        this.setLayout(new FitLayout());

        formPanel.setHeading("Edit createPerson");
        formPanel.setWidth(350);
        nameTF.setAllowBlank(false);
        nameTF.setFieldLabel("Name");
        formPanel.add(nameTF, new FormData("100%"));
    }
}