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

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.beneficiario.ListaBeneficiarios.Listener;
import py.edu.uca.intercajas.client.beneficiario.events.BeneficiarioChangedEvent;
import py.edu.uca.intercajas.shared.entity.Beneficiario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.OptionalFieldEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
    IsEditor<OptionalFieldEditor<Beneficiario, NameLabel>> {

  interface Binder extends UiBinder<Widget, BeneficiarioSelector> {
  }

  HandlerRegistration r;
  
  @UiField
  Button choose;

  @UiField
  Button clear;

  @UiField
  NameLabel nameLabel;
  
  Beneficiario beneficiario;

  private final OptionalFieldEditor<Beneficiario, NameLabel> editor;
//  private final GestionBeneficiario gestionBeneficiario;

  
	public interface Listener {
		void onSelected(Beneficiario beneficiarioSelected);
	}
 
	Listener listener;
  
  public BeneficiarioSelector() {
	
	
	  
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    editor = OptionalFieldEditor.of(nameLabel);
    clear.setVisible(false);
  }

  public OptionalFieldEditor<Beneficiario, NameLabel> asEditor() {
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
    
    r = AppUtils.EVENT_BUS.addHandler(BeneficiarioChangedEvent.TYPE, new BeneficiarioChangedEvent.Handler() {
		@Override
		public void selected(Beneficiario beneficiarioSelected) {
			r.removeHandler();
			//TODO esto hay que arreglar
			setValue(beneficiarioSelected);
		}
	});
    
    
    new ListaBeneficiarios(10).mostrarDialog();
//    factory.schoolCalendarRequest().getRandomPerson().to(
//        new Receiver<PersonProxy>() {
//          @Override
//          public void onSuccess(PersonProxy response) {
//            setValue(response);
//            setEnabled(true);
//          }
//        }).fire();
  }

  @UiHandler("clear")
  void onClear(ClickEvent event) {
    setValue(null);
  }

  /**
   * This method is not called by the Editor framework.
   */
  private void setValue(Beneficiario beneficiario) {
    //editor.setValue(beneficiario);
	this.beneficiario = beneficiario;
	nameLabel.setVisible(beneficiario !=  null);
	if (beneficiario != null) {
		nameLabel.setTexto(beneficiario.toString());
	} 
    
	if (listener!=null) {
		listener.onSelected(beneficiario);
	}
  }
  
  public Beneficiario getBeneficiario() {
	  return this.beneficiario;
  }
  
  public void setClearVisible(boolean visible) {
	  clear.setVisible(visible);
  }
  public void setListener(Listener listener) {
		this.listener = listener;
  }

}
