package com.example.dbmanager.client;

import com.example.dbmanager.domain.AppContext;
import com.example.dbmanager.domain.Document;
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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class SelectPersonToAddDocumentWindow extends  Window{
    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private SelectPersonToAddDocumentWindow personsWindow;
    private AppContext context;

    public void reloadPersons( AppContext appContext){
        context = appContext;
        personsWindow =  new SelectPersonToAddDocumentWindow();

        RpcProxy<List<Person>> proxy = new RpcProxy<List<Person>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<Person>> callback) {
                dbmanagerService.getPersonsByPprojectId(context.getCurrentProject().getId(),callback);
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
        panel.setSize("500", "300");
        panel.setLayout(new FitLayout());

        Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        //grid.setAutoExpandColumn("Name");
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ModelData>>() {
            @Override
            public void handleEvent(GridEvent<ModelData> be) {
//                final EditProjectWindow editProjectWindow = new EditProjectWindow(be.getModel());
                dbmanagerService.getDocumentsByPersonIdAndProjectId((Long) be.getModel().get("id"), context.getCurrentProject().getId(), new AsyncCallback<List<Document>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onSuccess(List<Document> result) {
                        ShowPersonDocumentsWindow nw = new ShowPersonDocumentsWindow(context);
                        nw.reloadDocuments();
                        nw.close();

                    }
                });
            }
        });
        panel.add(grid);

        personsWindow.setPlain(true);
        personsWindow.setSize(600, 400);
        personsWindow.setHeading("Project");
        personsWindow.setLayout(new FitLayout());
        personsWindow.add(panel);
        personsWindow.show();
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

        column = new ColumnConfig("delete", "Delete", 70);
        column.setRenderer(new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                final Long id = model.get("id");
                Button button = new Button("Delete");
                button.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        dbmanagerService.removePerson(id, new AsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(Integer result) {
                                personsWindow.close();
                                personsWindow.reloadPersons(context);
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