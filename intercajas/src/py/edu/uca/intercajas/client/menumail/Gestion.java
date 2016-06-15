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

import py.edu.uca.intercajas.client.Intercajas;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios;
import py.edu.uca.intercajas.client.caja.ListaCajas;
import py.edu.uca.intercajas.client.report.UIAuditoria;
import py.edu.uca.intercajas.client.report.UITramitesMiCaja;
import py.edu.uca.intercajas.client.report.UITramitesPorCaja;
import py.edu.uca.intercajas.client.report.UITramitesSolicitud;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;
import py.edu.uca.intercajas.client.view.login.ListaUsuarios;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * A component that displays a list of contacts.
 */
public class Gestion extends Composite {

	
	private final SimpleEventBus eventBus = new SimpleEventBus();
	private static final Logger log = Logger.getLogger(Intercajas.class.getName());
	
	/**
	 * Simple data structure representing a itemMenu.
	 */
	private static class ItemMenu {
		public String nombre;

		public ItemMenu(String nombre) {
			this.nombre = nombre;
		}
	}

	interface Binder extends UiBinder<Widget, Gestion> {
	}

	interface Style extends CssResource {
		String item();
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	FlowPanel panel;
	@UiField
	Style style;

	public Gestion() {
		
		initWidget(binder.createAndBindUi(this));
		
		if (LoginService.Util.currentUser.getTipo() == Usuario.Tipo.Administrador) {
			addItemUsuarios(new ItemMenu("Gestión de Usuarios"));
			addItemBeneficiario(new ItemMenu("Gestión de Beneficiario"));
			addItemInformeAuditoria(new ItemMenu("Informe Auditoria"));
			addItemReporteTramitesPorCaja(new ItemMenu("Reporte Trámites por caja"));
			addItemReporteTramitesSolicitud(new ItemMenu("Reporte Trámites solicitud"));
			addItemCaja(new ItemMenu("Gestión Caja de Jubilacion"));
		} else if (LoginService.Util.currentUser.getTipo() == Usuario.Tipo.Gestor) {
			addItemSolicitudTitular(new ItemMenu("Nueva Solicitud"));
			addItemBeneficiario(new ItemMenu("Gestión de Beneficiario"));
			addItemReporteTramitesMiCaja(new ItemMenu("Reporte Trámites"));
			
		} else if (LoginService.Util.currentUser.getTipo() == Usuario.Tipo.Superior) {
//			addItemAnularSolicitud(new ItemMenu("Anular Solicitud"));
		}
		
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
	
	private void addItemUsuarios(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListaUsuarios l = new ListaUsuarios(10);
				l.mostrarDialog();
			}
		});
	}	
	
	//TODO esto ya no se usa, no se puede anular una solicitud.
	private void addItemAnularSolicitud(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("esto falta desarrollar");
			}
		});
	}
	
	private void addItemInformeAuditoria(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new UIAuditoria().mostrarDialog();
			}
		});
	}	

	private void addItemReporteTramitesPorCaja(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new UITramitesPorCaja().mostrarDialog();
			}
		});
	}	

	private void addItemReporteTramitesMiCaja(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new UITramitesMiCaja().mostrarDialog();
			}
		});
	}	
	

	private void addItemReporteTramitesSolicitud(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new UITramitesSolicitud().mostrarDialog();
			}
		});
	}		
	
	private void addItemCaja(final ItemMenu itemMenu) {
		final Anchor link = new Anchor(itemMenu.nombre);
		link.setStyleName(style.item());

		panel.add(link);

		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new ListaCajas(10).mostrarDialog();
			}
		});
	}	

}
