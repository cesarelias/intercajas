package py.edu.uca.intercajas.client.menumail;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RefreshMailEvent extends GwtEvent<RefreshMailEvent.Handler> {

	public static Type<Handler> TYPE = new Type<Handler>();

	Long beneficiarioIdFilter;
	Long cajaIdFilter;
	Date fechaDesde;
	Date fechaHasta;
	
	public interface Handler extends EventHandler {
		void refresh(Long beneficiarioIdFilter, Long cajaIdFilter, Date fechaDesde, Date fechaHasta);
	}
	
	public  RefreshMailEvent(Long beneficiarioIdFilter, Long cajaIdFilter, Date fechaDesde, Date fechaHasta) {
		this.beneficiarioIdFilter = beneficiarioIdFilter;
		this.cajaIdFilter = cajaIdFilter;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
	}

	public RefreshMailEvent() {
		
	}
	
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.refresh(beneficiarioIdFilter, cajaIdFilter, fechaDesde, fechaHasta);
	}

}
