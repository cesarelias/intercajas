/*
 * Copyright 2010 Google Inc.
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
package py.edu.uca.intercajas.client.beneficiario;

import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.FactoryGestion;
import py.edu.uca.intercajas.server.ejb.GestionBeneficiario;
import py.edu.uca.intercajas.server.entity.Beneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.OptionalFieldEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * This demonstrates how an editor can be constructed to handle optional fields.
 * The Person domain object's mentor property is initially <code>null</code>.
 * This type delegates editing control to an instance of the
 * {@link OptionalValueEditor} adapter class.
 */
public class BeneficiarioSelector extends Composite implements
    IsEditor<OptionalFieldEditor<BeneficiarioProxy, NameLabel>> {

  interface Binder extends UiBinder<Widget, BeneficiarioSelector> {
  }

  SimpleEventBus eventBus;
  FactoryGestion requestFactory;
  HandlerRegistration r;
  
  @UiField
  Button choose;

//  @UiField
//  Button clear;

  @UiField
  NameLabel nameLabel;

  private final OptionalFieldEditor<BeneficiarioProxy, NameLabel> editor;
//  private final GestionBeneficiario gestionBeneficiario;

  public BeneficiarioSelector(SimpleEventBus eventBus, FactoryGestion requestFactory) {
	this.eventBus = eventBus;
	this.requestFactory = requestFactory;
	
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    editor = OptionalFieldEditor.of(nameLabel);
  }

  public OptionalFieldEditor<BeneficiarioProxy, NameLabel> asEditor() {
    return editor;
  }

  public void setEnabled(boolean enabled) {
    choose.setEnabled(enabled);
//    clear.setEnabled(enabled);
  }

  @Override
  protected void onUnload() {
    nameLabel.cancelSubscription();
  }

  @UiHandler("choose")
  void onChoose(ClickEvent event) {
//    setEnabled(false);
    
    r = eventBus.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
		@Override
		public void selected(Beneficiario beneficiarioSelected) {
			r.removeHandler();
			//TODO esto hay que arreglar
			setValue(beneficiarioSelected);
		}
	});
    
    new ListaBeneficiarios(eventBus, requestFactory, 10).mostrarDialog();
//    factory.schoolCalendarRequest().getRandomPerson().to(
//        new Receiver<PersonProxy>() {
//          @Override
//          public void onSuccess(PersonProxy response) {
//            setValue(response);
//            setEnabled(true);
//          }
//        }).fire();
  }

//  @UiHandler("clear")
//  void onClear(ClickEvent event) {
//    setValue(null);
//  }

  /**
   * This method is not called by the Editor framework.
   */
  private void setValue(Beneficiario beneficiario) {
    //editor.setValue(beneficiario);
    nameLabel.setVisible(beneficiario != null);
    nameLabel.setTexto(beneficiario.getNombres() + ", " + beneficiario.getApellidos());
  }
}
