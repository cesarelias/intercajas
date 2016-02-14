package py.edu.uca.intercajas.client.tiemposervicio;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Beneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Widget;

public class TablaSolicitudBeneficiario extends UIBase {

	private static TablaTiempoServicioReconocidoUiBinder uiBinder = GWT
			.create(TablaTiempoServicioReconocidoUiBinder.class);

	interface TablaTiempoServicioReconocidoUiBinder extends
			UiBinder<Widget, TablaSolicitudBeneficiario> {
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

	List<Beneficiario> listaBeneficiario = new ArrayList<Beneficiario>();

	int row;
	int selectedRow;

	public TablaSolicitudBeneficiario() {

		initWidget(uiBinder.createAndBindUi(this));
		initTable();
	}

	@UiHandler("create")
	void onCreate(ClickEvent event) {

//		TiempoServicioReconocidoEditor pp = new TiempoServicioReconocidoEditor(
//				null);
//		pp.setListener(new TiempoServicioReconocidoEditor.Listener() {
//			@Override
//			public void onChanged(
//					TiempoServicioReconocido tiempoServicioReconocido) {
//				listaTiempoServicioReconocido.add(tiempoServicioReconocido);
//				refreshTable();
//			}
//		});
//		pp.titulo = "Nuevo tiempo de servicio reconocido";
//		pp.mostrarDialog();

	}
	
	@UiHandler("delete")
	void onDekete(ClickEvent event){

		styleRow(selectedRow, false);
		listaBeneficiario.remove(selectedRow);
		refreshTable();

	}
	

	void refreshTable() {
		
		table.clear(true);
		Beneficiario b;
		for (int i=0; i< listaBeneficiario.size();i++) {
			b = listaBeneficiario.get(i); 
			table.setText(i, 0, b.getApellidos());
			
		}
	}

	private void initTable() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "230px");
		header.getColumnFormatter().setWidth(1, "60px");
		

		header.setText(0, 0, "Beneficiario");
		header.setText(0, 1, "Parentesco");

		// Initialize the table.
		table.getColumnFormatter().setWidth(0, "230px");
		table.getColumnFormatter().setWidth(1, "60px");
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

		  	if ( row == listaBeneficiario.size()) {  //la ultima fila es un total)
		  		return;
		  	}
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
