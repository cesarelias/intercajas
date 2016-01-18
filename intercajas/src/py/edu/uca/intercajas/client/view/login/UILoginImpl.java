package py.edu.uca.intercajas.client.view.login;

import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;



public class UILoginImpl extends UILogin{	
	
	public UILoginImpl() {
		super();
		txtUsuario.setText("cc");
		txtPassword.setText("cc");
	}

	@Override
	public void login() {
		
		
//		context.login(this.txtUsuario.getText(), this.txtPassword.getText()).fire(new Receiver<UsuarioProxy>() {
//
//			@Override
//			public void onSuccess(UsuarioProxy response) {
//				// TODO Auto-generated method stub
////				Window.alert("Bienvenido a Intercajas " + 	response.getNombre());
////				String idSession = RequestFactoryServlet.getThreadLocalRequest().getSession().getId();
////				response.setIdSesion(idSession);
//				irSesion(response);
//			}
//
//			public void onFailure(ServerFailure error) {
//				// TODO Auto-generated method stub
//				Window.alert("Usuario o contraseña no valido");
//			}
//			
//		});

	}
	
//	@Override
	public void irSesion(Usuario usuario) {
		// TODO Auto-generated method stub
		if(usuario!=null ) { //&& !usuario.getIdSesion().isEmpty() ) { //&& usuario.getIdSesion()!=null){
//			this.uiHome.getUiSesion().setBeanUsuario(usuario);
//			this.uiHome.getUiSesion().getUiAdminCosto().getUiCosto().cargarTabla();
//			this.uiHome.getPnlViews().showWidget(2);
//			UIMenuImpl uiMenu = new UIMenuImpl();
//			uiMenu.getLblBienvenido().setText("Bienvenido!! " + usuario.getNombre());
			RootPanel.get().remove(0);
//			RootPanel.get().add(uiMenu);
			new MenuMail();
			
			
		}
	}
	
}
