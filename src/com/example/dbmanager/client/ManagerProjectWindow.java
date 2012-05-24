package com.example.dbmanager.client;

import com.example.dbmanager.domain.AppContext;
import com.example.dbmanager.domain.Person;
import com.example.dbmanager.domain.Project;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class ManagerProjectWindow extends  Window{
    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private ManagerProjectWindow projectWindow;
    private AppContext context;
    private GroupingView view = null;
    private Grid<BeanModel> grid;

    public void reloadProjects(AppContext appContext){
        context = appContext;
        projectWindow =  new ManagerProjectWindow();

        RpcProxy<List<Project>> proxy = new RpcProxy<List<Project>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<Project>> callback) {
                dbmanagerService.getProjectsByPersonId(context.getCurrentPerson().getId(), callback);
            }
        };
        RpcProxy<List<Person>> peopleProxy = new RpcProxy<List<Person>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<Person>> callback) {
                dbmanagerService.getPersonsByPprojectId(context.getCurrentProject().getId(), callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();

        ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
        final GroupingStore<BeanModel> store = new GroupingStore<BeanModel>(loader);
        loader.load();
        view = new GroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        List<ColumnConfig> columnConfigs = getColumnConfig();
        final ColumnModel cm = new ColumnModel(columnConfigs);
        grid = new Grid<BeanModel>(store, cm);
        final ColumnModel columnModel = new ColumnModel(columnConfigs);
        view.setGroupRenderer(new GridGroupRenderer() {
            public String render(GroupColumnData data) {
                String f = cm.getColumnById(data.field).getHeader();
                String l = data.models.size() == 1 ? "Item" : "Items";
                return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        });
        grid.setView(view);

        ContentPanel panel = new ContentPanel();
        panel.setHeading("testTitle");
        panel.setFrame(true);
        panel.setSize("500", "300");
        panel.setLayout(new FitLayout());




        //grid.setAutoExpandColumn("Name");
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ModelData>>() {
            @Override
            public void handleEvent(GridEvent<ModelData> be) {
//                final EditProjectWindow editProjectWindow = new EditProjectWindow(be.getModel());
                Long id = (Long) be.getModel().get("id");
                final String name = (String) be.getModel().get("name");
                final List<Project> pList = new ArrayList<Project>();
                dbmanagerService.findProjectById(id, new AsyncCallback<Project>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Project result) {
                        pList.add(result);
                        context.setCurrentProject(pList.get(0));
                        PersonWindow personWindow = new PersonWindow();

                        personWindow.reloadPersons();
                        personWindow.setHeading("People assigned for project " + name);
                        personWindow.close();
                    }
                });

//                Button saveButton = new Button("Save");
//                saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
//                    @Override
//                    public void handleEvent(BaseEvent be) {
//                        if (pList.size() > 0) {
//                            final Project updateProject = pList.get(0);
//                            updateProject.setName(editProjectWindow.getName());
//                            dbmanagerService.updateProject(updateProject, new AsyncCallback() {
//                                @Override
//                                public void onFailure(Throwable caught) {
//                                }
//
//                                @Override
//                                public void onSuccess(Object result) {
//                                    editProjectWindow.close();
//                                    projectWindow.close();
//                                    reloadProjects(context);
//                                }
//                            });
//                        }
//                    }
//                });
//                Button cancelButton = new Button("Cancel");
//                cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
//                    @Override
//                    public void handleEvent(BaseEvent be) {
//                        editProjectWindow.close();
//                    }
//                });
//                editProjectWindow.addButton(saveButton);
//                editProjectWindow.addButton(cancelButton);
//                editProjectWindow.show();
            }
        });
        panel.add(grid);




        projectWindow.setPlain(true);
        projectWindow.setSize(600, 400);
        projectWindow.setHeading("Project");
        projectWindow.setLayout(new FitLayout());
        projectWindow.add(panel);
        projectWindow.show();
    }

    private List<ColumnConfig> getColumnConfig () {
        final List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("id", "ID", 50);
        columns.add(column);


        final ColumnConfig name = new ColumnConfig("name", "Name", 200);
        TextField<String> text = new TextField<String>();
        // text.setAllowBlank(false);
        name.setEditor(new CellEditor(text));
        columns.add(name);


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
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(Integer result) {
                                projectWindow.close();
                                projectWindow.reloadProjects(context);
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