package py.edu.uca.intercajas.client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import py.edu.uca.intercajas.client.menumail.Mail;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditorWorkFlow;
import py.edu.uca.intercajas.client.view.login.UILoginImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppUtils {
	    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
	    
	    public static void mostrarMenuPrincipal() {
			RootLayoutPanel.get().clear();
            new MenuMail();
            new Mail().mostrar();
            try {
            	Window.alert("a");
            	TiempoServicioReconocidoEditorWorkFlow b = new TiempoServicioReconocidoEditorWorkFlow();
            	Window.alert("b");
				  b.titulo = "Reconocimiento de Tiempo de Servicio";
				  Window.alert("c");
				  b.mostrarDialog();
				  Window.alert("d");
				  b.create();
				  Window.alert("e");
				  
            } catch (Exception e) {
            	Window.alert(e.getMessage());
            }
	    }
	    
	    public static  void mostrarLogin() {
	    	RootLayoutPanel.get().clear();
	    	DecoratorPanel panel = new DecoratorPanel();
			UILoginImpl login = new UILoginImpl();
			panel.add(login);
			RootLayoutPanel.get().add(panel);
	    }
	    
	    
}
