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

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import javafx.scene.layout.FlowPane;
import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.solicitud.SolicitudTitularEditorWorkFlow;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditorWorkFlow;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.CajaDeclarada;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.Solicitud.Estado;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite for displaying the details of an email message.
 */
public class MailDetail extends ResizeComposite {

  interface Binder extends UiBinder<Widget, MailDetail> { }
  private static final Binder binder = GWT.create(Binder.class);

  @UiField Label subject;
  @UiField Label sender;
  @UiField Label recipient;
  @UiField HTML body;
  @UiField HorizontalPanel panelAdjuntos;
  @UiField FlowPanel opciones;

  HorizontalPanel optionsButtons = new HorizontalPanel();
  
  public MailDetail() {
    initWidget(binder.createAndBindUi(this));
//    panelAdjuntos.getElement().getStyle().setPadding(20, Unit.PX);
  }

  public void setItem(Destino item) {
    subject.setText(item.getMensaje().getReferencia());
    sender.setText(item.getMensaje().getRemitente().getSiglas());
    //TODO falta
    recipient.setText(item.getDestinatario().getSiglas());

    // WARNING: For the purposes of this demo, we're using HTML directly, on
    // the assumption that the "server" would have appropriately scrubbed the
    // HTML. Failure to do so would open your application to XSS attacks.
    //body.setHTML(item.body);

    
    
    //Obtenemos los adjuntos del mensaje
    BeneficiarioService.Util.get().adjuntoFindByMensajeId(item.getMensaje().getId(), new MethodCallback<List<Adjunto>>() {
		@Override
		public void onSuccess(Method method, List<Adjunto> response) {
			panelAdjuntos.clear();
			for (Adjunto a : response) {
				panelAdjuntos.add(createDownloadLink(a.getNombreArchivo(), a.getRutaArchivo()));
			}
		}
		@Override
		public void onFailure(Method method, Throwable exception) {
			// TODO Auto-generated method stub
		}
		
	});

    
    final Solicitud s = item.getMensaje().getSolicitud();
    opciones.clear();
    optionsButtons.clear();
    
    //Obtenemos la cajaDeclarada correspondiente al usuario
    BeneficiarioService.Util.get().findCajaDeclaraadBySolicitudIdAndCurrentUser(item.getMensaje().getSolicitud().getId(), new MethodCallback<CajaDeclarada>() {
		@Override
		public void onFailure(Method method, Throwable exception) {
			//TODO mejorar esto
			Window.alert(exception.getMessage());
		}

		@Override
		public void onSuccess(Method method, CajaDeclarada response) {
			if (response.getEstado() == CajaDeclarada.Estado.Nuevo) {
				
			    Button rts = new Button("Reconocer Tiempo de Servicio");
			    rts.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
								TiempoServicioReconocidoEditorWorkFlow b = new TiempoServicioReconocidoEditorWorkFlow(s);
								b.titulo = "Reconocimiento de Tiempo de Servicio";
								b.mostrarDialog();
								b.create();
					}
				});
			    optionsButtons.add(rts);

			} else 	if (response.getEstado() == CajaDeclarada.Estado.ConAntiguedad) {
				
			    Button fin = new Button("Finiquitar");
			    fin.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.alert("aqui abrimos la venta de finiquito, que aun no existe! :-)");
					}
				});
			    optionsButtons.add(fin);

			}

			
			
			
		}
	});
    
    
    

    // Add advanced options to form in a disclosure panel
    DisclosurePanel optionPanel = new DisclosurePanel("Acciones");
    optionPanel.setAnimationEnabled(true);
//    optionPanel.ensureDebugId("cwDisclosurePanel");
    optionPanel.setContent(optionsButtons);

    opciones.add(optionPanel);
    
    
	//Actualizamos el cuerpo del mensaje
    if (item.getMensaje().getCuerpo() == null || item.getMensaje().getCuerpo().isEmpty()) {
    	body.setHTML("");
    } else {
    	body.setHTML(new SafeHtmlBuilder().appendEscapedLines(item.getMensaje().getCuerpo()).toSafeHtml());
    }

    
    
    
  }
  
  
  public Anchor createDownloadLink(final String nombreArchivo, final String nombreInternoArchivo) {
	  
	    Anchor link = new Anchor(nombreArchivo);
////		link.setStyleName(style.item());
//
//		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				final String url = GWT.getModuleBaseURL() + "servlet.gupld?show=" + nombreInternoArchivo;
				Window.open( "servlet.gupld?show=" + nombreInternoArchivo, "_blank", "status=0,toolbar=0,menubar=0,location=0");
				
			}
		});

		link.getElement().getStyle().setProperty("margin", "4px");
		link.getElement().getStyle().setProperty("color", "blue");
		return link;
  }
}
