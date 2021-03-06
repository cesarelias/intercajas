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
package py.edu.uca.intercajas.client.view.login;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditor.Listener;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Direccion;
import py.edu.uca.intercajas.shared.entity.DocumentoIdentidad;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;
import py.edu.uca.intercajas.shared.entity.DocumentoIdentidad.TipoDocumentoIdentidad;
import py.edu.uca.intercajas.shared.entity.Usuario;

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
import com.google.gwt.user.client.ui.Button;
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
public class ListaUsuarios extends UIBase {

  interface Binder extends UiBinder<Widget, ListaUsuarios> {
  }

  interface Style extends CssResource {
  }

  interface TableResources extends DataGrid.Resources {
//    @Override
//    @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
//    DataGrid.Style dataGridStyle();
  }
  
	public interface Listener {
		void onSelected(Usuario usuarioSelected);
	}
 
	Listener listener;
	
  private class ColDescripcion extends Column<Usuario, String> {
    public ColDescripcion() {
      super(new TextCell());
    }

    @Override
    public String getValue(Usuario object) {
      return object.getDescripcion();// + " (" + object.getDocumento().getNumeroDocumento() + ")";
    }
  }

//  @UiField
//  DockLayoutPanel dock;

  @UiField(provided = true)
  SimplePager pager = new SimplePager();

  @UiField(provided = true)
  DataGrid<Usuario> table;
  
  @UiField TextBox filtroNombres;

  @UiField Button select;
  
  private int lastFetch;
  private final int maxRows;
  private int lastStart = 0;
  private boolean pending;
  private final SingleSelectionModel<Usuario> selectionModel = new SingleSelectionModel<Usuario>();
  private HandlerRegistration r;
  
  public ListaUsuarios(int maxRows) {
    this.maxRows = maxRows;
    
    this.titulo = "Lista de Usuarios";
    
    table = new DataGrid<Usuario>(maxRows,
        GWT.<TableResources> create(TableResources.class));
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

    Column<Usuario, String> colDescripcion = new ColDescripcion();
    table.addColumn(colDescripcion, "Descripcion");
    table.setColumnWidth(colDescripcion, "25ex");

    table.setSelectionModel(selectionModel);
    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

    table.setEmptyTableWidget(new Label("Vacio"));
    
//    r = AppUtils.EVENT_BUS.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
//		@Override
//		public void selected(Beneficiario beneficiarioSelected) {
//			r.removeHandler();
//			refreshTable();
//		}
//	});
    
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        //ListaBeneficiarios.this.edit();
      }
    });

    table.addDomHandler(new DoubleClickHandler() {
		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			onSelect(null);
		}
    },  DoubleClickEvent.getType());
    
    

    fetch(0);
    
    filtroNombres.setFocus(true);
    
  }


  @UiHandler("close")
  void onClose(ClickEvent event) { 
	  close();
  }
  

  @UiHandler("create")
  void onCreate(ClickEvent event) {
	  
	  UIEditarUsuario e = new UIEditarUsuario(null);
	  e.mostrarDialog();
	  
  }

  @UiHandler("table")
  void onRangeChange(RangeChangeEvent event) {
    Range r = event.getNewRange();
    int start = r.getStart();
    lastStart = start;
    fetch(start);
  }
  

  @UiHandler("del")
  void onDel(ClickEvent event) {

		Usuario usuario = selectionModel.getSelectedObject();
		if (usuario == null) {
			Window.alert("Seleccione una fila para eliminar");
			return;
		}

		
	  BeneficiarioService.Util.get().eliminarUsuario(usuario.getId(), new MethodCallback<Void>() {

		@Override
		public void onFailure(Method method, Throwable exception) {
			new UIErrorRestDialog(method, exception);
		}

		@Override
		public void onSuccess(Method method, Void response) {
			refreshTable();
		}
	});
	  
  }
  
  
  @UiHandler("edit")
  void onEdit(ClickEvent event) {
	

		Usuario usuario = selectionModel.getSelectedObject();
		if (usuario == null) {
			Window.alert("Seleccione una fila para editar");
			return;
		}
	
		UIEditarUsuario e = new UIEditarUsuario(usuario);
		
		e.setListener(new UIEditarUsuario.Listener() {
			@Override
			public void onSave() {
				refreshTable();				
			}
		});
		
		e.mostrarDialog();
		
  }

  
  @UiHandler("select")
  void onSelect(ClickEvent event) {
	
    Usuario usuario = selectionModel.getSelectedObject();
    if (usuario == null) {
    	return;
    }
    
//    AppUtils.EVENT_BUS.fireEvent(new BeneficiarioChangedEvent(beneficiario));
    
	if (listener!=null) {
		listener.onSelected(usuario);
	}
    
    close();
    
  }

  @UiHandler("buscar")
  public void buscarClick(ClickEvent event){
	  fetch(0);
//	  eventBus.fireEvent(new BeneficiarioChangedEvent(table.getVisibleItem(1))); //trae la segunda fila
  }

  @UiHandler("filtroNombres")
  public void buscarEnter(KeyDownEvent event){
	  if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		  fetch(0);
	  } else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
		  close();
	  }
  }
  
  private void fetch(final int start) {
	  
	  lastFetch = start;
	  
	  
	  BeneficiarioService.Util.get().findUsuarioByDescripcionNombre(filtroNombres.getText(), start, maxRows, new MethodCallback<List<Usuario>>() {

		@Override
		public void onFailure(Method method, Throwable exception) {
			new UIErrorRestDialog(method, exception);
		}

		@Override
		public void onSuccess(Method method, List<Usuario> response) {
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
  
  public void hideSelectButton() {
	  select.setVisible(false);
  }


public void setListener(Listener listener) {
	this.listener = listener;
}
  
}
