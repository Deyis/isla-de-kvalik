package com.example.dbmanager.client;

import com.example.dbmanager.domain.AppContext;
import com.example.dbmanager.domain.Document;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
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

public class DocumentWindow extends  Window{
    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private DocumentWindow documentWindow;
    private AppContext context;

    public DocumentWindow (AppContext appContext) {
        context = appContext;
    }

    public void reloadDocuments() {
        documentWindow =  new DocumentWindow(context);

        RpcProxy<List<Document>> proxy = new RpcProxy<List<Document>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<Document>> callback) {
                dbmanagerService.getDocumentsByPersonIdAndProjectId(context.getCurrentPerson().getId(), context.getCurrentProject().getId(), callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();

        ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
        final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
        loader.load();

        ColumnModel cm = new ColumnModel(getColumnConfig());

        ContentPanel panel = new ContentPanel();
        panel.setHeading("Document List");
        panel.setFrame(true);
        panel.setSize("500", "300");
        panel.setLayout(new FitLayout());

        Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        //grid.setAutoExpandColumn("Name");
        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ModelData>>() {
            @Override
            public void handleEvent(GridEvent<ModelData> be) {
                final EditDocumentWindow editDocumentWindow = new EditDocumentWindow(be.getModel());
                //editProjectWindow.setHeading(be.getModel().get("id").toString());
                Long id = (Long) be.getModel().get("id");
                final List<Document> docList = new ArrayList<Document>();
                dbmanagerService.findDocumentById(id, new AsyncCallback<Document>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Document result) {
                        docList.add(result);
                    }
                });
                Button saveButton = new Button("Save");
                saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        if (docList.size() > 0) {
                            final Document updateDocument = docList.get(0);
                            updateDocument.setName(editDocumentWindow.getFirstName());
                            dbmanagerService.updateDocument(updateDocument, new AsyncCallback() {
                                @Override
                                public void onFailure(Throwable caught) {
                                }

                                @Override
                                public void onSuccess(Object result) {
                                    editDocumentWindow.close();
                                    documentWindow.close();
                                    reloadDocuments();
                                }
                            });
                        }
                    }
                });
                Button cancelButton = new Button("Cancel");
                cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        editDocumentWindow.close();
                    }
                });
                editDocumentWindow.addButton(saveButton);
                editDocumentWindow.addButton(cancelButton);
                editDocumentWindow.show();
            }
        });
        panel.add(grid);

        documentWindow.setPlain(true);
        documentWindow.setSize(600, 400);
        documentWindow.setHeading("Document");
        documentWindow.setLayout(new FitLayout());
        documentWindow.add(panel);
        documentWindow.show();
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
                                documentWindow.close();
                                documentWindow.reloadDocuments();
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