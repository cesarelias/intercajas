package py.edu.uca.intercajas.client;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.finiquito.UIDenegar;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Denegado;
import py.edu.uca.intercajas.shared.entity.Finiquito;
import py.edu.uca.intercajas.shared.entity.Concedido;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Intercajas implements EntryPoint {

	public void onModuleLoad() {

		Defaults.setDateFormat(null);

		String sessionID = Cookies.getCookie("sid");
		if (sessionID == null) {
			AppUtils.mostrarLogin();
		} else {
			checkWithServerIfSessionIdIsStillLegal(sessionID);
		}

	}

	private void checkWithServerIfSessionIdIsStillLegal(String sessionID) {
		
		LoginService.Util.getInstance().loginFromSessionServer(
				new AsyncCallback<UserDTO>() {
					@Override
					public void onFailure(Throwable caught) {
						AppUtils.mostrarLogin();
					}

					@Override
					public void onSuccess(UserDTO result) {
						if (result == null) {
							AppUtils.mostrarLogin();
						} else {
							if (result.getLoggedIn()) {
								LoginService.Util.currentUser = result;
								AppUtils.mostrarMenuPrincipal();
							} else {
								AppUtils.mostrarLogin();
							}
						}
					}
				});
	}

}