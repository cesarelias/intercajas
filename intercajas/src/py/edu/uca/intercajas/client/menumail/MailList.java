/*
 * Copyright 2007 Google Inc.
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
package py.edu.uca.intercajas.client.menumail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.solicitud.events.SolicitudCreatedEvent;
import py.edu.uca.intercajas.shared.BandejaParam;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite that displays a list of emails that can be selected.
 */
public class MailList extends ResizeComposite {

  /**
   * Callback when mail items are selected. 
   */
	
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
	
  public interface Listener {
    void onItemSelected(Destino item);
    void clear();
  }

  interface Binder extends UiBinder<Widget, MailList> { }
  interface SelectionStyle extends CssResource {
    String selectedRow();
  }

  
  public BandejaParam parametros = new BandejaParam();
  public Long beneficiarioIdFilter;
  public Long cajaIdFilter;
  
  private static final Binder binder = GWT.create(Binder.class);
  static final int VISIBLE_EMAIL_COUNT = 8;

  @UiField FlexTable header;
  @UiField FlexTable table;
  @UiField SelectionStyle selectionStyle;

  Destino selectedItem;
  List<Destino> destinos =  new ArrayList<Destino>();
  
  private Listener listener;
  private int startIndex, selectedRow = -1;
  private NavBar navBar;
  private boolean onLoad = true;

  public Modo modo;
  
  public enum Modo {
	  MisPendientes,
	  MisFiniquitados,
	  Pendientes,
	  Finquitados,
	  Anulados
  }
  
  public MailList(Modo modo) {
	
    initWidget(binder.createAndBindUi(this));
    navBar = new NavBar(this);
    
    
    this.modo = modo;
    
    initTable();
    update();
    registerEvent();

  }

  
  public void registerEvent() {
	    
	  
	  
	    AppUtils.EVENT_BUS.addHandler(RefreshMailEvent.TYPE, new RefreshMailEvent.Handler() {

			@Override
			public void refresh(Long beneficiarioIdFilter, Long cajaIdFilter) {
				// TODO Auto-generated method stub
				MailList.this.beneficiarioIdFilter = beneficiarioIdFilter;
				MailList.this.cajaIdFilter = cajaIdFilter;
				update();
				
			}

		});
	    

  }
  /**
   * Sets the listener that will be notified when an item is selected.
   */
  public void setListener(Listener listener) {
    this.listener = listener;
  }

//  @Override
//  protected void onLoad() {
//    // Select the first row if none is selected.
//    if (selectedRow == -1) {
//      selectRow(0);
//    }
//  }

  void newer() {
    // Move back a page.
    startIndex -= VISIBLE_EMAIL_COUNT;
    if (startIndex < 0) {
      startIndex = 0;
    } else {
      styleRow(selectedRow, false);
      selectedRow = -1;
      update();
    }
  }

  //TODO arreglar el older, nunca termina!
  void older() {
    // Move forward a page.
    startIndex += VISIBLE_EMAIL_COUNT;
    if (table.getRowCount() < VISIBLE_EMAIL_COUNT) {
      startIndex -= VISIBLE_EMAIL_COUNT;
    } else {
      styleRow(selectedRow, false);
      selectedRow = -1;
      update();
    }
  }

  @UiHandler("table")
  void onTableClicked(ClickEvent event) {
    // Select the row that was clicked (-1 to account for header row).
    Cell cell = table.getCellForEvent(event);
    if (cell != null) {
      int row = cell.getRowIndex();
      selectRow(row);
    }
  }

  /**
   * Initializes the table so that it contains enough rows for a full page of
   * emails. Also creates the images that will be used as 'read' flags.
   */
  private void initTable() {
    // Initialize the header.
    header.getColumnFormatter().setWidth(0, "120px");
    header.getColumnFormatter().setWidth(1, "120px");
    header.getColumnFormatter().setWidth(2, "210px");
//    header.getColumnFormatter().setWidth(3, "300x");
    

    header.setText(0, 0, "De");
    header.setText(0, 1, "Fecha");
    header.setText(0, 2, "Asunto");
    header.setText(0, 3, "Referencia");
    header.setWidget(0, 4, navBar);
    header.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);

