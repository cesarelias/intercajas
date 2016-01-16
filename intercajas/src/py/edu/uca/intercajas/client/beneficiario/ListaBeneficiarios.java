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

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
import py.edu.uca.intercajas.server.entity.Beneficiario;
import py.edu.uca.intercajas.server.entity.Direccion;
import py.edu.uca.intercajas.server.entity.DocumentoIdentidad;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

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
 
  private class ColNombres extends Column<Beneficiario, String> {
    public ColNombres() {
      super(new TextCell());
    }

    @Override
    public String getValue(Beneficiario object) {
      return object.getNombres() + ", " + object.getApellidos();// + " (" + object.getDocumento().getNumeroDocumento() + ")";
    }
  }

//  @UiField
//  DockLayoutPanel dock;

  @UiField(provided = true)
  SimplePager pager = new SimplePager();

  @UiField(provided = true)
  DataGrid<Beneficiario> table;
  
  @UiField TextBox filtroNombres;

  private final SimpleEventBus eventBus;
  private int lastFetch;
  private final int maxRows;
  private int lastStart = 0;
  private boolean pending;
  private final SingleSelectionModel<Beneficiario> selectionModel = new SingleSelectionModel<Beneficiario>();
  private HandlerRegistration r;
  
  public ListaBeneficiarios(SimpleEventBus eventBus, int maxRows) {
    this.eventBus = eventBus;
    this.maxRows = maxRows;
    
    this.title = "Beneficiarios";
    
    table = new DataGrid<Beneficiario>(maxRows,
        GWT.<TableResources> create(TableResources.class));
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));


    Column<Beneficiario, String> colNombres = new ColNombres();
    table.addColumn(colNombres, "Nombres y apellidos");
    table.setColumnWidth(colNombres, "25ex");

    table.setSelectionModel(selectionModel);
    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

    table.setEmptyTableWidget(new Label("Vacio"));
    
    
    r = eventBus.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
		@Override
		public void selected(Beneficiario beneficiarioSelected) {
			r.removeHandler();
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
			Beneficiario beneficiario = selectionModel.getSelectedObject();
		    if (beneficiario != null) {
		    	Window.alert("el beneficiario seleccionado es:" + beneficiario.getNombres());
		    }
		}
    },  DoubleClickEvent.getType());
    
    

    fetch(0);
    
  }

  @UiHandler("create")
  void onCreate(ClickEvent event) {
	  
	  Beneficiario beneficiario = new Beneficiario();
	  DocumentoIdentidad doc = new DocumentoIdentidad();
	  Direccion dir = new Direccion();

	  doc.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
	  beneficiario.setDocumento(doc);
	  beneficiario.setDireccion(dir);
    
	  beneficiario.setNombres("test");
	  BeneficiarioEditorWorkFlow b = new BeneficiarioEditorWorkFlow(eventBus);
	  b.title = "Nuevo Beneficiario";
	  b.mostrarDialog();
	  b.create();

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
	
    Beneficiario beneficiario = selectionModel.getSelectedObject();
    if (beneficiario == null) {
    	Window.alert("Seleccione un beneficiario para editar");
      return;
    }
    
    BeneficiarioEditorWorkFlow b = new BeneficiarioEditorWorkFlow(eventBus);
    b.title = "Editando Beneficiario";
    b.mostrarDialog();
    b.edit(beneficiario);
//    
  }

  
  @UiHandler("select")
  void onSelect(ClickEvent event) {
	
    Beneficiario beneficiario = selectionModel.getSelectedObject();
    if (beneficiario == null) {
    	return;
    }
    eventBus.fireEvent(new BeneficiarioChangedEvent(beneficiario));
    close();
    
  }

  @UiHandler("buscar")
  public void buscarClick(ClickEvent event){
//	  eventBus.fireEvent(new BeneficiarioChangedEvent(table.getVisibleItem(1))); //trae la segunda fila
  }

  @UiHandler("filtroNombres")
  public void buscarEnter(KeyDownEvent event){
	  if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		  fetch(0);
	  }
  }
  
  private void fetch(final int start) {
	  
	  lastFetch = start;
	  
	  BeneficiarioService.Util.get().findByNombresDocs(filtroNombres.getText(), start, maxRows, new MethodCallback<List<Beneficiario>>() {
		
		@Override
		public void onSuccess(Method method, List<Beneficiario> response) {
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
		
		@Override
		public void onFailure(Method method, Throwable exception) {
			// TODO Auto-generated method stub
		}
	  });
  }
  
  public void refreshTable() {
	  fetch(lastStart);
  }
  
}
