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

import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.finiquito.UIConceder;
import py.edu.uca.intercajas.client.finiquito.UIDenegar;
import py.edu.uca.intercajas.client.mensaje.UIAnular;
import py.edu.uca.intercajas.client.mensaje.UIAutorizar;
import py.edu.uca.intercajas.client.tiemposervicio.TiempoServicioReconocidoEditorWorkFlow;
import py.edu.uca.intercajas.shared.ConsultaEstadoMensaje;
import py.edu.uca.intercajas.shared.ConsultaEstadoSolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.Adjunto;
import py.edu.uca.intercajas.shared.entity.Destino;
import py.edu.uca.intercajas.shared.entity.Mensaje;
import py.edu.uca.intercajas.shared.entity.Solicitud;
import py.edu.uca.intercajas.shared.entity.SolicitudBeneficiario;
import py.edu.uca.intercajas.shared.entity.Usuario;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
    if (item.getMensaje().getRemitente() == null) {
    	sender.setText("SistemaIntercajas");
    } else {
    	sender.setText(item.getMensaje().getRemitente().getSiglas());
    }
    //TODO falta
    recipient.setText(item.getDestinatario().getSiglas());

    // WARNING: For the purposes of this demo, we're using HTML directly, on
    // the assumption that the "server" would have appropriately scrubbed the
    // HTML. Failure to do so would open your application to XSS attacks.
    //body.setHTML(item.body);


   	acciones(item);
    
    //Obtenemos los adjuntos del mensaje
    BeneficiarioService.Util.get().adjuntoFindByMensajeId(item.getMensaje().getId(), new MethodCallback<List<Adjunto>>() {
		@Override
		public void onSuccess(Method method, List<Adjunto> response) {
			panelAdjuntos.clear();
			for (Adjunto a : response) {
				panelAdjuntos.add(createDownloadLink(a.getTipo().toString(), a.getRutaArchivo(), a.getNombreArchivo()));
			}
		}
		@Override
		public void onFailure(Method method, Throwable exception) {
			new UIErrorRestDialog(method, exception);
		}
		
	});

    
    
	//Actualizamos el cuerpo del mensaje
    if (item.getMensaje().getCuerpo() == null || item.getMensaje().getCuerpo().isEmpty()) {
    	body.setHTML("");
    } else {
    	body.setHTML(new SafeHtmlBuilder().appendEscapedLines(item.getMensaje().getCuerpo()).toSafeHtml());
    }

    
    
  }
  
  
  public Anchor createDownloadLink(final String tipo, final String rutaArchivo, final String nombreArchivo) {



	    Anchor link = new Anchor(tipo);
////		link.setStyleName(style.item());
//
//		// Add a click handler that displays a ContactPopup when it is clicked.
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				final String url = GWT.getModuleBaseURL() + "servlet.gupld?show=" + nombreInternoArchivo;
				Window.open( "servlet.gupld?show=" + rutaArchivo + nombreArchivo, "_blank", "status=0,toolbar=0,menubar=0,location=0");
				
			}
		});

		link.getElement().getStyle().setProperty("margin", "4px");
		link.getElement().getStyle().setProperty("color", "blue");
		return link;
  }
  
  
  public void acciones(final Destino item) {
	  
		opciones.clear();
		optionsButtons.clear();
	    
	  final Solicitud s = item.getMensaje().getSolicitud();
	  
	  //Para el gestor, con estadoDestino Nuevo
	  if (LoginService.Util.currentUser.getTipo() == Usuario.Tipo.Gestor) {
		  
		  
		  BeneficiarioService.Util.get().consultEstadoMensaje(item.getMensaje().getId(), new MethodCallback<ConsultaEstadoMensaje>() {
			@Override
			public void onSuccess(Method method, ConsultaEstadoMensaje response) {
				
				
				  if (item.getMensaje().getAsunto() == Mensaje.Asunto.NuevaSolicitud && response.getEstadoRTS() == ConsultaEstadoMensaje.EstadoRTS.SIN_RTS) {
					  
					  Button rts = new Button("Reconocer Tiempo de Servicio");
					  rts.addClickHandler(new ClickHandler() {
						  @Override
						  public void onClick(ClickEvent event) {
							  TiempoServicioReconocidoEditorWorkFlow b = new TiempoServicioReconocidoEditorWorkFlow(item);
//							  b.titulo = "Reconocimiento de Tiempo de Servicio";
							  b.mostrarDialog();
						  }
					  });
					  optionsButtons.add(rts);
					  
				  //} else if (response.getSolicitud().getEstado() == Solicitud.Estado.ConAntiguedad && response.getEstado() == CajaDeclarada.Estado.ConAntiguedad && response.getAutorizado() ) {
				  } else if (item.getMensaje().getAsunto() == Mensaje.Asunto.NuevaSolicitud && response.getEstadoRTS() == ConsultaEstadoMensaje.EstadoRTS.CON_RTS_SIN_AUTORIZACION) {
					  optionsButtons.add(new Label("Con Reconocimiento de Tiempo de Servicio - Esperando autorizacion para envio"));
				  } else if (item.getMensaje().getAsunto() == Mensaje.Asunto.TotalizacionTiempoServicio) {
					  
					  try {
						
					  final VerticalPanel vp = new VerticalPanel();
					  HorizontalPanel hp = null;
					  
					  for (ConsultaEstadoSolicitudBeneficiario c : response.getListaConsultaEstadoSolicitudBeneficiario()) {
						  
						  final SolicitudBeneficiario sb = c.getSolicitudBeneficiario();
						  
						  hp = new HorizontalPanel();
						  if (c.getEstado() == ConsultaEstadoSolicitudBeneficiario.Estado.Concedido && c.isAutorizado()) {
							  hp.add(new Label(sb.getBeneficiario().toString() + " con beneficio concedido, no mas acciones diponibles"));
						  } else if (c.getEstado() == ConsultaEstadoSolicitudBeneficiario.Estado.Denegado && c.isAutorizado()) {
							  hp.add(new Label(sb.getBeneficiario().toString() + " con beneficio denegado, no mas acciones diponibles"));
						  } else if (c.getEstado() == ConsultaEstadoSolicitudBeneficiario.Estado.Concedido && !c.isAutorizado()) {
							  hp.add(new Label(sb.getBeneficiario().toString() + " en espera de autorizacion para conceder beneficio, no mas acciones diponibles"));
						  } else if (c.getEstado() == ConsultaEstadoSolicitudBeneficiario.Estado.Denegado && !c.isAutorizado()) {
							  hp.add(new Label(sb.getBeneficiario().toString() + " en espera de autorizacion para denegar beneficio, no mas acciones diponibles"));
						  } else if (c.getEstado() == ConsultaEstadoSolicitudBeneficiario.Estado.Pendiente) {
							  
							  Anchor conceder = new Anchor("conceder");
									  conceder.addClickHandler(new ClickHandler() {
										  @Override
										  public void onClick(ClickEvent event) {
											  UIConceder c = new UIConceder(sb, item);
											  c.titulo = "Conceder beneficio a " + sb.getBeneficiario().toString();
											  c.mostrarDialog();
										  }
									  });
							  
							  Anchor denegar = new Anchor("denegar");
									  denegar.addClickHandler(new ClickHandler() {
										  @Override
										  public void onClick(ClickEvent event) {
											  UIDenegar d = new UIDenegar(sb, item);
											  d.titulo = "Denegar beneficio a " + sb.getBeneficiario().getNombres() + " " + sb.getBeneficiario().getApellidos();
											  d.mostrarDialog();
										  }
									  });
							  
							  conceder.getElement().getStyle().setProperty("margin", "4px");
							  conceder.getElement().getStyle().setProperty("color", "blue");
							  denegar.getElement().getStyle().setProperty("margin", "4px");
							  denegar.getElement().getStyle().setProperty("color", "blue");
							  hp.add(conceder);
							  hp.add(new Label("o"));
							  hp.add(denegar);
							  hp.add(new Label(" el beneficio a " + sb.getBeneficiario().getNombres() + " " + sb.getBeneficiario().getApellidos()));
						  }
						  vp.add(hp);
					  }
					  
					  optionsButtons.add(vp);
					  } catch (Exception e) {
						  Window.alert(e.getMessage());
					  }
				  } else {
					  optionsButtons.add(new Label("No hay acciones disponibles"));
				  }
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}
		});
		  
		  
	  } else if (LoginService.Util.currentUser.getTipo() == Usuario.Tipo.Superior && item.getMensaje().getEstado() == Mensaje.Estado.Pendiente ) {
		  
		  final HorizontalPanel hp = new HorizontalPanel();
		  
		  final Anchor autorizar = new Anchor("autorizar y enviar");
		  autorizar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UIAutorizar uiautorizar = new UIAutorizar(item);
				uiautorizar.mostrarDialog();
			}
		  });
		  
		  
		  final Anchor anular = new Anchor("anular");
		  anular.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UIAnular uianular = new UIAnular(item.getMensaje(), item);
				uianular.mostrarDialog();
				
			}
		  });

		  autorizar.getElement().getStyle().setProperty("margin", "4px");
		  autorizar.getElement().getStyle().setProperty("color", "blue");
		  
		  anular.getElement().getStyle().setProperty("margin", "4px");
		  anular.getElement().getStyle().setProperty("color", "blue");
		  
		  hp.add(autorizar);
		  hp.add(new Label("o"));
		  hp.add(anular);
		  
		  optionsButtons.add(hp);
		  
		  
	  } else if (LoginService.Util.currentUser.getTipo() == Usuario.Tipo.Administrador) {
		  
		  final Anchor anular = new Anchor("anular solicitud");
		  anular.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				UIAnular uianular = new UIAnular(item.getMensaje(), item);
//				uianular.mostrarDialog();
				Window.alert("falta la UI de anular solicitud");
				
			}
		  });
		  
		  anular.getElement().getStyle().setProperty("margin", "4px");
		  anular.getElement().getStyle().setProperty("color", "blue");
		  
		  optionsButtons.add(anular);
		  
	  } else {
		  optionsButtons.add(new Label("No hay acciones disponibles"));
	  }

	    // Add advanced options to form in a disclosure panel
	    DisclosurePanel optionPanel = new DisclosurePanel("Acciones");
	    optionPanel.setAnimationEnabled(true);
//	    optionPanel.ensureDebugId("cwDisclosurePanel");
	    optionPanel.setContent(optionsButtons);

	    opciones.add(optionPanel);


  }
  
  public void clear() {
	  subject.setText("--");
	  sender.setText("--");
	  recipient.setText("--");
	  body.setHTML("");
	  panelAdjuntos.clear();
	  optionsButtons.clear();
	  opciones.clear();
	  
  }
  
}
