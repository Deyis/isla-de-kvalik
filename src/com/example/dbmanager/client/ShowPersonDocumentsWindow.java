package com.example.dbmanager.client;

import com.example.dbmanager.domain.AppContext;
import com.example.dbmanager.domain.Document;
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

public class ShowPersonDocumentsWindow extends Window {
    private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerService.class);
    private ShowPersonDocumentsWindow documentWindow;
    private AppContext context;

    public ShowPersonDocumentsWindow (AppContext appContext) {
        context = appContext;
    }

    public void reloadDocuments() {
        documentWindow =  new ShowPersonDocumentsWindow(context);

        RpcProxy<List<Document>> proxy = new RpcProxy<List<Document>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<List<Document>> callback) {
                dbmanagerService.getDocumentsByOwnerId(context.getCurrentPerson().getId(), callback);
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
                            updateDocument.setName(editDocumentWindow.getName());
                            updateDocument.setState(editDocumentWindow.getStateOfDoc());
                            dbmanagerService.updateDocument(updateDocument, new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    //To change body of implemented methods use File | Settings | File Templates.
                                }

                                @Override
                                public void onSuccess(Void result) {
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

        Button newDocumentButton = new Button("Create New");
        newDocumentButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                final EditDocumentWindow editDocumentWindow = new EditDocumentWindow(context);
                Button saveButton = new Button("Save");
                saveButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {

                        final Document updateDocument = new Document();
                        updateDocument.setName(editDocumentWindow.getName());
                        updateDocument.setState(editDocumentWindow.getStateOfDoc());
                        updateDocument.setPersonId(editDocumentWindow.getPerformerId());
                        updateDocument.setProjectId(editDocumentWindow.getProjectId());
                        dbmanagerService.saveDocument(updateDocument, new AsyncCallback<Long>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            public void onSuccess(Long result) {
                                editDocumentWindow.close();
                                documentWindow.close();
                                reloadDocuments();
                            }
                        });

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
        documentWindow.setPlain(true);
        documentWindow.setSize(600, 400);
        documentWindow.setHeading("Document");
        documentWindow.setLayout(new FitLayout());
        documentWindow.add(panel);
        documentWindow.addButton(newDocumentButton);
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