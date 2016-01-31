package py.edu.uca.intercajas.client.view.login;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class UILoginImpl extends UILogin{	
	
	public UILoginImpl() {
		super();
		
		txtUsuario.setText("cesar");
		txtPassword.setText("323698");
		
	}

	@Override
	public void login() {
		
		LoginService.Util.getInstance().loginServer(txtUsuario.getValue(), txtPassword.getValue(), new AsyncCallback<UserDTO>()
                {
                    @Override
                    public void onSuccess(UserDTO result)
                    {
                        if (result != null && result.getLoggedIn())
                        {
                            String sessionID = result.getSessionId();
                            Cookies.setCookie("sid", sessionID, result.getExpire(), null, "/", false);
                            AppUtils.mostrarMenuPrincipal();
                        } else
                        {
                            Window.alert("Usuario / Contraseña invalido!");
                        }
 
                    }
 
                    @Override
                    public void onFailure(Throwable caught)
                    {
                        Window.alert("Ocurrio un error al verificar el Usuario y Contraseña");
                    }
                });

	}
	
}
