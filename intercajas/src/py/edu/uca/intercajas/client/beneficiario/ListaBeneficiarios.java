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

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.WriteOperation;
import com.sun.java.swing.plaf.windows.resources.windows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.input.KeyCode;
import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
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
import py.edu.uca.intercajas.shared.UIBase;

/**
 * A paging table with summaries of all known people.
 */
public class ListaBeneficiarios extends UIBase {

	

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
      return object.getNombres() + ", " + object.getApellidos();
    }
  }

//  @UiField
//  DockLayoutPanel dock;

  @UiField(provided = true)
  SimplePager pager = new SimplePager();

  @UiField(provided = true)
  DataGrid<BeneficiarioProxy> table;
  
  @UiField TextBox filtroNombres;

  private final SimpleEventBus eventBus;
  private int lastFetch;
  private final int maxRows;
  private int lastStart = 0;
  private boolean pending;
  private final FactoryGestion requestFactory;
  private final SingleSelectionModel<BeneficiarioProxy> selectionModel = new SingleSelectionModel<BeneficiarioProxy>();

  public ListaBeneficiarios(SimpleEventBus eventBus,
		  FactoryGestion requestFactory, int maxRows) {
    this.eventBus = eventBus;
    this.requestFactory = requestFactory;
    this.maxRows = maxRows;
    table = new DataGrid<BeneficiarioProxy>(maxRows,
        GWT.<TableResources> create(TableResources.class));
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));


    Column<BeneficiarioProxy, String> colNombres = new ColNombres();
    table.addColumn(colNombres, "Nombres y apellidos");
    table.setColumnWidth(colNombres, "25ex");

    table.setSelectionModel(selectionModel);
    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

    table.setEmptyTableWidget(new Label("Vacio"));
    
    
    requestFactory.initialize(eventBus);
    
    eventBus.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
		@Override
		public void selected(BeneficiarioProxy beneficiarioSelected) {
			refreshTable();
		}
	});
    
//    FilterChangeEvent.register(eventBus, new FilterChangeEvent.Handler() {
//      @Override
//      public void onFilterChanged(FilterChangeEvent e) {
//        filter.set(e.getDay(), e.isSelected());
//        if (!pending) {
//          pending = true;
//          Scheduler.get().scheduleFinally(new ScheduledCommand() {
//            @Override
//            public void execute() {
//              pending = false;
//              fetch(0);
//            }
//          });
//        }
//      }
//    });

    
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        //ListaBeneficiarios.this.edit();
      }
    });

    table.addDomHandler(new DoubleClickHandler() {
		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			BeneficiarioProxy beneficiario = selectionModel.getSelectedObject();
		    if (beneficiario != null) {
		    	Window.alert("el beneficiario seleccionado es:" + beneficiario.getNombres());
		    }
		}
    },  DoubleClickEvent.getType());
    
    

    fetch(0);
    
  }

  @UiHandler("create")
  void onCreate(ClickEvent event) {
	  
	  final ContextGestionBeneficiario context =  requestFactory.contextGestionBeneficiario();
  
	  BeneficiarioProxy beneficiario = context.create(BeneficiarioProxy.class);
	  DocumentoIdentidadProxy docProxy = context.create(DocumentoIdentidadProxy.class);
	  DireccionProxy dirProxy = context.create(DireccionProxy.class);

	  docProxy.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
	  beneficiario.setDocumento(docProxy);
	  beneficiario.setDireccion(dirProxy);
    
	  BeneficiarioEditorWorkFlow b = new BeneficiarioEditorWorkFlow();
	  b.mostrarDialog(null, "Editar beneficiario", eventBus);
	  b.create(beneficiario, context, requestFactory);
	    
//	  new BeneficiarioEditorWorkFlow().create(beneficiario, context, requestFactory);
	  
  }

  @UiHandler("table")
  void onRangeChange(RangeChangeEvent event) {
    Range r = event.getNewRange();
    int start = r.getStart();
    lastStart = start;
    fetch(start);
  }
  
  
  @UiHandler("edit")
  void onEdit(ClickEvent event) {
	
    BeneficiarioProxy beneficiario = selectionModel.getSelectedObject();
    if (beneficiario == null) {
    	Window.alert("Seleccione un beneficiario para editar");
      return;
    }
    
    BeneficiarioEditorWorkFlow b = new BeneficiarioEditorWorkFlow();
    b.mostrarDialog(null, "Editar beneficiario", eventBus);
    b.edit(beneficiario, null, requestFactory);
//    selectionModel.setSelected(beneficiario, false);
    //eventBus.fireEvent(new EditPersonEvent(person));
    
  }

  
  @UiHandler("select")
  void onSelect(ClickEvent event) {
	
    BeneficiarioProxy beneficiario = selectionModel.getSelectedObject();
    if (beneficiario == null) {
    	return;
    }
    eventBus.fireEvent(new BeneficiarioChangedEvent(beneficiario));
    close();
    
  }

  @UiHandler("buscar")
  public void buscarClick(ClickEvent event){
	  //fetch(0);
	  eventBus.fireEvent(new BeneficiarioChangedEvent(table.getVisibleItem(1))); //trae la segunda fila
  }

  @UiHandler("filtroNombres")
  public void buscarEnter(KeyDownEvent event){
	  if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		  fetch(0);
	  }
  }
  
  private void fetch(final int start) {
	  
	  lastFetch = start;
	  requestFactory.contextGestionBeneficiario().findByNombres(filtroNombres.getText(), start, maxRows).fire(new Receiver<List<BeneficiarioProxy>>() {
		@Override
		public void onSuccess(List<BeneficiarioProxy> response) {

            if (lastFetch != start) {
                return;
              }

            int responses = response.size();
            table.setRowData(start, response);
            pager.setPageStart(start);
            if (start == 0 || !table.isRowCountExact()) {
              table.setRowCount(start + responses, responses < maxRows);
            }
			
		}
	});

  }
  
  public void refreshTable() {
	  fetch(lastStart);
  }
  
}
