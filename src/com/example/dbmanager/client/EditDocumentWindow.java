package com.example.dbmanager.client;

import com.example.dbmanager.domain.AppContext;
import com.example.dbmanager.domain.Document;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
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

public class EditDocumentWindow extends Window {

    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private final TextField idTF = new TextField();
    private final TextField nameTF = new TextField();
    private final TextField stateTF = new TextField();
    private final TextField projectIdTF = new TextField();
    private final TextField performerIdTF = new TextField();
    private final FormPanel formPanel = new FormPanel();
    private Long editPersonId  = new Long(0) ;
    private ModelData modelData;
    private AppContext context;

    public String getStateOfDoc() {
        return  stateTF.getValue().toString();
    }
    public String getName(){
        return nameTF.getValue().toString();
    }
    public Long getProjectId(){
        return new Long(Integer.decode(projectIdTF.getValue().toString()));
    }
    public Long getPerformerId(){
        return new Long(Integer.decode(performerIdTF.getValue().toString()));
    }

    public EditDocumentWindow(AppContext appContext){
        context = appContext;
        init();
        this.setHeading("Створення");
//        idTF.setEmptyText("Введіть ім*я");
        nameTF.setEmptyText("Введіть назву");
        stateTF.setValue("NEW");
        projectIdTF.setValue(context.getCurrentProject().getId());
        performerIdTF.setValue(context.getCurrentPerson().getId());
        this.add(formPanel);
    }

    public EditDocumentWindow(ModelData model) {
        editPersonId = (Long) model.get("id");
        modelData = model;
        init();
        this.setHeading("Редагування");
        nameTF.setValue(model.get("name").toString());
        stateTF.setValue(model.get("state").toString());
        projectIdTF.setValue((Long)model.get("projectId"));
        performerIdTF.setValue((Long)model.get("personId"));
        this.add(formPanel);
    }

    private void init(){
        this.setPlain(true);
        this.setSize(450, 350);
        this.setLayout(new FitLayout());

        formPanel.setHeading("Edit Person");
        formPanel.setWidth(350);
        nameTF.setAllowBlank(false);
        nameTF.setFieldLabel("Стан");

        stateTF.setAllowBlank(false);
        stateTF.setFieldLabel("Стан");


        projectIdTF.setAllowBlank(false);
        projectIdTF.setFieldLabel("Project id");

        performerIdTF.setAllowBlank(false);
        performerIdTF.setFieldLabel("performer id");

        formPanel.add(nameTF, new FormData("100%"));
        formPanel.add(stateTF, new FormData("100%"));
        formPanel.add(projectIdTF, new FormData("100%"));
        formPanel.add(performerIdTF, new FormData("100%"));

//
//        if (editPersonId != 0) {
//            RpcProxy<List<Project>> proxy = new RpcProxy<List<Project>>() {
//                @Override
//                protected void load(Object loadConfig, AsyncCallback<List<Project>> callback) {
//                    dbmanagerService.getProjectsByPersonId(editPersonId, callback);
//                }
//            };
//            BeanModelReader reader = new BeanModelReader();
//
//            ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
//            final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
//            loader.load();
//
//            ColumnModel cm = new ColumnModel(getColumnConfig());
//
//            ContentPanel panel = new ContentPanel();
//            panel.setHeading("testTitle");
//            panel.setFrame(true);
//            panel.setSize("350", "150");
//            panel.setLayout(new FitLayout());
//
//            Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
//
//            panel.add(grid);
//            formPanel.add(panel);
//        }

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
        final EditDocumentWindow thisWindow = this;
        column.setRenderer(new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                final Long id = model.get("id");
                Button button = new Button("Delete");
                button.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        dbmanagerService.removeProjectFromPersonByIds(editPersonId, id, new AsyncCallback<Integer>() {
                            @Override public void onFailure(Throwable caught) {}
                            @Override
                            public void onSuccess(Integer result) {
                                thisWindow.close();
                                thisWindow.reloadWindow();
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
        EditDocumentWindow tmp = new EditDocumentWindow(modelData);
        tmp.show();
    }
}