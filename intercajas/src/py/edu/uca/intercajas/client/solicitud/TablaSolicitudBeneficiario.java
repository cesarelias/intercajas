package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios.Listener;
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
import com.google.web.bindery.event.shared.HandlerRegistration;

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
	
	HandlerRegistration r;

	@UiField
	FlexTable header;
	@UiField
	FlexTable table;

	ParentescoEditor parentesco;

	List<Beneficiario> listaBeneficiario = new ArrayList<Beneficiario>();
	List<ParentescoEditor> listaParentescoEditors = new ArrayList<ParentescoEditor>();

	int row;
	int selectedRow;

	public TablaSolicitudBeneficiario() {

		initWidget(uiBinder.createAndBindUi(this));
		initTable();
	}

	@UiHandler("create")
	void onCreate(ClickEvent event) {
		
	    ListaBeneficiarios l = new ListaBeneficiarios(10);
	    
	    l.setListener(new Listener() {
			@Override
			public void onSelected(Beneficiario beneficiarioSelected) {
				listaBeneficiario.add(beneficiarioSelected);
				listaParentescoEditors.add(new ParentescoEditor());
				refreshTable();
				
			}
		});
	    
	    l.mostrarDialog();
	    
	    
	}

	@UiHandler("delete")
	void onDelete(ClickEvent event) {

		styleRow(selectedRow, false);
		listaBeneficiario.remove(selectedRow);
		listaParentescoEditors.remove(selectedRow);
		refreshTable();

	}

	void refreshTable() {

		table.clear(true);
		Beneficiario b;
		for (int i = 0; i < listaBeneficiario.size(); i++) {
			b = listaBeneficiario.get(i);
			table.setText(i, 0, b.toString());
			table.setWidget(i, 1, listaParentescoEditors.get(i));

		}
	}

	private void initTable() {
		// Initialize the header.
//		header.setWidth("320px");
//		header.getColumnFormatter().setWidth(0, "210px");
		header.getColumnFormatter().setWidth(1, "110px");

		header.setText(0, 0, "Beneficiario");
		header.setText(0, 1, "Parentesco");

//		table.setWidth("320px");
		// Initialize the table.
//		table.getColumnFormatter().setWidth(0, "210px");
		table.getColumnFormatter().setWidth(1, "110px");

//		table.setWidget(0, 1, new ParentescoEditor());
//		table.setWidget(1, 1, new ParentescoEditor());

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

		// if (row < 0 || listaTiempoServicioReconocido.size() == 0) {
		// return;
		// };

		// selectedItem = listaTiempoServicioReconocido.get(row);

		// if (selectedItem == null) {
		// return;
		// }

		if (row == listaBeneficiario.size()) { // la ultima fila es un total)
			return;
		}
		styleRow(selectedRow, false);
		styleRow(row, true);

		selectedRow = row;

		// if (listener != null) {
		// listener.onItemSelected(selectedItem);
		// }

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
