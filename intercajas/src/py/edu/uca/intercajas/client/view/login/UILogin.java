package py.edu.uca.intercajas.client.view.login;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.TextCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;


public class UILogin extends Composite implements ClickHandler {	

	
//	public Login(UIHome uiHome){
//		this.uiHome=uiHome;
//		initComponents();
//		style();
//		widgetListener();		
//	}
	
	
	private FlowPanel panel;
	private Grid grid;
	private Button btnLogin;
	private Label lblNombre;
	protected TextBox txtUsuario;
	private Label lblPassword;
	protected PasswordTextBox txtPassword;

	
	public UILogin() {
		initComponents();
		widgetListener();
		
		txtUsuario.setText("cesar");
		txtPassword.setText("123456");
	}
	
	
	private void widgetListener() {
		btnLogin.addClickHandler(this);	
	}


	private void initComponents(){

		
		lblNombre = new Label("Usuario");
		lblPassword = new Label("Contraseña");
		btnLogin = new Button("Iniciar Sesion");
		txtPassword = new PasswordTextBox();
		txtUsuario = new TextBox();
		grid = new Grid(3,2);
		
		try {
		grid.setWidget(0, 0, lblNombre);
		grid.setWidget(0, 1, txtUsuario);
		grid.setWidget(1, 0, lblPassword);
		grid.setWidget(1, 1, txtPassword);
		grid.setWidget(2, 1, btnLogin);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		grid = new Grid(4,2);
		
		grid.setWidget(0, 0, lblNombre);
		grid.setWidget(0, 1, txtUsuario);
		grid.setWidget(1, 0, lblPassword);
		grid.setWidget(1, 1, txtPassword);
		grid.setWidget(2, 1, btnLogin);

		grid.setWidget(3, 1, panelRecuperarContrasena());
		
		
		
	    
	    
		
		initWidget(grid);

	}
	
	
	public void login() {
		
//		btnLogin.setEnabled(false);
//		BeneficiarioService.Util.get().reportTest(txtUsuario.getValue(), new TextCallback() {
//
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				Window.alert(exception.getMessage());
//				btnLogin.setEnabled(true);		
//			}
//
//			@Override
//			public void onSuccess(Method method, String response) {
//				btnLogin.setEnabled(true);
//				Window.open( "servlet.gupld?show=" + "/reports/" + response, "_blank", "status=0,toolbar=0,menubar=0,location=0");
//			}
//	});
		
		
		LoginService.Util.getInstance().loginServer(txtUsuario.getValue(), txtPassword.getValue(), new AsyncCallback<UserDTO>()
                {
                    @Override
                    public void onSuccess(UserDTO result)
                    {
                        if (result != null && result.getLoggedIn())
                        {
                            String sessionID = result.getSessionId();
                            Cookies.setCookie("sid", sessionID, result.getExpire(), null, "/", false);
                            LoginService.Util.currentUser = result;
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

	public void irCuenta() {
//		uiHome.getPnlViews().showWidget(0);
	}	

//	public void irSesion(UsuarioProxy bean) {
//		
//	}


	@Override
	public void onClick(ClickEvent event) {
		login();
	}
	

	public DisclosurePanel panelRecuperarContrasena() {
		
		final DisclosurePanel optionPanel = new DisclosurePanel("Recuperar contraseña");
		final Anchor recuperar = new Anchor("Recuperar");
		final TextBox correo = new TextBox();
		final FlexTable f = new FlexTable();
		
		f.setText(0, 0, "Correo");
		f.setWidget(0, 1, correo);
		f.setWidget(0, 2, recuperar);
		
		recuperar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				BeneficiarioService.Util.get().restablecerContrasena(txtUsuario.getValue(), correo.getValue(), new MethodCallback<Void>() {
					@Override
					public void onSuccess(Method method, Void response) {
						Window.alert("La nueva contraseña fue enviada a " + correo.getValue());
						optionPanel.setOpen(false);
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						new UIErrorRestDialog(method, exception);
					}
				});
			}
		});
		
	    optionPanel.setAnimationEnabled(true);
	    optionPanel.setContent(f);
	    
	    return optionPanel;

	}
	
}
