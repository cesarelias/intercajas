/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package py.edu.uca.intercajas.client.beneficiario;

import static py.edu.uca.intercajas.dynatablerf.shared.DynaTableRequestFactory.SchoolCalendarRequest.ALL_DAYS;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.WriteOperation;
import com.sun.java.swing.plaf.windows.resources.windows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.DireccionProxy;
import py.edu.uca.intercajas.client.requestfactory.DocumentoIdentidadProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.dynatablerf.client.events.EditPersonEvent;
import py.edu.uca.intercajas.dynatablerf.client.events.FilterChangeEvent;
import py.edu.uca.intercajas.dynatablerf.shared.AddressProxy;
import py.edu.uca.intercajas.dynatablerf.shared.DynaTableRequestFactory;
import py.edu.uca.intercajas.dynatablerf.shared.PersonProxy;
import py.edu.uca.intercajas.dynatablerf.shared.ScheduleProxy;
import py.edu.uca.intercajas.dynatablerf.shared.TimeSlotProxy;
import py.edu.uca.intercajas.dynatablerf.shared.DynaTableRequestFactory.PersonRequest;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

/**
 * A paging table with summaries of all known people.
 */
public class ListaBeneficiarios extends Composite {

  interface Binder extends UiBinder<Widget, ListaBeneficiarios> {
  }

  interface Style extends CssResource {
  }

  interface TableResources extends DataGrid.Resources {
    @Override
    @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
    DataGrid.Style dataGridStyle();
  }

  private class ColNombres extends Column<BeneficiarioProxy, String> {
    public ColNombres() {
      super(new TextCell());
    }

    @Override
    public String getValue(BeneficiarioProxy object) {
      return object.getNombres();
    }
  }

  private class ColApellidos extends Column<BeneficiarioProxy, String> {
	    public ColApellidos() {
	      super(new TextCell());
	    }
	    @Override
	    public String getValue(BeneficiarioProxy object) {
	      return object.getApellidos();
	    }
  }
  
  

//  @UiField
//  DockLayoutPanel dock;

  @UiField(provided = true)
  SimplePager pager = new SimplePager();

  @UiField(provided = true)
  DataGrid<BeneficiarioProxy> table;

  private final EventBus eventBus;
  private List<Boolean> filter = new ArrayList<Boolean>(ALL_DAYS);
  private int lastFetch;
  private final int numRows;
  private boolean pending;
  private final FactoryGestion requestFactory;
  private final SingleSelectionModel<BeneficiarioProxy> selectionModel = new SingleSelectionModel<BeneficiarioProxy>();

