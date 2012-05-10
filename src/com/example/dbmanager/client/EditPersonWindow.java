package com.example.dbmanager.client;

import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class EditPersonWindow extends Window {

    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private final TextField firstNameTF = new TextField();
    private final TextField lastNameTF = new TextField();
    private final TextField ageTF = new TextField();
    private final TextField roleTF = new TextField();
    private final FormPanel formPanel = new FormPanel();
    final List<Person> editPerson = new ArrayList<Person>();

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
    
    public EditPersonWindow(ModelData model) {
        Long id =(Long) model.get("id");

        dbmanagerService.findPersonById(id, new AsyncCallback<Person>() {
            @Override public void onFailure(Throwable caught) {}
            @Override
            public void onSuccess(Person result) {
                editPerson.add(result);
            }
        });
        init();
        this.setHeading("Редагування");
        firstNameTF.setValue(model.get("firstName").toString());
        lastNameTF.setValue(model.get("lastName").toString());
        ageTF.setValue((Integer)model.get("age"));
        roleTF.setValue((Integer)model.get("role"));
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

        if (editPerson != null) {
            RpcProxy<List<Project>> proxy = new RpcProxy<List<Project>>() {
                @Override
                protected void load(Object loadConfig, AsyncCallback<List<Project>> callback) {
                    dbmanagerService.getProjectsByPersonId(editPerson.get(0).getId(),callback);
                }
            };
            BeanModelReader reader = new BeanModelReader();

            ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
            final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
            loader.load();

            ColumnModel cm = new ColumnModel(getColumnConfig());

            Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
            //grid.setAutoExpandColumn("Name");
            grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ModelData>>() {
                @Override
                public void handleEvent(GridEvent<ModelData> be) {
                    final EditProjectWindow editProjectWindow = new EditProjectWindow(be.getModel().get("name").toString());
                    //editProjectWindow.setHeading(be.getModel().get("id").toString());
                    Long id = (Long) be.getModel().get("id");
                    final List<Project> pList = new ArrayList<Project>();
                    dbmanagerService.findProjectById(id, new AsyncCallback<Project>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(Project result) {
                            pList.add(result);
                        }
                    });
                    Button saveButton = new Button("Save");
                    saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
                        @Override
                        public void handleEvent(BaseEvent be) {
                            if (pList.size() > 0) {
                                final Project updateProject = pList.get(0);
                                updateProject.setName(editProjectWindow.getName());
                                dbmanagerService.updateProject(updateProject, new AsyncCallback() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(Object result) {
                                        editProjectWindow.setHeading("yahoo");
                                        //editProjectWindow.close();
                                    }
                                });
                            }
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
            });
            formPanel.add(grid);
        }

    }

    private List<ColumnConfig> getColumnConfig () {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("id", "ID", 50);
        columns.add(column);

        column = new ColumnConfig("name", "Name", 200);
        TextField<String> text = new TextField<String>();
        // text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        columns.add(column);

        column = new ColumnConfig("delete", "Delete", 70);
        column.setRenderer(new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                final Long id = model.get("id");
                Button button = new Button("Delete");
                button.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        dbmanagerService.removeProject(id, new AsyncCallback<Integer>() {
                            @Override public void onFailure(Throwable caught) {}
                            @Override
                            public void onSuccess(Integer result) {
//                                projectWindow.close();
//                                projectWindow.reloadProjects();
                            }
                        });
                    }
                });
                return button;
            }
        });
        columns.add(column);
        return columns;
    }
}