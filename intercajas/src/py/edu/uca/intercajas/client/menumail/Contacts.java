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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A component that displays a list of contacts.
 */
public class Contacts extends Composite {

  /**
   * Simple data structure representing a contact.
   */
  private static class Contact {
    public String email;
    public String name;

    public Contact(String name, String email) {
      this.name = name;
      this.email = email;
    }
  }

  /**
   * A simple popup that displays a contact's information.
   */
  static class ContactPopup extends PopupPanel {
    @UiTemplate("ContactPopup.ui.xml")
    interface Binder extends UiBinder<Widget, ContactPopup> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField Element nameDiv;
    @UiField Element emailDiv;

    public ContactPopup(Contact contact) {
      // The popup's constructor's argument is a boolean specifying that it
      // auto-close itself when the user clicks outside of it.
      super(true);
      add(binder.createAndBindUi(this));

      nameDiv.setInnerText(contact.name);
      emailDiv.setInnerText(contact.email);
    }
  }

  interface Binder extends UiBinder<Widget, Contacts> { }
  interface Style extends CssResource {
    String item();
  }

  private static final Binder binder = GWT.create(Binder.class);

  private Contact[] contacts = new Contact[] {
      new Contact("Dina Miglio", "dina@example.com"),
      new Contact("Cesar Sanabria", "cesar@example.com"),
      new Contact("Graciela Perez", "graciela@example.com"),
      new Contact("Lia Belen", "lia@example.com"),
      new Contact("Elias Mateo", "elias@example.com")};

  @UiField ComplexPanel panel;
  @UiField Style style;

  public Contacts() {
    initWidget(binder.createAndBindUi(this));

    Label usuariosLabel = new Label("Usuarios");
    usuariosLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
    
    panel.add(usuariosLabel);
    // Add all the contacts to the list.
    for (int i = 0; i < contacts.length; ++i) {
      addContact(contacts[i]);
    }
  }

  private void addContact(final Contact contact) {
    final Anchor link = new Anchor(contact.name);
    link.setStyleName(style.item());
    panel.add(link);

    // Add a click handler that displays a ContactPopup when it is clicked.
    link.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        ContactPopup popup = new ContactPopup(contact);
        int left = link.getAbsoluteLeft() + 14;
        int top = link.getAbsoluteTop() + 14;
        popup.setPopupPosition(left, top);
        popup.show();
      }
    });
  }
  

  
}
