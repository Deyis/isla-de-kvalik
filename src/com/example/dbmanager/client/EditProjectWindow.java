package com.example.dbmanager.client;

import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class EditProjectWindow extends Window {

    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private TextField nameTF = new TextField();
    private final FormPanel formPanel = new FormPanel();
    private Long editProjectId = new Long(0);
    private ModelData modelData;


    public String getName(){
        return nameTF.getValue().toString();
    }

    public EditProjectWindow(){
        init();
        this.setHeading("Створення");
        nameTF.setEmptyText("Введіть ім*я");
        this.add(formPanel);
    }

    public EditProjectWindow(ModelData model) {
        editProjectId = (Long) model.get("id");
        modelData = model;
        init();
        this.setHeading("Редагування");
        nameTF.setValue(model.get("name").toString());
        this.add(formPanel);
    }

    private void init(){
        this.setPlain(true);
        this.setSize(600, 300);
        this.setLayout(new FitLayout());

        formPanel.setHeading("Edit Person");
        formPanel.setWidth(600);
        nameTF.setAllowBlank(false);
        nameTF.setFieldLabel("Name");
        formPanel.add(nameTF, new FormData("100%"));


        if (editProjectId != 0) {
            RpcProxy<List<Person>> proxy = new RpcProxy<List<Person>>() {
                @Override
                protected void load(Object loadConfig, AsyncCallback<List<Person>> callback) {
                    dbmanagerService.getPersonsByPprojectId(editProjectId, callback);
                }
            };
            BeanModelReader reader = new BeanModelReader();

            ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
            final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
            loader.load();

            ColumnModel cm = new ColumnModel(getColumnConfig());

            ContentPanel panel = new ContentPanel();
            panel.setHeading("testTitle");
            panel.setFrame(true);
            panel.setSize("600", "250");
            panel.setLayout(new FitLayout());

            Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);

            panel.add(grid);
            formPanel.add(panel);
        }
    }

    private List<ColumnConfig> getColumnConfig () {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("id", "ID", 50);
        columns.add(column);

        column = new ColumnConfig("age", "Age", 50);
        NumberField field = new NumberField();
        field.setPropertyEditorType(Integer.class);
        column.setEditor(new CellEditor(field));
        columns.add(column);

        column = new ColumnConfig("firstName", "First name", 200);
        TextField<String> text = new TextField<String>();
        // text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        columns.add(column);

        column = new ColumnConfig("lastName", "Last name", 200);
        text = new TextField<String>();
        column.setEditor(new CellEditor(text));
        columns.add(column);

        final EditProjectWindow thisWindow = this;
        column = new ColumnConfig("delete", "Delete", 70);
        column.setRenderer(new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                final Long id = model.get("id");
                Button button = new Button("Delete");
                button.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        dbmanagerService.removeProjectFromPersonByIds(id ,editProjectId, new AsyncCallback<Integer>() {
                            @Override public void onFailure(Throwable caught) {}
                            @Override
                            public void onSuccess(Integer result) {
                                thisWindow.close();
                                thisWindow.reloadWindow();
//                                personWindow.close();
//                                personWindow.reloadPersons();
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

    private void reloadWindow() {
        EditProjectWindow tmp = new EditProjectWindow(modelData);
        tmp.show();
    }
}