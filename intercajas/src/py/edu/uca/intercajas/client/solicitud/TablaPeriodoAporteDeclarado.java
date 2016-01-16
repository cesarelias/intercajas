package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.List;

import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.server.entity.PeriodoAporteDeclarado;
import py.edu.uca.intercajas.shared.UIBase;

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

public class TablaPeriodoAporteDeclarado extends UIBase {

	private static TablaPeriodoAporteDeclaradoUiBinder uiBinder = GWT
			.create(TablaPeriodoAporteDeclaradoUiBinder.class);

	interface TablaPeriodoAporteDeclaradoUiBinder extends
			UiBinder<Widget, TablaPeriodoAporteDeclarado> {
	}
	
	interface TableResources extends DataGrid.Resources {
//	    @Override
//	    @Source(value = {DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css"})
//	    DataGrid.Style dataGridStyle();
	  }

	@UiField(provided = true) DataGrid<PeriodoAporteDeclarado> table;
	
	HandlerRegistration h;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
	
	ListDataProvider<PeriodoAporteDeclarado> model;
	List<PeriodoAporteDeclarado> listaPeriodoAporteDeclarado = new ArrayList<PeriodoAporteDeclarado>();
	
	int row;
	
	private class ColCaja extends Column<PeriodoAporteDeclarado, String> {
		public ColCaja() {
			super(new TextCell());
		}
		@Override
		public String getValue(PeriodoAporteDeclarado object) {
			return object.getCaja().getSiglas();
		}
	}

	private class ColInicio extends Column<PeriodoAporteDeclarado, String> {
		public ColInicio() {
			super(new TextCell());
		}
		@Override
		public String getValue(PeriodoAporteDeclarado object) {
			return dateFormat.format(object.getInicio()).toString();
		}
	}

	private class ColFin extends Column<PeriodoAporteDeclarado, String> {
		public ColFin() {
			super(new TextCell());
		}
		@Override
		public String getValue(PeriodoAporteDeclarado object) {
			return dateFormat.format(object.getFin()).toString();
		}
	}
	
	private class ColLugar extends Column<PeriodoAporteDeclarado, String> {
	    public ColLugar() {
	      super(new TextCell());
	    }
	    @Override
	    public String getValue(PeriodoAporteDeclarado object) {
	      return object.getLugar();
	    }
	}
	

	
	public TablaPeriodoAporteDeclarado(SimpleEventBus eventBus) {
		
		this.eventBus = eventBus;
		
		model = new ListDataProvider<PeriodoAporteDeclarado>();
		
	    table = new DataGrid<PeriodoAporteDeclarado>(0,GWT.<TableResources> create(TableResources.class));

	    Column<PeriodoAporteDeclarado, String> colCaja = new ColCaja();
	    table.addColumn(colCaja, "Caja");
	    table.setColumnWidth(colCaja, "8ex");
	    
	    Column<PeriodoAporteDeclarado, String> colLugar = new ColLugar();
	    table.addColumn(colLugar, "Lugar");
	    table.setColumnWidth(colLugar, "25ex");

	    Column<PeriodoAporteDeclarado, String> colInicio = new ColInicio();
	    table.addColumn(colInicio, "Inicio");
	    table.setColumnWidth(colInicio, "11ex");

	    Column<PeriodoAporteDeclarado, String> colFin = new ColFin();
	    table.addColumn(colFin, "Fin");
	    table.setColumnWidth(colFin, "11ex");

	    
	    //boton remove
	    Column<PeriodoAporteDeclarado, String> removeColumn = new Column<PeriodoAporteDeclarado, String>(new ButtonCell()) {
	      @Override
	      public String getValue(PeriodoAporteDeclarado object) {
	        // The value to display in the button.
	        return "X";
	      }
	    };
	    
	    Column<PeriodoAporteDeclarado, String> editColumn = new Column<PeriodoAporteDeclarado, String>(new ButtonCell()) {
		      @Override
		      public String getValue(PeriodoAporteDeclarado object) {
		        // The value to display in the button.
		        return "..";
		      }
		};
		    
	    table.addColumn(removeColumn, "Eliminar");
	    table.setColumnWidth(removeColumn, "8ex");

	    table.addColumn(editColumn, "Editar");
	    table.setColumnWidth(editColumn, "8ex");
	    
	    
	    // set a FieldUpdater on the Column to be notified of clicks.
	    removeColumn.setFieldUpdater(new FieldUpdater<PeriodoAporteDeclarado, String>() {
	      public void update(int index, PeriodoAporteDeclarado object, String value) {
	        // Value is the button value.  Object is the row object.
	        listaPeriodoAporteDeclarado.remove(index);
	        refreshTable();
	      }
	    });
	    
	    editColumn.setFieldUpdater(new FieldUpdater<PeriodoAporteDeclarado, String>() {
		      public void update(int index, PeriodoAporteDeclarado object, String value) {
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

	    h	 = eventBus.addHandler(PeriodoAporteDeclaradoChangedEvent.TYPE, new PeriodoAporteDeclaradoChangedEvent.Handler() {
			@Override
			public void selected(PeriodoAporteDeclarado periodoAporteDeclaradoProxy) {
				listaPeriodoAporteDeclarado.add(periodoAporteDeclaradoProxy);
				refreshTable();
				h.removeHandler();
			}
		});
		PeriodoAporteDeclaradoEditor pp = new PeriodoAporteDeclaradoEditor(eventBus, null);
		pp.title = "Nuevo periodo de aporte declarado";
		pp.mostrarDialog();

	}
	
	
	void onEdit(PeriodoAporteDeclarado periodoAporteDeclaradoProxy) {
		
	    h = eventBus.addHandler(PeriodoAporteDeclaradoChangedEvent.TYPE, new PeriodoAporteDeclaradoChangedEvent.Handler() {
			@Override
			public void selected(PeriodoAporteDeclarado periodoAporteDeclaradoProxy) {
				listaPeriodoAporteDeclarado.set(row, periodoAporteDeclaradoProxy);
//				listaPeriodoAporteDeclarado.remove(idx);
//				listaPeriodoAporteDeclarado.add(periodoAporteDeclaradoProxy);
				h.removeHandler();
				refreshTable();
			}
		});
		
		PeriodoAporteDeclaradoEditor pp = new PeriodoAporteDeclaradoEditor(eventBus, periodoAporteDeclaradoProxy);
		pp.title = "Editar periodo de aporte declarado";
		pp.mostrarDialog();
		
	}
	
	void refreshTable() {
		table.setRowData(listaPeriodoAporteDeclarado);
		table.setRowCount(listaPeriodoAporteDeclarado.size());
	}
	

}
