package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.client.menumail.Mail;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;
import py.edu.uca.intercajas.client.solicitud.TablaSolicitudBeneficiario;
import py.edu.uca.intercajas.client.tiemposervicio.ListaEmpleadores;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditor;
import py.edu.uca.intercajas.client.view.login.ListaUsuarios;
import py.edu.uca.intercajas.client.view.login.UILogin;
import py.edu.uca.intercajas.shared.NuevoReconocimientoTiempoServicio;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppUtils {
	
	    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
	    
	    public static void mostrarMenuPrincipal() {
			RootLayoutPanel.get().clear();
            new MenuMail();
            Mail m = AppUtils.Util.getMail();
            m.mostrar();
            m.mostrarMisPendientes();
//            m.mostrarEntrada(); //Eso no hace falta, se muestra en forma predeterminada al crear Mail, de echo, crea problemas al volver a mostrar
            
    		//test
            try {
            	
//            	ListaEmpleadores l = new ListaEmpleadores(10);
//            	l.mostrarDialog();
            	
//            	TiempoServicioReconocidoEditor n = new TiempoServicioReconocidoEditor(null);
//            	n.titulo = "Nuevo quete";
//            	n.mostrarDialog();
            	
//            	SolicitudTitularEditorWorkFlow s = new SolicitudTitularEditorWorkFlow();
//            	s.titulo = "Nueva solicitud";
//            	s.create();
//            	s.mostrarDialog();
//            	
            	
            }catch (Exception e) {
            	Window.alert(e.getMessage());
            }
    		
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
	    

	    //Singleton de mail.
	    public static class Util
	    {
	        public static UserDTO user;
	        private static Mail mail;
	 
	        public static Mail getMail()
	        {
	            if (mail == null)
	            {
	                mail= new Mail();
	            }
	            return mail;
	        }

		    //ESTO NO ANDA AUN jaja.. mejor usar del LoginService
	        public static UserDTO getCurrentUser()
	        {
	            if (user == null)
	            {
	            	user= new UserDTO();
	            }
	            return user;
	        }
	        
	    }
	    
	    
	    
}
