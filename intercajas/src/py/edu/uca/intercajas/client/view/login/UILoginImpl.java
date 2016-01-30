package py.edu.uca.intercajas.client.view.login;

import java.util.Date;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.menumail.MenuMail;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditorWorkFlow;
import py.edu.uca.intercajas.shared.UserDTO;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;



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
//			new MenuMail(null);
			
			
		}
	}
	
}
