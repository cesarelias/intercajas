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


import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import py.edu.uca.intercajas.client.AppUtils;
import py.edu.uca.intercajas.client.BeneficiarioService;
import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.client.UIErrorRestDialog;
import py.edu.uca.intercajas.client.beneficiario.BeneficiarioEditorWorkFlow;
import py.edu.uca.intercajas.client.beneficiario.BeneficiarioSelector;
import py.edu.uca.intercajas.client.beneficiario.BeneficiarioSelector.Listener;
import py.edu.uca.intercajas.client.caja.ListaCajas;
import py.edu.uca.intercajas.shared.entity.Beneficiario;
import py.edu.uca.intercajas.shared.entity.Caja;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.SimpleKeyProvider;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * A tree displaying a set of email folders.
 */
public class Mailboxes extends Composite {

  /**
   * Specifies the images that will be bundled for this Composite and specify
   * that tree's images should also be included in the same bundle.
   */
	
	SimpleEventBus eventBus;
	
  public interface Images extends Tree.Resources {
    ImageResource drafts();

    ImageResource home();

    ImageResource inbox();

    ImageResource sent();

    ImageResource templates();

    ImageResource trash();

    @Override
    @Source("noimage.png")
    ImageResource treeLeaf();
  }

  private Tree tree;

  DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
  ValueListBox<Caja> caja;
  Long beneficiarioIdFilter;
  Long cajaIdFilter;
  Date fechaDesde;
  Date fechaHasta;
  
  /**
   * Constructs a new mailboxes widget.
   */
  public Mailboxes() {
	 
    Images images = GWT.create(Images.class);
    
    tree = new Tree(images);
    final TreeItem root = new TreeItem(
        imageItemHTML(images.home(), LoginService.Util.currentUser.getDescription()));
    tree.addItem(root);
    
    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
  	  @Override
  	  public void onSelection(SelectionEvent<TreeItem> event) {
//  	    TreeItem item = event.getSelectedItem();
  		
  	    if (event.getSelectedItem().getText().trim().equals("Mis Pendientes")) {
  	    	Mail m = AppUtils.Util.getMail();
//  	    	m.mostrar();
  	    	m.mostrarMisPendientes();
  	    	AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
  	    } else if (event.getSelectedItem().getText().trim().equals("Mis Finiquitados")) {
  	    	Mail m = AppUtils.Util.getMail();
//  	    	m.mostrar();
  	    	m.mostrarMisFiniquitados();
  	    	AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
	    } else if (event.getSelectedItem().getText().trim().equals("Pendientes")) {
  	    	Mail m = AppUtils.Util.getMail();
//  	    	m.mostrar();
  	    	m.mostrarPendientes();
  	    	AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
	    } else if (event.getSelectedItem().getText().trim().equals("Finiquitados")) {
  	    	Mail m = AppUtils.Util.getMail();
  	    	m.mostrar();
  	    	m.mostrarFiniquitados();
  	    	AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
	    } else if (event.getSelectedItem().getText().trim().equals("Anulados")) {
  	    	Mail m = AppUtils.Util.getMail();
//  	    	m.mostrar();
  	    	m.mostrarAnulados();
  	    	AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent());
	    }
  	  }
  	});

    addImageItem(root, "Mis Pendientes", images.inbox());
    addImageItem(root, "Mis Finiquitados", images.sent());
    addImageItem(root, "Pendientes", images.drafts());
    addImageItem(root, "Finiquitados", images.templates());
//    addImageItem(root, "Anulados", images.trash());

    root.setState(true);

    