  public ListaBeneficiarios(EventBus eventBus,
		  FactoryGestion requestFactory, int numRows) {
    this.eventBus = eventBus;
    this.requestFactory = requestFactory;
    this.numRows = numRows;
    table = new DataGrid<BeneficiarioProxy>(numRows,
        GWT.<TableResources> create(TableResources.class));
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    

    Column<BeneficiarioProxy, String> colNombres = new ColNombres();
    table.addColumn(colNombres, "Nombres");
    table.setColumnWidth(colNombres, "25ex");

    Column<BeneficiarioProxy, String> colApellidos = new ColApellidos();
    table.addColumn(colApellidos, "Nombres");
    table.setColumnWidth(colApellidos, "25ex");
    
    table.setSelectionModel(selectionModel);
    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

    table.setEmptyTableWidget(new Label("Vacio"));
    
    
    EntityProxyChange.registerForProxyType(eventBus, BeneficiarioProxy.class,
        new EntityProxyChange.Handler<BeneficiarioProxy>() {
          @Override
          public void onProxyChange(EntityProxyChange<BeneficiarioProxy> event) {
            ListaBeneficiarios.this.onBeneficiarioChanged(event);
          }
        });

    FilterChangeEvent.register(eventBus, new FilterChangeEvent.Handler() {
      @Override
      public void onFilterChanged(FilterChangeEvent e) {
        filter.set(e.getDay(), e.isSelected());
        if (!pending) {
          pending = true;
          Scheduler.get().scheduleFinally(new ScheduledCommand() {
            @Override
            public void execute() {
              pending = false;
              fetch(0);
            }
          });
        }
      }
    });

    
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        ListaBeneficiarios.this.refreshSelection();
      }
    });
    
    fetch(0);
    
  }

  @UiHandler("create")
  void onCreate(ClickEvent event) {
//    PersonRequest context = requestFactory.personRequest();
//    AddressProxy address = context.create(AddressProxy.class);
//    ScheduleProxy schedule = context.create(ScheduleProxy.class);
//    schedule.setTimeSlots(new ArrayList<TimeSlotProxy>());
//    PersonProxy person = context.edit(context.create(PersonProxy.class));
//    person.setAddress(address);
//    person.setClassSchedule(schedule);
//    context.persist().using(person);
//    eventBus.fireEvent(new EditPersonEvent(person, context));
	  
	  final ContextGestionBeneficiario context =  requestFactory.contextGestionBeneficiario();
		requestFactory.initialize(eventBus);
	  
		BeneficiarioProxy beneficiario = context.create(BeneficiarioProxy.class);
		DocumentoIdentidadProxy docProxy = context.create(DocumentoIdentidadProxy.class);
		DireccionProxy dirProxy = context.create(DireccionProxy.class);

		docProxy.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
		beneficiario.setDocumento(docProxy);
		beneficiario.setDireccion(dirProxy);
	    
		new BeneficiarioEditorWorkFlow().create(beneficiario, context, requestFactory);
	  
	  
  }

  void onBeneficiarioChanged(EntityProxyChange<BeneficiarioProxy> event) {
	  Window.alert("onBeneficiarioChanged: " + event.getProxyId());
	/*
    if (WriteOperation.PERSIST.equals(event.getWriteOperation())) {
      // Re-fetch if we're already displaying the last page
      if (table.isRowCountExact()) {
        fetch(lastFetch + 1);
      }
    }
    if (WriteOperation.UPDATE.equals(event.getWriteOperation())) {
      EntityProxyId<PersonProxy> personId = event.getProxyId();

      // Is the changing record onscreen?
      int displayOffset = offsetOf(personId);
      if (displayOffset != -1) {
        // Record is onscreen and may differ from our data
        requestFactory.find(personId).fire(new Receiver<PersonProxy>() {
          @Override
          public void onSuccess(PersonProxy person) {
            // Re-check offset in case of changes while waiting for data
            int offset = offsetOf(person.stableId());
            if (offset != -1) {
              table.setRowData(table.getPageStart() + offset,
                  Collections.singletonList(person));
            }
          }
        });
      }
    }
	 */
  }

  /*
  @UiHandler("table")
  void onRangeChange(RangeChangeEvent event) {
    Range r = event.getNewRange();
    int start = r.getStart();

    fetch(start);
  }
  */

  void refreshSelection() {
    BeneficiarioProxy beneficiario = selectionModel.getSelectedObject();
    if (beneficiario == null) {
      return;
    }
    new BeneficiarioEditorWorkFlow().edit(beneficiario, null, requestFactory);
    //eventBus.fireEvent(new EditPersonEvent(person));
    selectionModel.setSelected(beneficiario, false);
  }

  private void fetch(final int start) {
	  
	  try {
		  
		  requestFactory.contextGestionBeneficiario().findAll().fire(new Receiver<List<BeneficiarioProxy>>() {

			@Override
			public void onSuccess(List<BeneficiarioProxy> response) {
				table.setRowCount(response.size(),true);
				  table.setRowData(0,response);
				  table.redraw();
				
			}
		});
		  
	  
	  } catch (Exception e) {
		  Window.alert(e.getMessage());
	  }
	/*  
    lastFetch = start;
    requestFactory.schoolCalendarRequest().getPeople(start, numRows, filter).fire(
        new Receiver<List<PersonProxy>>() {
          @Override
          public void onSuccess(List<PersonProxy> response) {
        	  
        	  
        	  
            if (lastFetch != start) {
              return;
            }

            int responses = response.size();
            table.setRowData(start, response);
            pager.setPageStart(start);
            if (start == 0 || !table.isRowCountExact()) {
              table.setRowCount(start + responses, responses < numRows);
            }
          }
          @Override
          public void onFailure(ServerFailure error) {
        	  Window.alert("aqui esta el error: " + error.getMessage());
          }
        });
      */

  }

  //TODO nosotros no tenemos EntityProxyId
  private int offsetOf(EntityProxyId<BeneficiarioProxy> beneficiarioId) {
    List<BeneficiarioProxy> displayedItems = table.getVisibleItems();
    for (int offset = 0, j = displayedItems.size(); offset < j; offset++) {
      if (beneficiarioId.equals(displayedItems.get(offset).stableId())) {
        return offset;
      }
    }
    return -1;
  }
}
