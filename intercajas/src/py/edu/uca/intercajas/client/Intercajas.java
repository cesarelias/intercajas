package py.edu.uca.intercajas.client;

import org.fusesource.restygwt.client.Defaults;

import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
								AppUtils.mostrarMenuPrincipal();
							} else {
								AppUtils.mostrarLogin();
							}
						}
					}
				});
	}

}