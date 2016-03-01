package py.edu.uca.intercajas.client.view.login;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;


public class UICambioContrasena extends UIBase implements ClickHandler {	

	
	private Grid grid;
	private Button btnLogin;
	private Button btnCancel;
	private Label lblNombre;
	protected TextBox txtUsuario;
	private Label lblPassword;
	private Label lblNewPassword;
	private Label lblNewPasswordConfirm;
	protected PasswordTextBox txtPassword;
	protected PasswordTextBox txtNewPassword;
	protected PasswordTextBox txtNewPasswordConfirm;
	UserDTO usuario;
	
	public UICambioContrasena(UserDTO usuario) {
		initComponents();
		widgetListener();
		
		this.usuario = usuario;
		
		titulo = "Cambiar mi contraseña";
		txtUsuario.setText(usuario.getName());
		
	}
	
	
	private void widgetListener() {
		btnLogin.addClickHandler(this);	
	}


	private void initComponents(){

		lblNombre = new Label("Usuario");
		lblPassword = new Label("Contraseña actual");
		lblNewPassword = new Label("Nueva contraseña");
		lblNewPasswordConfirm = new Label("Confirme contraseña");
		btnLogin = new Button("Cambiar");
		txtPassword = new PasswordTextBox();
		txtNewPassword = new PasswordTextBox();
		txtNewPasswordConfirm = new PasswordTextBox();
		txtUsuario = new TextBox();
		txtUsuario.setEnabled(false);
		btnCancel = new Button("Cancelar");
		grid = new Grid(5,2);
		
		grid.setWidget(0, 0, lblNombre);
		grid.setWidget(0, 1, txtUsuario);
		grid.setWidget(1, 0, lblPassword);
		grid.setWidget(1, 1, txtPassword);
		grid.setWidget(2, 0, lblNewPassword);
		grid.setWidget(2, 1, txtNewPassword);
		grid.setWidget(3, 0, lblNewPasswordConfirm);
		grid.setWidget(3, 1, txtNewPasswordConfirm);
		grid.setWidget(4, 0, btnCancel);
		grid.setWidget(4, 1, btnLogin);

		
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				close();
			}
		});
		
		initWidget(grid);

	}
	
	
	public void login() {
		
		if (!formularioValido()) return;
		
		LoginService.Util.getInstance().changePassword(usuario.getName(), txtPassword.getValue(), txtNewPassword.getValue(), new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				close();
				AppUtils.logout();
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					Window.alert("Contraseña cambiada!");
					close();
					AppUtils.logout();
				} else {
					Window.alert("Contraseña anterior no valida!");
					return;
				}
						
			}
		} );				
		
		
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
	
	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete las siguientes informaciones solicitadas para crear la concesion de beneficio");
		
		if (txtNewPassword.getValue().length() < 5) {
			vf.addError("La contaseña debe contener al menos cinco caracteres");
		}
		if (!txtNewPassword.getValue().equals(txtNewPasswordConfirm.getValue())) {
			vf.addError("Verifique los datos introducidos!");
		}
		
		return vf.esValido();
		
	}	
}