//    // Create a static tree and a container to hold it
//    Tree staticTree = createStaticTree();
//    staticTree.setAnimationEnabled(true);
//    ScrollPanel staticTreeWrapper = new ScrollPanel(staticTree);
//    staticTreeWrapper.setSize("300px", "300px");
//
//    // Wrap the static tree in a DecoratorPanel
//    DecoratorPanel staticDecorator = new DecoratorPanel();
//    staticDecorator.setWidget(staticTreeWrapper);
    
    
    
    VerticalPanel vp = new VerticalPanel();
    
    vp.add(tree);
    vp.add(crearFiltros());
    initWidget(vp);
  }

  /**
   * A helper method to simplify adding tree items that have attached images.
   * {@link #addImageItem(TreeItem, String, ImageResource) code}
   * 
   * @param root the tree item to which the new item will be added.
   * @param title the text associated with this item.
   */
  private TreeItem addImageItem(TreeItem root, String title,
      ImageResource imageProto) {
    TreeItem item = new TreeItem(imageItemHTML(imageProto, title));
       
    root.addItem(item);
    return item;

  }

  /**
   * Generates HTML for a tree item with an attached icon.
   * 
   * @param imageProto the image prototype to use
   * @param title the title of the item
   * @return the resultant HTML
   */
  private SafeHtml imageItemHTML(ImageResource imageProto, String title) {
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    builder.append(AbstractImagePrototype.create(imageProto).getSafeHtml());
    builder.appendHtmlConstant(" ");
    builder.appendEscaped(title);
    return builder.toSafeHtml();
  }
  
  private Tree crearFiltros() {

	  
//	  Anchor beneficiario = new Anchor("Beneficiario");
//	  beneficiario.addClickHandler(new ClickHandler() {
//		@Override
//		public void onClick(ClickEvent event) {
//			ListaBeneficiarios lb = new ListaBeneficiarios(10);
//			lb.setListener(new Listener() {
//				@Override
//				public void onSelected(Beneficiario beneficiarioSelected) {
//					AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(beneficiarioSelected.getId(), cajaIdFilter));
//				}
//			});
//			lb.mostrarDialog();
//		}
//	});
	  
	  
	  caja = new ValueListBox<Caja>(
				
				new Renderer<Caja>() {
					@Override
					public String render(Caja object) {
						return object.getSiglas();
					}
					@Override
					public void render(Caja object, Appendable appendable)
							throws IOException {
						// TODO Auto-generated method stub
					}
				},
				new SimpleKeyProvider<Caja>() {
					@Override
					public Object getKey(Caja item) {
						return item == null ? null : item.getId();
					}
			});
			
	  try {
	  BeneficiarioService.Util.get().findCajaAll(new MethodCallback<List<Caja>>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				new UIErrorRestDialog(method, exception);
			}

			@Override
			public void onSuccess(Method method, List<Caja> response) {
				Caja cajaTodos = new Caja();
				cajaTodos.setSiglas("TODOS");
				response.add(0,cajaTodos);
				caja.setAcceptableValues(response);
			}
		});
	  } catch (Exception e) {
		  Window.alert(e.getMessage());
	  }

	  
	  caja.addValueChangeHandler(new ValueChangeHandler<Caja>() {
			@Override
			public void onValueChange(ValueChangeEvent<Caja> event) {
				setCajaIdFilter(event.getValue().getId());
				AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(Mailboxes.this.beneficiarioIdFilter,event.getValue().getId(), fechaDesde, fechaHasta));
			}
	  });
	  
	  
	  
	  Tree staticTree = new Tree();
	  staticTree.setAnimationEnabled(true);
	  TreeItem filtrarItem = staticTree.addTextItem("Filtrar");
	  
	  BeneficiarioSelector bs = new BeneficiarioSelector();
	  bs.setClearVisible(true); 
	  bs.setListener(new Listener() {
		@Override
		public void onSelected(Beneficiario beneficiarioSelected) {
			if (beneficiarioSelected == null) {
				beneficiarioIdFilter = null;
			} else {
				Mailboxes.this.beneficiarioIdFilter = beneficiarioSelected.getId();
			}
			AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(beneficiarioIdFilter, getCajaIdFilter(), fechaDesde, fechaHasta));
		}
	});

	  DateBox fechaDesde = new DateBox();
	  DateBox fechaHasta = new DateBox();
	  
	  fechaDesde.setFormat(new DateBox.DefaultFormat(dateFormat));
	  fechaHasta.setFormat(new DateBox.DefaultFormat(dateFormat));
	  
	  fechaDesde.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
		
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			Mailboxes.this.fechaDesde = null;
			AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(Mailboxes.this.beneficiarioIdFilter, Mailboxes.this.cajaIdFilter, Mailboxes.this.fechaDesde, Mailboxes.this.fechaHasta));
		}
	  });
	  fechaDesde.addValueChangeHandler(new ValueChangeHandler<Date>() {
		
		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
			Mailboxes.this.fechaDesde = event.getValue();
			AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(Mailboxes.this.beneficiarioIdFilter, Mailboxes.this.cajaIdFilter, Mailboxes.this.fechaDesde, Mailboxes.this.fechaHasta));			
		}
	  });
	  fechaHasta.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
		
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			Mailboxes.this.fechaHasta = null;
			AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(Mailboxes.this.beneficiarioIdFilter, Mailboxes.this.cajaIdFilter, Mailboxes.this.fechaDesde, Mailboxes.this.fechaHasta));
		}
	  });
	  
	  
	  fechaHasta.addValueChangeHandler(new ValueChangeHandler<Date>() {
		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
			Mailboxes.this.fechaHasta = event.getValue();
			AppUtils.EVENT_BUS.fireEvent(new RefreshMailEvent(Mailboxes.this.beneficiarioIdFilter, Mailboxes.this.cajaIdFilter, Mailboxes.this.fechaDesde, Mailboxes.this.fechaHasta));
		}
	  });
	 
	  
	  
	  VerticalPanel vp = new VerticalPanel();
	  vp.add(new HTML("<b>Caja de Jubilacion<b>"));
	  vp.add(caja);
	  vp.add(new HTML("<b>Beneficiario<b>"));
	  vp.add(bs);
	  vp.add(new HTML("<b>Fecha desde<b>"));
//	  HorizontalPanel hp = new HorizontalPanel();
	  vp.add(fechaDesde);
	  vp.add(new HTML("<b>Fecha hasta<b>"));
	  vp.add(fechaHasta);
//	  vp.add(hp);

	  
	  filtrarItem.addItem(vp);
  	  return staticTree;
  	  
  }

public Long getBeneficiarioIdFilter() {
	return beneficiarioIdFilter;
}

public void setBeneficiarioIdFilter(Long beneficiarioIdFilter) {
	this.beneficiarioIdFilter = beneficiarioIdFilter;
}

public Long getCajaIdFilter() {
	return cajaIdFilter;
}

public void setCajaIdFilter(Long cajaIdFilter) {
	this.cajaIdFilter = cajaIdFilter;
}
  
  
  
  
}
