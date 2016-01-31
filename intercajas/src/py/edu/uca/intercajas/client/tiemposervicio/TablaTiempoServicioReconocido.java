package py.edu.uca.intercajas.client.tiemposervicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;
import py.edu.uca.intercajas.shared.entity.TiempoServicioReconocido;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class TablaTiempoServicioReconocido extends UIBase {

	private static TablaTiempoServicioReconocidoUiBinder uiBinder = GWT
			.create(TablaTiempoServicioReconocidoUiBinder.class);

	interface TablaTiempoServicioReconocidoUiBinder extends
			UiBinder<Widget, TablaTiempoServicioReconocido> {
	}

	interface TableResources extends DataGrid.Resources {
		// @Override
		// @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
		// DataGrid.Style dataGridStyle();
	}

	@UiField
	FlexTable header;
	@UiField
	FlexTable table;

	HandlerRegistration h;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");

	ListDataProvider<TiempoServicioReconocido> model;
	List<TiempoServicioReconocido> listaTiempoServicioReconocido = new ArrayList<TiempoServicioReconocido>();

	int row;
	int selectedRow;

	public TablaTiempoServicioReconocido() {

		initWidget(uiBinder.createAndBindUi(this));
		initTable();
		
		//sacar este codigo luego hasta el final del contructor
		TiempoServicioReconocido r;
		r = new TiempoServicioReconocido();
		r.setInicio(new Date());
		r.setFin(new Date());
		listaTiempoServicioReconocido.add(r);
		listaTiempoServicioReconocido.add(r);
		listaTiempoServicioReconocido.add(r);
		refreshTable();
		
	}

	@UiHandler("create")
	void onCreate(ClickEvent event) {

		TiempoServicioReconocidoEditor pp = new TiempoServicioReconocidoEditor(
				null);
		pp.setListener(new TiempoServicioReconocidoEditor.Listener() {
			@Override
			public void onChanged(
					TiempoServicioReconocido tiempoServicioReconocido) {
				listaTiempoServicioReconocido.add(tiempoServicioReconocido);
				refreshTable();
			}
		});
		pp.titulo = "Nuevo tiempo de servicio reconocido";
		pp.mostrarDialog();

	}
	
	@UiHandler("edit")
	void onEdit(ClickEvent event){

		TiempoServicioReconocidoEditor pp = new TiempoServicioReconocidoEditor(
				listaTiempoServicioReconocido.get(selectedRow));

		pp.setListener(new TiempoServicioReconocidoEditor.Listener() {
			@Override
			public void onChanged(
					TiempoServicioReconocido tiempoServicioReconocido) {
				listaTiempoServicioReconocido
						.set(row, tiempoServicioReconocido);
				refreshTable();
			}
		});

		pp.titulo = "Editar tiempo de servicio reconocido";
		pp.mostrarDialog();

	}
	
	@UiHandler("delete")
	void onDekete(ClickEvent event){

		styleRow(selectedRow, false);
		listaTiempoServicioReconocido.remove(selectedRow);
		refreshTable();

	}
	

	void refreshTable() {
		
		table.clear(true);
		TiempoServicioReconocido t;
		for (int i=0; i< listaTiempoServicioReconocido.size();i++) {
			t = listaTiempoServicioReconocido.get(i); 
			table.setText(i, 0, "Empleador tal tal");
			table.setText(i, 1, dateFormat.format(t.getInicio()).toString());
			table.setText(i, 2, dateFormat.format(t.getFin()).toString());
			table.setText(i, 3, "2 años 3 meses");
		}
		if (listaTiempoServicioReconocido.size() > 0 ) {
			selectRow(0);
			table.setWidget(listaTiempoServicioReconocido.size(), 2, new HTML("<b>Totalización</b>"));
			table.setWidget(listaTiempoServicioReconocido.size(), 3, new HTML("<b>12 años 3 meses</b>"));
			
			
			
		}
		
	}

	private void initTable() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "230px");
		header.getColumnFormatter().setWidth(1, "60px");
		header.getColumnFormatter().setWidth(2, "60px");
		header.getColumnFormatter().setWidth(3, "110px");
		

		header.setText(0, 0, "Empleador");
		header.setText(0, 1, "Inicio");
		header.setText(0, 2, "Fin");
		header.setText(0, 3, "Tiempo");

		// Initialize the table.
		table.getColumnFormatter().setWidth(0, "230px");
		table.getColumnFormatter().setWidth(1, "60px");
		table.getColumnFormatter().setWidth(2, "60px");
		table.getColumnFormatter().setWidth(3, "110px");
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
	  
	  void selectRow(int row) {
		  
//	  		if (row < 0 || listaTiempoServicioReconocido.size() == 0) {
//	  			return; 
//	  		};
	  		
//			selectedItem = listaTiempoServicioReconocido.get(row);
		     
//		     if (selectedItem == null) {
//		       return;
//		     }

		     styleRow(selectedRow, false);
		     styleRow(row, true);

		     selectedRow = row;

//		     if (listener != null) {
//		       listener.onItemSelected(selectedItem);
//		     }
		     
	  }
	  
	  private void styleRow(int row, boolean selected) {
		    if (row != -1) {
		      String style = "selectedRow";
		      if (selected) {
		        table.getRowFormatter().addStyleName(row, style);
		      } else {
		        table.getRowFormatter().removeStyleName(row, style);
		      }
		    }
		  }

}
