package py.edu.uca.intercajas.client.view.login;

import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionUsuario;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.client.requestfactory.UsuarioProxy;
import py.edu.uca.intercajas.client.view.menu.UIMenuImpl;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;



public class UILoginImpl extends UILogin{	
	
	private static  EventBus EVENTBUS = new SimpleEventBus();
	private static  FactoryGestion FACTORY  = GWT.create(FactoryGestion.class);
	
	public UILoginImpl() {
		super();
		txtUsuario.setText("cc");
		txtPassword.setText("cc");
	}

	@Override
	public void login() {
		
		FACTORY.initialize(EVENTBUS);
		ContextGestionUsuario context = FACTORY.contextGestionUsuario();
		
		context.login(this.txtUsuario.getText(), this.txtPassword.getText()).fire(new Receiver<UsuarioProxy>() {

			@Override
			public void onSuccess(UsuarioProxy response) {
				// TODO Auto-generated method stub
//				Window.alert("Bienvenido a Intercajas " + 	response.getNombre());
//				String idSession = RequestFactoryServlet.getThreadLocalRequest().getSession().getId();
//				response.setIdSesion(idSession);
				irSesion(response);
			}

			public void onFailure(ServerFailure error) {
				// TODO Auto-generated method stub
				Window.alert("Usuario o contraseña no valido");
			}
			
		});

	}
	
//	@Override
	public void irSesion(UsuarioProxy usuario) {
		// TODO Auto-generated method stub
		if(usuario!=null ) { //&& !usuario.getIdSesion().isEmpty() ) { //&& usuario.getIdSesion()!=null){
//			this.uiHome.getUiSesion().setBeanUsuario(usuario);
//			this.uiHome.getUiSesion().getUiAdminCosto().getUiCosto().cargarTabla();
//			this.uiHome.getPnlViews().showWidget(2);
			UIMenuImpl uiMenu = new UIMenuImpl();
			uiMenu.getLblBienvenido().setText("Bienvenido!! " + usuario.getNombre());
			RootPanel.get().remove(0);
//			RootPanel.get().add(uiMenu);
			new MenuMail();
			
			
		}
	}
	
}
