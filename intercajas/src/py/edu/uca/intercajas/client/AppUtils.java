package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.client.menumail.Mail;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.view.login.UICambioContrasena;
import py.edu.uca.intercajas.client.view.login.UIEditarUsuario;
import py.edu.uca.intercajas.client.view.login.UILogin;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Caja;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppUtils {
	
	
	    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
	    
	    public static void mostrarMenuPrincipal() {
			RootLayoutPanel.get().clear();
            new MenuMail();
            new Mail().mostrar();
            
            
    		//test
            new UIEditarUsuario(null).mostrarDialog();
    		//

	    }
	    
	    public static  void mostrarLogin() {
	    	RootLayoutPanel.get().clear();
	    	DecoratorPanel panel = new DecoratorPanel();
			UILogin login = new UILogin();
			panel.add(login);
			RootLayoutPanel.get().add(panel);
			
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
	    
	    
}
