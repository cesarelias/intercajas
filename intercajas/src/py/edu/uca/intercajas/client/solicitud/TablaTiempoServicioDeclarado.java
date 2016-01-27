package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.TiempoServicioDeclarado;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class TablaTiempoServicioDeclarado extends UIBase {

	private static TablaTiempoServicioDeclaradoUiBinder uiBinder = GWT
			.create(TablaTiempoServicioDeclaradoUiBinder.class);

	interface TablaTiempoServicioDeclaradoUiBinder extends
			UiBinder<Widget, TablaTiempoServicioDeclarado> {
	}
	
	interface TableResources extends DataGrid.Resources {
//	    @Override
//	    @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
//	    DataGrid.Style dataGridStyle();
	  }

	@UiField(provided = true) DataGrid<TiempoServicioDeclarado> table;
	
	HandlerRegistration h;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
	
	ListDataProvider<TiempoServicioDeclarado> model;
	List<TiempoServicioDeclarado> listaTiempoServicioDeclarado = new ArrayList<TiempoServicioDeclarado>();
	
	int row;
	
	private class ColCaja extends Column<TiempoServicioDeclarado, String> {
		public ColCaja() {
			super(new TextCell());
		}
		@Override
		public String getValue(TiempoServicioDeclarado object) {
			return object.getCaja().getSiglas();
		}
	}

	private class ColInicio extends Column<TiempoServicioDeclarado, String> {
		public ColInicio() {
			super(new TextCell());
		}
		@Override
		public String getValue(TiempoServicioDeclarado object) {
			return dateFormat.format(object.getInicio()).toString();
		}
	}

	private class ColFin extends Column<TiempoServicioDeclarado, String> {
		public ColFin() {
			super(new TextCell());
		}
		@Override
		public String getValue(TiempoServicioDeclarado object) {
			return dateFormat.format(object.getFin()).toString();
		}
	}
	
	private class ColLugar extends Column<TiempoServicioDeclarado, String> {
	    public ColLugar() {
	      super(new TextCell());
	    }
	    @Override
	    public String getValue(TiempoServicioDeclarado object) {
	      return object.getLugar();
	    }
	}
	

	
	public TablaTiempoServicioDeclarado() {
		
		model = new ListDataProvider<TiempoServicioDeclarado>();
		
	    table = new DataGrid<TiempoServicioDeclarado>(0,GWT.<TableResources> create(TableResources.class));

	    Column<TiempoServicioDeclarado, String> colCaja = new ColCaja();
	    table.addColumn(colCaja, "Caja");
	    table.setColumnWidth(colCaja, "8ex");
	    
	    Column<TiempoServicioDeclarado, String> colLugar = new ColLugar();
	    table.addColumn(colLugar, "Lugar");
	    table.setColumnWidth(colLugar, "25ex");

	    Column<TiempoServicioDeclarado, String> colInicio = new ColInicio();
	    table.addColumn(colInicio, "Inicio");
	    table.setColumnWidth(colInicio, "11ex");

	    Column<TiempoServicioDeclarado, String> colFin = new ColFin();
	    table.addColumn(colFin, "Fin");
	    table.setColumnWidth(colFin, "11ex");

	    
	    //boton remove
	    Column<TiempoServicioDeclarado, String> removeColumn = new Column<TiempoServicioDeclarado, String>(new ButtonCell()) {
	      @Override
	      public String getValue(TiempoServicioDeclarado object) {
	        // The value to display in the button.
	        return "X";
	      }
	    };
	    
	    Column<TiempoServicioDeclarado, String> editColumn = new Column<TiempoServicioDeclarado, String>(new ButtonCell()) {
		      @Override
		      public String getValue(TiempoServicioDeclarado object) {
		        // The value to display in the button.
		        return "..";
		      }
		};
		    
	    table.addColumn(removeColumn, "Eliminar");
	    table.setColumnWidth(removeColumn, "8ex");

	    table.addColumn(editColumn, "Editar");
	    table.setColumnWidth(editColumn, "8ex");
	    
	    
	    // set a FieldUpdater on the Column to be notified of clicks.
	    removeColumn.setFieldUpdater(new FieldUpdater<TiempoServicioDeclarado, String>() {
	      public void update(int index, TiempoServicioDeclarado object, String value) {
	        // Value is the button value.  Object is the row object.
	    	  listaTiempoServicioDeclarado.remove(index);
	        refreshTable();
	      }
	    });
	    
	    editColumn.setFieldUpdater(new FieldUpdater<TiempoServicioDeclarado, String>() {
		      public void update(int index, TiempoServicioDeclarado object, String value) {
		        // Value is the button value.  Object is the row object.
		        //listaPeriodoAporteDeclarado.remove(index);
		    	row = index;
		    	onEdit(object);  
		      }
		    });
	    
	    //fin boton remove


//	    table.setSelectionModel(selectionModel);
//	    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
	    table.setEmptyTableWidget(new Label("Vacio"));
	    
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	@UiHandler("create")
	void onCreate(ClickEvent event) {

	    h	 = AppUtils.EVENT_BUS.addHandler(PeriodoAporteDeclaradoChangedEvent.TYPE, new PeriodoAporteDeclaradoChangedEvent.Handler() {
			@Override
			public void selected(TiempoServicioDeclarado periodoAporteDeclaradoProxy) {
				listaTiempoServicioDeclarado.add(periodoAporteDeclaradoProxy);
				refreshTable();
				h.removeHandler();
			}
		});
		TiempoServicioDeclaradoEditor pp = new TiempoServicioDeclaradoEditor(null);
		pp.titulo = "Nuevo tiempo de servicio declarado";
		pp.mostrarDialog();

	}
	
	
	void onEdit(TiempoServicioDeclarado periodoAporteDeclaradoProxy) {
		
	    h = AppUtils.EVENT_BUS.addHandler(PeriodoAporteDeclaradoChangedEvent.TYPE, new PeriodoAporteDeclaradoChangedEvent.Handler() {
			@Override
			public void selected(TiempoServicioDeclarado periodoAporteDeclaradoProxy) {
				listaTiempoServicioDeclarado.set(row, periodoAporteDeclaradoProxy);
//				listaPeriodoAporteDeclarado.remove(idx);
//				listaPeriodoAporteDeclarado.add(periodoAporteDeclaradoProxy);
				h.removeHandler();
				refreshTable();
			}
		});
		
		TiempoServicioDeclaradoEditor pp = new TiempoServicioDeclaradoEditor(periodoAporteDeclaradoProxy);
		pp.titulo = "Editar tiempo de servicio declarado";
		pp.mostrarDialog();
		
	}
	
	void refreshTable() {
		table.setRowData(listaTiempoServicioDeclarado);
		table.setRowCount(listaTiempoServicioDeclarado.size());
	}
	
}
