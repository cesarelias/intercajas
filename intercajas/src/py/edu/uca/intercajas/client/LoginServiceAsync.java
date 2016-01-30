package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoginServiceAsync {
	

	void changePassword(String name, String newPassword,
			AsyncCallback<Boolean> callback);

	void loginFromSessionServer(AsyncCallback<UserDTO> callback);

	void logout(AsyncCallback<Void> callback);

	void loginServer(String name, String password,
			AsyncCallback<UserDTO> callback);
	
}
