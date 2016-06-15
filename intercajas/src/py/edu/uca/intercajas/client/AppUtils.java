package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.client.caja.ListaCajas;
import py.edu.uca.intercajas.client.menumail.Mail;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.report.UITramitesMiCaja;
import py.edu.uca.intercajas.client.report.UITramitesPorCaja;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;
import py.edu.uca.intercajas.client.tiemposervicio.ListaEmpleadores;
import py.edu.uca.intercajas.client.view.login.UILogin;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppUtils {

	public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
	public static DateTimeFormat dateFormat = DateTimeFormat
			.getFormat("dd/MM/yyyy");

	public static void mostrarMenuPrincipal() {
		RootLayoutPanel.get().clear();
		new MenuMail();
		Mail m = AppUtils.Util.getMail();
		m.mostrar();
		m.mostrarMisPendientes();
		// m.mostrarEntrada(); //Eso no hace falta, se muestra en forma
		// predeterminada al crear Mail, de echo, crea problemas al volver a
		// mostrar

	}

	public static void mostrarLogin() {
		AppUtils.Util.clearMail();
		RootLayoutPanel.get().clear();
		UILogin login = new UILogin();
		RootLayoutPanel.get().add(login);

	}

	public static void logout() {
		LoginService.Util.getInstance().logout(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
			}
		});
		mostrarLogin();
	}

	// Singleton de mail.
	public static class Util {
		public static UserDTO user;
		private static Mail mail;

		public static Mail getMail() {
			if (mail == null) {
				mail = new Mail();
			}
			return mail;
		}
		
		public static void clearMail() {
			mail = null;
		}
		

		// ESTO NO ANDA AUN jaja.. mejor usar del LoginService
		public static UserDTO getCurrentUser() {
			if (user == null) {
				user = new UserDTO();
			}
			return user;
		}

	}

	public static boolean esFecha(DateBox fecha) {
		try {
			if (fecha == null) {
				return false;
			}
			dateFormat.parseStrict(fecha.getTextBox().getValue());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public static boolean esFecha(String fecha) {
		try {
			if (fecha == null) {
				return false;
			}
			dateFormat.parseStrict(fecha);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	

	public native static boolean isValidEmail(String email) /*-{
		var reg1 = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; // not valid
		var reg2 = /^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/; // valid
		return !reg1.test(email) && reg2.test(email);
	}-*/;

}
