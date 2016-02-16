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



import py.edu.uca.intercajas.shared.UIBase;
import py.edu.uca.intercajas.shared.entity.Destino;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;

/**
 * This application demonstrates how to construct a relatively complex user
 * interface, similar to many common email readers. It has no back-end,
 * populating its components with hard-coded data.
 */
public class Mail extends UIBase {

  interface Binder extends UiBinder<DockLayoutPanel, Mail> { }

  interface GlobalResources extends ClientBundle {
    @NotStrict
    @Source("global.css")
    CssResource css();
  }

  private static final Binder binder = GWT.create(Binder.class);

//  @UiField TopPanel topPanel;
  @UiField(provided=true) MailList mailList;
  @UiField MailDetail mailDetail;
//  @UiField Shortcuts shortcuts;
//  @UiField SimplePanel main;

  public Mail() {
	 
		mailList = new MailList(MailList.Modo.Entrada);
		initWidget(binder.createAndBindUi(this));
		init();
  }
  
  /**
   * This method constructs the application user interface by instantiating
   * controls and hooking up event handler.
   */
  public void init() {
    // Inject global styles.
    GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();

    // Create the UI defined in Mail.ui.xml.
//    DockLayoutPanel outer = binder.createAndBindUi(this);

    // Get rid of scrollbars, and clear out the window's built-in margin,
    // because we want to take advantage of the entire client area.
    Window.enableScrolling(false);
    Window.setMargin("0px");

    // Special-case stuff to make topPanel overhang a bit.
//    Element topElem = outer.getWidgetContainerElement(topPanel);
//    topElem.getStyle().setZIndex(2);
//    topElem.getStyle().setOverflow(Overflow.VISIBLE);

    // Listen for item selection, displaying the currently-selected item in
    // the detail area.
    mailList.setListener(new MailList.Listener() {
      public void onItemSelected(Destino item) {
        mailDetail.setItem(item);
      }
    });

    // Add the outer panel to the RootLayoutPanel, so that it will be
    // displayed.
    
//    RootLayoutPanel root = RootLayoutPanel.get();
//    root.add(outer);
    
  }
  
  public void mostrarEntrada() {
	  mailList.modo = MailList.Modo.Entrada;
	  mailList.update();
  }
  
  public void mostrarFiniquitado() {
	  mailList.modo = MailList.Modo.Finquitado;
	  mailList.update();
  }
  
}
