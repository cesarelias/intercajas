/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package py.edu.uca.intercajas.client.menumail;

import java.util.logging.Logger;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.Intercajas;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;
import py.edu.uca.intercajas.client.view.login.UICambioContrasena;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * A component that displays a list of contacts.
 */
public class Solicitudes extends Composite {

	
	private final SimpleEventBus eventBus = new SimpleEventBus();
	private static final Logger log = Logger.getLogger(Intercajas.class.getName());
	
	/**
	 * Simple data structure representing a itemMenu.
	 */
	private static class ItemMenu {
		public String nombre;
		public String titulo;

		public ItemMenu(String nombre, String titulo) {
			this.nombre = nombre;
			this.titulo = titulo;
		}
	}

	interface Binder extends UiBinder<Widget, Solicitudes> {
	}

	interface Style extends CssResource {
		String item();
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	FlowPanel panel;
	@UiField
	Style style;

	public Solicitudes() {
		
		initWidget(binder.createAndBindUi(this));
		
		addItemSolicitudTitular(new ItemMenu("Solicitud Titular", "Solicitud Titular"));
		addItemBeneficiario(new ItemMenu("Beneficiario", "Beneficiario"));
		addItemCambioContrasena(new ItemMenu("Cambiar mi contraseña","Cambiar Contraseña"));
		
//		addItem(new ItemMenu("Solicitud Derechohabiente",
//				"Solicitud Derechohabiente"));

	}

	private void addItemSolicitudTitular(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//new UISolicitudTitular().mostrar();
				  SolicitudTitularEditorWorkFlow b = new SolicitudTitularEditorWorkFlow();
				  b.titulo = "Nueva solicitud titular";
				  b.mostrarDialog();
				  b.create();
			}
		});
	}
	
	private void addItemBeneficiario(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListaBeneficiarios beneficiarios = 	new ListaBeneficiarios(10);
				beneficiarios.mostrarDialog();
				beneficiarios.hideSelectButton();
			}
		});

	}	
	
	private void addItemCambioContrasena(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserDTO>() {
					@Override
					public void onSuccess(UserDTO result) {
						new UICambioContrasena(result).mostrarDialog();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						//TODO mejorar esto
						Window.alert(caught.getMessage());
					}
				});				
				
			}
		});
	}	
	
}
