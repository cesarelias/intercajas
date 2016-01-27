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
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.solicitud.events.SolicitudCreatedEvent;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
	
  public interface Listener {
    void onItemSelected(Mensaje item);
  }

  interface Binder extends UiBinder<Widget, MailList> { }
  interface SelectionStyle extends CssResource {
    String selectedRow();
  }

  private static final Binder binder = GWT.create(Binder.class);
  static final int VISIBLE_EMAIL_COUNT = 8;

  @UiField FlexTable header;
  @UiField FlexTable table;
  @UiField SelectionStyle selectionStyle;

  Mensaje selectedItem;
  List<Mensaje> mensajes =  new ArrayList<Mensaje>();
  
  private Listener listener;
  private int startIndex, selectedRow = -1;
  private NavBar navBar;
  private boolean onLoad = true;

  public MailList() {
	  
    initWidget(binder.createAndBindUi(this));
    navBar = new NavBar(this);
    
    initTable();
    update();
    registerEvent();

  }

  
  public void registerEvent() {
	    AppUtils.EVENT_BUS.addHandler(SolicitudCreatedEvent.TYPE, new SolicitudCreatedEvent.Handler() {
				@Override
				public void created(Solicitud solicitud) {
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
    if (startIndex >= MailItems.getMailItemCount()) {
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
    header.getColumnFormatter().setWidth(0, "128px");
    header.getColumnFormatter().setWidth(1, "192px");
    header.getColumnFormatter().setWidth(3, "256px");

    header.setText(0, 0, "De");
    header.setText(0, 1, "Asunto");
    header.setText(0, 2, "Referencia");
    header.setWidget(0, 3, navBar);
    header.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);

    // Initialize the table.
    table.getColumnFormatter().setWidth(0, "128px");
    table.getColumnFormatter().setWidth(1, "192px");
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
	  
	  		if (row < 0 || mensajes.size() == 0) {
	  			Window.alert("aqui nomas estaba el problem ");
	  			return; 
	  		};
	  		
			selectedItem = mensajes.get(row);
		     
		     if (selectedItem == null) {
		       return;
		     }

		     styleRow(selectedRow, false);
		     styleRow(row, true);
//
//		     //TODO aqui marcar como leido
////		     item.set read = true;
		     selectedRow = row;
//
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

  private void update() {
    // Update the older/newer buttons & label.
//    int count = MailItems.getMailItemCount();
    int max = startIndex + VISIBLE_EMAIL_COUNT;
//    if (max > count) {
//      max = count;
//    }

    // Update the nav bar.
    navBar.update(startIndex, max);
    
    BeneficiarioService.Util.get().findAllPending(startIndex, VISIBLE_EMAIL_COUNT, new MethodCallback<List<Mensaje>>() {
		
		@Override
		public void onSuccess(Method method, List<Mensaje> response) {
			table.removeAllRows();
		      // Add a new row to the table, then set each of its columns to the
		      // email's sender and subject values.
						
			for (int i=0; i<response.size(); i++) {
		      table.setText(i, 0, response.get(i).getRemitente().getSiglas());
		      table.setText(i, 1, response.get(i).getAsunto().toString());
		      table.setText(i, 2, response.get(i).getReferencia());
			}
			
			mensajes = response;
			
			//Al cargar por primera vez la lista de correo, seleccionamos la primera fila
			if (onLoad && mensajes.size() > 1) {
				selectRow(0);
				onLoad = false;
			}
		}
		
		@Override
		public void onFailure(Method method, Throwable exception) {
			// TODO falta agregar el mensaje de error del REST
		}
	});
    
  }
}
