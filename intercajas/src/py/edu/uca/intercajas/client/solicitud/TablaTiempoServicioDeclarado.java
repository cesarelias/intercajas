package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.shared.CalculoTiempo;
import py.edu.uca.intercajas.shared.RangoTiempo;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class TablaTiempoServicioDeclarado extends UIBase {

	private static TablaTiempoServicioDeclaradoUiBinder uiBinder = GWT
			.create(TablaTiempoServicioDeclaradoUiBinder.class);

	interface TablaTiempoServicioDeclaradoUiBinder extends
			UiBinder<Widget, TablaTiempoServicioDeclarado> {
	}

	interface TableResources extends DataGrid.Resources {
		// @Override
		// @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
		// DataGrid.Style dataGridStyle();
	}

	@UiField
	FlexTable table;
	@UiField
	FlexTable header;

	HandlerRegistration h;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");

	ListDataProvider<TiempoServicioDeclarado> model;
	List<TiempoServicioDeclarado> listaTiempoServicioDeclarado = new ArrayList<TiempoServicioDeclarado>();

	int row;
	int selectedRow;

	public TablaTiempoServicioDeclarado() {

		initWidget(uiBinder.createAndBindUi(this));
		initTable();

	}

	private void initTable() {
		// Initialize the header.
		header.getColumnFormatter().setWidth(0, "60px");
		header.getColumnFormatter().setWidth(1, "220px");
		header.getColumnFormatter().setWidth(2, "60px");
		header.getColumnFormatter().setWidth(3, "110px");
		header.getColumnFormatter().setWidth(4, "110px");

		header.setText(0, 0, "Caja");
		header.setText(0, 1, "Lugar");
		header.setText(0, 2, "Inicio");
		header.setText(0, 3, "Fin");
		header.setText(0, 4, "Tiempo");

		// Initialize the table.
		table.getColumnFormatter().setWidth(0, "60px");
		table.getColumnFormatter().setWidth(1, "220px");
		table.getColumnFormatter().setWidth(2, "60px");
		table.getColumnFormatter().setWidth(3, "110px");
		table.getColumnFormatter().setWidth(4, "110px");

	}

	@UiHandler("create")
	void onCreate(ClickEvent event) {

		TiempoServicioDeclaradoEditor pp = new TiempoServicioDeclaradoEditor(
				null);
		pp.setListener(new TiempoServicioDeclaradoEditor.Listener() {
			@Override
			public void onChanged(
					TiempoServicioDeclarado tiempoServicioDeclatado) {
				listaTiempoServicioDeclarado.add(row, tiempoServicioDeclatado);
				refreshTable();
			}
		});
		pp.titulo = "Nuevo tiempo de servicio declarado";
		pp.mostrarDialog();

	}

	void onEdit(TiempoServicioDeclarado periodoAporteDeclaradoProxy) {

		TiempoServicioDeclaradoEditor pp = new TiempoServicioDeclaradoEditor(
				listaTiempoServicioDeclarado.get(selectedRow));
		pp.setListener(new TiempoServicioDeclaradoEditor.Listener() {
			@Override
			public void onChanged(
					TiempoServicioDeclarado tiempoServicioDeclatado) {
				listaTiempoServicioDeclarado.set(row, tiempoServicioDeclatado);
				refreshTable();
			}
		});
		pp.titulo = "Editar tiempo de servicio declarado";
		pp.mostrarDialog();

	}

	@UiHandler("delete")
	void onDekete(ClickEvent event){

		styleRow(selectedRow, false);
		listaTiempoServicioDeclarado.remove(selectedRow);
		refreshTable();

	}
	
	void refreshTable() {
		table.clear(true);
		TiempoServicioDeclarado t;
		for (int i = 0; i < listaTiempoServicioDeclarado.size(); i++) {
			t = listaTiempoServicioDeclarado.get(i);
			table.setText(i, 0, t.getCaja().getSiglas());
			table.setText(i, 1, t.getLugar());
			table.setText(i, 2, dateFormat.format(t.getInicio()).toString());
			table.setText(i, 3, dateFormat.format(t.getFin()).toString());

			List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
			rangos.add(new RangoTiempo(t.getInicio(), t.getFin()));
			int meses = CalculoTiempo.txBruto(rangos);
			String mesesLetra = CalculoTiempo.leeMeses(meses);

			table.setText(i, 4, mesesLetra);

		}
		if (listaTiempoServicioDeclarado.size() > 0) {

			List<RangoTiempo> rangos = new ArrayList<RangoTiempo>();
			for (TiempoServicioDeclarado r : listaTiempoServicioDeclarado) {
				rangos.add(new RangoTiempo(r.getInicio(), r.getFin()));
			}

			int meses = CalculoTiempo.txBruto(rangos);
			String mesesLetra = CalculoTiempo.leeMeses(meses);

			selectRow(0);
			table.setWidget(listaTiempoServicioDeclarado.size(), 3, new HTML(
					"<b>Totalización</b>"));
			table.setWidget(listaTiempoServicioDeclarado.size(), 4, new HTML(
					"<b>" + mesesLetra + "</b>"));
		}
	}

	private void selectRow(int i) {
		// if (row < 0 || listaTiempoServicioReconocido.size() == 0) {
		// return;
		// };

		// selectedItem = listaTiempoServicioReconocido.get(row);

		// if (selectedItem == null) {
		// return;
		// }

		if (row == listaTiempoServicioDeclarado.size()) { // la ultima fila es
															// un total)
			return;
		}
		styleRow(selectedRow, false);
		styleRow(row, true);

		selectedRow = row;

		// if (listener != null) {
		// listener.onItemSelected(selectedItem);
		// }

	}

	private void styleRow(int selectedRow2, boolean selected) {
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
