package com.example.dbmanager.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.example.dbmanager.domain.Person;
import com.extjs.gxt.ui.client.widget.MessageBox;


public class DBManagerGWT implements EntryPoint {
	private final DBManagerServiceAsync dbmanagerService = GWT.create(DBManagerServiceAsync.class);
	private HorizontalPanel addPanel = new HorizontalPanel();
	private VerticalPanel mainPanel = new VerticalPanel(); 
	private CellTable<Person> table = new CellTable<Person>();
	private TextBox ageTextBox = new WatermarkedTextBox("", "age");
	private TextBox firstNameTextBox = new WatermarkedTextBox("", "First name");
	private TextBox lastNameTextBox = new WatermarkedTextBox("", "Last name");
	private TextBox idTextBox = new TextBox();
	private Button addButton = new Button("Add");
	private Button getButton = new Button("Get");
	private Button removeButton = new Button("Remove");
	private Button removeSelButton = new Button("Remove selected");
	private final MultiSelectionModel<Person> selectionModel = new MultiSelectionModel<Person>();
	
	private void constructTable() {
	
		table.setSelectionModel(selectionModel, DefaultSelectionEventManager.<Person> createCheckboxManager());
		
		Column<Person, Boolean> checkColumn = new Column<Person, Boolean>(
			    new CheckboxCell(true, false)) {
			  @Override
			  public Boolean getValue(Person object) {
			    return selectionModel.isSelected(object);
			  }
			};
		
		TextColumn<Person> idColumn = new TextColumn<Person>() {
			public String getValue(Person object) {
				return String.valueOf(object.getId());
			}
		};
		
		Column<Person, String> ageColumn = new Column<Person, String>(new EditTextCell()) {
			public String getValue(Person object) {
				return String.valueOf(object.getAge());
			}
		};
		
		ageColumn.setFieldUpdater(new FieldUpdater<Person, String>() {

			public void update(int index, Person object, String value) {
				object.setAge(Integer.parseInt(value));
				dbmanagerService.updatePerson(object, new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("WTF");
					}

					@Override
					public void onSuccess(Integer result) {
					//	Window.alert("Edited");
						refreshTable();
					}
				});
				
				
			}
		});
		
		Column<Person, String> firstNameColumn = new Column<Person, String>(new EditTextCell()) {
			public String getValue(Person object) {
				return object.getFirstName();
			}
		};
		
		firstNameColumn.setFieldUpdater(new FieldUpdater<Person, String>() {
			@Override
			public void update(int index, Person object, String value) {
				object.setFirstName(value);
				dbmanagerService.updatePerson(object, new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("WTF");
					}

					@Override
					public void onSuccess(Integer result) {
						refreshTable();
					}
				});
			}
		});
		
		Column<Person, String> lastNameColumn = new Column<Person, String>(new EditTextCell()) {
			public String getValue(Person object) {
				return object.getLastName();
			}
		};
		
		lastNameColumn.setFieldUpdater(new FieldUpdater<Person, String>() {
			@Override
			public void update(int index, Person object, String value) {
				object.setLastName(value);
				dbmanagerService.updatePerson(object, new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("WTF");
					}

					@Override
					public void onSuccess(Integer result) {
						refreshTable();
					}
				});
			}
		});
		
		

		table.addColumn(checkColumn);
		table.addColumn(idColumn, "id");
		table.addColumn(ageColumn, "age");
		table.addColumn(firstNameColumn, "firstName");
		table.addColumn(lastNameColumn, "lastName");
		
		table.setWidth("100%", true);
		
	}
	
	private void initControls() {
		removeSelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
		            Set<Person> selected = selectionModel.getSelectedSet();
		            for (Person p : selected) {

						dbmanagerService.removePerson(p.getId(), new AsyncCallback<Integer>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("WTF");
							}

							@Override
							public void onSuccess(Integer result) {
								Window.alert("Removed");
								refreshTable();
							}
						});
		            }
	
				}
			});
			
		    

		getButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refreshTable();

			}
		});
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
		        Person person = new Person();
		        person.setAge(Integer.valueOf(ageTextBox.getText()));
		        person.setFirstName(firstNameTextBox.getText());
		        person.setLastName(lastNameTextBox.getText());
		        dbmanagerService.savePerson(person, new AsyncCallback<Long>() {
			          @Override
			          public void onFailure(Throwable caught) {
			            Window.alert("Failed to save record.");
			            System.out.println(caught.getMessage());
			          }

			          @Override
			          public void onSuccess(Long result) {
			        	//addButton.setText("Saved");
			          //  Window.alert("Record saved");
			        	
						refreshTable();
						ageTextBox.setText("");
						firstNameTextBox.setText("");
						lastNameTextBox.setText("");
			        	//addButton.setText("Add");
			          }
				});
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Long id = Long.valueOf(idTextBox.getText());
				dbmanagerService.removePerson(id, new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("WTF");
					}

					@Override
					public void onSuccess(Integer result) {
						refreshTable();
					}
				});	
			}
		});
	
	}
	
	private void addComponents() {
		addPanel.add(ageTextBox);
		addPanel.add(firstNameTextBox);
		addPanel.add(lastNameTextBox);
		addPanel.add(addButton);
		
		mainPanel.add(table);
		mainPanel.add(removeSelButton);
		mainPanel.add(addPanel);
		RootPanel.get().add(mainPanel);
	}
	
	public void onModuleLoad() {
		constructTable();
		initControls();
		addComponents();
		refreshTable();
	}
	
	private void refreshTable() {
		dbmanagerService.getPeople(new AsyncCallback<List<Person>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("WTF");
			}
			@Override
			public void onSuccess(List<Person> result) {
				if (result == null) {Window.alert("No people"); return; };
				ListDataProvider<Person> dataProvider = new ListDataProvider<Person>();
				dataProvider.addDataDisplay(table);
				List<Person> list = dataProvider.getList();
				list.addAll(result);
			}
		});
	}
	
}

