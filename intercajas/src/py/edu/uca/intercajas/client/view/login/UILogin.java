package py.edu.uca.intercajas.client.view.login;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.UIValidarFormulario;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


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
		
//		txtUsuario.setText("cesar");
//		txtPassword.setText("123456");
	}
	
	
	private void widgetListener() {
		btnLogin.addClickHandler(this);	
	}


	private void initComponents(){
//
//		<g:HorizontalPanel width="100%" height="100%">
//		 <g:cell horizontalAlignment="ALIGN_CENTER" verticalAlignment="ALIGN_MIDDLE">
//		  <g:Label>Hello Center</g:Label>
//		 </g:cell>
//		</g:HorizontalPanel>
//		
	
		
		
		lblNombre = new Label("Usuario");
		lblPassword = new Label("Contraseña");
		btnLogin = new Button("Iniciar Sesión");
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
		
		DecoratorPanel dp = new DecoratorPanel();
		
		dp.add(grid);

		VerticalPanel logos = new VerticalPanel();
		logos.setSize("100%", "100%");
		logos.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		logos.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		logos.add(new HTML("<font color='blue' size='6'>Bienvenido al sistema intercajas</font>"));
		
		
		VerticalPanel v = new VerticalPanel();//Esto es el panel central, donde se centra todo
	    v.setWidth("100%");
	    v.setHeight("100%");
	    v.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//	    v.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    v.add(dp);

	    DockLayoutPanel dockPanel = new DockLayoutPanel(Unit.PX);
		dockPanel.addNorth(logos, 200);
		dockPanel.add(v);
		
		initWidget(dockPanel);

	}
	
	
	public void login() {
		
		if (!formularioValido()) return;
		
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

	public boolean formularioValido() {
		
		UIValidarFormulario vf = new UIValidarFormulario("Favor complete los siguientes datos");

	
		if (txtUsuario.getValue().length() == 0){
			vf.addError("Ingrese un nombre de usuario");
		}
		
		if (txtPassword.getValue().length() == 0){
			vf.addError("Ingrese una contraseña");
		}

		
		
		return vf.esValido();
		
	}
}
