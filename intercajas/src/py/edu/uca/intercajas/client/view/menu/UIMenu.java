package py.edu.uca.intercajas.client.view.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;


public class UIMenu extends Composite implements ClickHandler {	

	
//	public Login(UIHome uiHome){
//		this.uiHome=uiHome;
//		initComponents();
//		style();
//		widgetListener();		
//	}
	
	
	private FlowPanel panel;
	private Label lblBienvenido;
	private Button btnTest;
	
	
	public Label getLblBienvenido() {
		return lblBienvenido;
	}


	public void setLblBienvenido(Label lblBienvenido) {
		this.lblBienvenido = lblBienvenido;
	}


	public UIMenu() {
		initComponents();
		widgetListener();
	}
	
	
	private void widgetListener() {
		btnTest.addClickHandler(this);	
	}


	private void initComponents(){

		lblBienvenido = new Label("Bienvenido");
		btnTest = new Button("Test me");
		 
		
		panel = new FlowPanel();
		panel.add(lblBienvenido);
		panel.add(btnTest);
		initWidget(panel);

	}
	
	@Override
	public void onClick(ClickEvent event) {
		opcion1();
	}
	
	public void opcion1() {
		
	}
	
		
}