    // Initialize the table.
    table.getColumnFormatter().setWidth(0, "120px");
    table.getColumnFormatter().setWidth(1, "120px");
    table.getColumnFormatter().setWidth(2, "210px");
//    table.getColumnFormatter().setWidth(3, "300px");
  }

  /**
   * Selects the given row (relative to the current page).
   * 
   * @param row the row to be selected
   */
  private void selectRow(int row) {
    // When a row (other than the first one, which is used as a header) is
    // selected, display its associated MailItem.
    		//MailItems.getMailItem(startIndex + row);
	  
	  		if (row < 0 || destinos.size() == 0) {
	  			return; 
	  		};
	  		
			selectedItem = destinos.get(row);
		     
		     if (selectedItem == null) {
		       return;
		     }

		     styleRow(selectedRow, false);
		     styleRow(row, true);

		     selectedRow = row;

		     if (listener != null) {
		       listener.onItemSelected(selectedItem);
		     }

  }

  private void styleRow(int row, boolean selected) {
    if (row != -1) {
      String style = selectionStyle.selectedRow();
      if (selected) {
        table.getRowFormatter().addStyleName(row, style);
      } else {
        table.getRowFormatter().removeStyleName(row, style);
      }
    }
  }

  void update() {
	  //limpiamos la tabla
	  table.removeAllRows();
	  //limpiamos el mailDetail
	  if (listener!=null) {
		  listener.clear();
	  }
	  
	  
    // Update the older/newer buttons & label.
//    int count = MailItems.getMailItemCount();
    int max = startIndex + VISIBLE_EMAIL_COUNT;
//    if (max > count) {
//      max = count;
//    }

    // Update the nav bar.
    navBar.update(startIndex, max);
    
    if (modo == Modo.MisPendientes) {
    	
    	parametros = new BandejaParam();
    	parametros.setStartRow(startIndex);
    	parametros.setMaxResults(VISIBLE_EMAIL_COUNT);
    	parametros.setBeneficiario_id(beneficiarioIdFilter);
    	parametros.setRemitente_id(cajaIdFilter);
    	parametros.setFecha_desde(null);
    	parametros.setFecha_hasta(null);
    	BeneficiarioService.Util.get().findMisPendientes(parametros, new MethodCallback<List<Destino>>() {
    		
    		@Override
    		public void onSuccess(Method method, List<Destino> response) {
    			update(response);
    		}
    		@Override
    		public void onFailure(Method method, Throwable exception) {
    			new UIErrorRestDialog(method, exception);
    		}
    	});
    } else if (modo == Modo.MisFiniquitados) {
    	BeneficiarioService.Util.get().findMisFiniquitados(startIndex, VISIBLE_EMAIL_COUNT, new MethodCallback<List<Destino>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
			@Override
			public void onSuccess(Method method, List<Destino> response) {
				update(response);
			}
		});
    } else if (modo == Modo.Pendientes) {
    	BeneficiarioService.Util.get().findPendientes(startIndex, VISIBLE_EMAIL_COUNT, new MethodCallback<List<Destino>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
			@Override
			public void onSuccess(Method method, List<Destino> response) {
				update(response);
			}
		});
    } else if (modo == Modo.Finquitados) {
    	BeneficiarioService.Util.get().findFiniquitados(startIndex, VISIBLE_EMAIL_COUNT, new MethodCallback<List<Destino>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
			@Override
			public void onSuccess(Method method, List<Destino> response) {
				update(response);
			}
		});
    } else if (modo == Modo.Anulados) {
    	BeneficiarioService.Util.get().findAnulados(startIndex, VISIBLE_EMAIL_COUNT, new MethodCallback<List<Destino>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
			@Override
			public void onSuccess(Method method, List<Destino> response) {
				update(response);
			}
		});
    }
    
  }
  
  private void update(List<Destino> items) {
		table.removeAllRows();
		// Add a new row to the table, then set each of its columns to the
		// email's sender and subject values.
		
		for (int i=0; i<items.size(); i++) {
			if (items.get(i).getMensaje().getRemitente() == null) {
				table.setText(i, 0, "SistemaIntercajas");
			} else {
				table.setText(i, 0, items.get(i).getMensaje().getRemitente().getSiglas());
			}
			table.setText(i, 1, dateFormat.format(items.get(i).getMensaje().getFecha()).toString());
			table.setText(i, 2, items.get(i).getMensaje().getAsunto().toString());
			table.setText(i, 3, items.get(i).getMensaje().getReferencia());
		}
		
		destinos = items;
		
		//Al cargar por primera vez la lista de correo, seleccionamos la primera fila
		if (onLoad && destinos.size() > 1) {
			selectRow(0);
			onLoad = false;
//		} else {
//			selectRow(selectedRow);
		}

  }
}
