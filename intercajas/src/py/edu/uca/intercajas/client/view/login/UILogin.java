package py.edu.uca.intercajas.client.view.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
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
	private Button btnLogin;
	private Label lblNombre;
	protected TextBox txtUsuario;
	private Label lblPassword;
	protected PasswordTextBox txtPassword;
	
	
	public UILogin() {
		initComponents();
		widgetListener();
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
		
		panel = new FlowPanel();
		panel.add(lblNombre);
		panel.add(txtUsuario);
		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(btnLogin);
		initWidget(panel);

	}
	
	
	public void login() {
		
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
