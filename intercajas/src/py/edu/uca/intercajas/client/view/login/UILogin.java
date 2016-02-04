package py.edu.uca.intercajas.client.view.login;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
		txtPassword.setText("323698");
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
		grid = new Grid(3,2);
		
		grid.setWidget(0, 0, lblNombre);
		grid.setWidget(0, 1, txtUsuario);
		grid.setWidget(1, 0, lblPassword);
		grid.setWidget(1, 1, txtPassword);
		grid.setWidget(2, 1, btnLogin);
		
		initWidget(grid);

	}
	
	
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
	
		
}
