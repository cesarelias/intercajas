package py.edu.uca.intercajas.client.solicitud;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import py.edu.uca.intercajas.client.requestfactory.ContextGestionSolicitud;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.client.requestfactory.PeriodoAporteDeclaradoProxy;
import py.edu.uca.intercajas.client.solicitud.events.PeriodoAporteDeclaradoChangedEvent;
import py.edu.uca.intercajas.shared.UIBase;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.HasData;
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

	@UiField(provided = true) DataGrid<PeriodoAporteDeclaradoProxy> table;
	
	FactoryGestion factoryGestion;
	ContextGestionSolicitud ctx;
	HandlerRegistration h;
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
	
	ListDataProvider<PeriodoAporteDeclaradoProxy> model;
	List<PeriodoAporteDeclaradoProxy> listaPeriodoAporteDeclarado = new ArrayList<PeriodoAporteDeclaradoProxy>();
	
	int row;
	
	private class ColCaja extends Column<PeriodoAporteDeclaradoProxy, String> {
		public ColCaja() {
			super(new TextCell());
		}
		@Override
		public String getValue(PeriodoAporteDeclaradoProxy object) {
			return object.getCaja().getSiglas();
		}
	}

	private class ColInicio extends Column<PeriodoAporteDeclaradoProxy, String> {
		public ColInicio() {
			super(new TextCell());
		}
		@Override
		public String getValue(PeriodoAporteDeclaradoProxy object) {
			return dateFormat.format(object.getInicio()).toString();
		}
	}

	private class ColFin extends Column<PeriodoAporteDeclaradoProxy, String> {
		public ColFin() {
			super(new TextCell());
		}
		@Override
		public String getValue(PeriodoAporteDeclaradoProxy object) {
			return dateFormat.format(object.getFin()).toString();
		}
	}
	
	private class ColLugar extends Column<PeriodoAporteDeclaradoProxy, String> {
	    public ColLugar() {
	      super(new TextCell());
	    }
	    @Override
	    public String getValue(PeriodoAporteDeclaradoProxy object) {
	      return object.getLugar();
	    }
	}
	

	
	public TablaPeriodoAporteDeclarado(SimpleEventBus eventBus, FactoryGestion factoryGestion, ContextGestionSolicitud context) {
		
//		this.factoryGestion = factoryGestion;
		ctx = context;
		this.eventBus = eventBus;
		this.factoryGestion = factoryGestion;
		
		model = new ListDataProvider<PeriodoAporteDeclaradoProxy>();
		
	    table = new DataGrid<PeriodoAporteDeclaradoProxy>(0,GWT.<TableResources> create(TableResources.class));

	    Column<PeriodoAporteDeclaradoProxy, String> colCaja = new ColCaja();
	    table.addColumn(colCaja, "Caja");
	    table.setColumnWidth(colCaja, "8ex");
	    
	    Column<PeriodoAporteDeclaradoProxy, String> colLugar = new ColLugar();
	    table.addColumn(colLugar, "Lugar");
	    table.setColumnWidth(colLugar, "25ex");

	    Column<PeriodoAporteDeclaradoProxy, String> colInicio = new ColInicio();
	    table.addColumn(colInicio, "Inicio");
	    table.setColumnWidth(colInicio, "11ex");

	    Column<PeriodoAporteDeclaradoProxy, String> colFin = new ColFin();
	    table.addColumn(colFin, "Fin");
	    table.setColumnWidth(colFin, "11ex");

	    
	    //boton remove
	    Column<PeriodoAporteDeclaradoProxy, String> removeColumn = new Column<PeriodoAporteDeclaradoProxy, String>(new ButtonCell()) {
	      @Override
	      public String getValue(PeriodoAporteDeclaradoProxy object) {
	        // The value to display in the button.
	        return "X";
	      }
	    };
	    
	    Column<PeriodoAporteDeclaradoProxy, String> editColumn = new Column<PeriodoAporteDeclaradoProxy, String>(new ButtonCell()) {
		      @Override
		      public String getValue(PeriodoAporteDeclaradoProxy object) {
		        // The value to display in the button.
		        return "..";
		      }
		};
		    
	    table.addColumn(removeColumn, "Eliminar");
	    table.setColumnWidth(removeColumn, "8ex");

	    table.addColumn(editColumn, "Editar");
	    table.setColumnWidth(editColumn, "8ex");
	    
	    
	    // set a FieldUpdater on the Column to be notified of clicks.
	    removeColumn.setFieldUpdater(new FieldUpdater<PeriodoAporteDeclaradoProxy, String>() {
	      public void update(int index, PeriodoAporteDeclaradoProxy object, String value) {
	        // Value is the button value.  Object is the row object.
	        listaPeriodoAporteDeclarado.remove(index);
	        refreshTable();
	      }
	    });
	    
	    editColumn.setFieldUpdater(new FieldUpdater<PeriodoAporteDeclaradoProxy, String>() {
		      public void update(int index, PeriodoAporteDeclaradoProxy object, String value) {
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

	    h = eventBus.addHandler(PeriodoAporteDeclaradoChangedEvent.TYPE, new PeriodoAporteDeclaradoChangedEvent.Handler() {
			@Override
			public void selected(PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy) {
				listaPeriodoAporteDeclarado.add(periodoAporteDeclaradoProxy);
				refreshTable();
				h.removeHandler();
			}
		});
		PeriodoAporteDeclaradoEditor pp = new PeriodoAporteDeclaradoEditor(eventBus, factoryGestion, ctx, null);
		pp.title = "Nuevo periodo de aporte declarado";
		pp.mostrarDialog();

	}
	
	
	void onEdit(PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy) {
		
	    h = eventBus.addHandler(PeriodoAporteDeclaradoChangedEvent.TYPE, new PeriodoAporteDeclaradoChangedEvent.Handler() {
			@Override
			public void selected(PeriodoAporteDeclaradoProxy periodoAporteDeclaradoProxy) {
				listaPeriodoAporteDeclarado.set(row, periodoAporteDeclaradoProxy);
//				listaPeriodoAporteDeclarado.remove(idx);
//				listaPeriodoAporteDeclarado.add(periodoAporteDeclaradoProxy);
				refreshTable();
				h.removeHandler();
			}
		});
		
		PeriodoAporteDeclaradoEditor pp = new PeriodoAporteDeclaradoEditor(eventBus, factoryGestion, ctx, periodoAporteDeclaradoProxy);
		pp.title = "Editar periodo de aporte declarado";
		pp.eventBus = eventBus;
		pp.mostrarDialog();
		
	}
	
	void refreshTable() {
		table.setRowData(listaPeriodoAporteDeclarado);
		table.setRowCount(listaPeriodoAporteDeclarado.size());
	}
	

}
