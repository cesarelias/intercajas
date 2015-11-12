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


import sun.security.jca.GetInstance;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.thirdparty.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This application demonstrates how to construct a relatively complex user
 * interface, similar to many common email readers. It has no back-end,
 * populating its components with hard-coded data.
 */
public class Mail extends Composite {

  interface Binder extends UiBinder<DockLayoutPanel, Mail> { }

  interface GlobalResources extends ClientBundle {
    @NotStrict
    @Source("global.css")
    CssResource css();
  }

  private static final Binder binder = GWT.create(Binder.class);

//  @UiField TopPanel topPanel;
  @UiField MailList mailList;
  @UiField MailDetail mailDetail;
//  @UiField Shortcuts shortcuts;
//  @UiField SimplePanel main;

  public Mail() {
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
      public void onItemSelected(MailItem item) {
        mailDetail.setItem(item);
        //Window.alert("se ejecuto el listener: body: " + item.body);
      }
    });

    // Add the outer panel to the RootLayoutPanel, so that it will be
    // displayed.
    
//    RootLayoutPanel root = RootLayoutPanel.get();
//    root.add(outer);
    
  }
  
  public void mostrar() {
    if (MenuMail.getMain().getWidget() == null || !(MenuMail.getMain().getWidget() instanceof Mail )) {
    	MenuMail.getMain().setWidget(this);
    }
  }
}