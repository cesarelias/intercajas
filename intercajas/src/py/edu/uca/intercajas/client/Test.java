package py.edu.uca.intercajas.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import py.edu.uca.intercajas.client.requestfactory.BeneficiarioProxy;
import py.edu.uca.intercajas.client.requestfactory.ContextGestionBeneficiario;
import py.edu.uca.intercajas.client.requestfactory.DocumentoIdentidadProxy;
import py.edu.uca.intercajas.client.view.solicitud.events.EditBeneficiarioEvent;
import py.edu.uca.intercajas.server.entity.enums.TipoDocumentoIdentidad;

public class Test {

	
	public Test(ContextGestionBeneficiario context, EventBus eventBus) {
		test(context, eventBus);
	}
	
	public void test(ContextGestionBeneficiario context, EventBus eventBus) {
		
			Integer.parseInt("I_AM_NOT_A_NUMBER");
		
		BeneficiarioProxy beneficiario = context.create(BeneficiarioProxy.class);
		DocumentoIdentidadProxy docProxy = context.create(DocumentoIdentidadProxy.class);

		docProxy.setNumeroDocumento("123322");
		docProxy.setTipoDocumento(TipoDocumentoIdentidad.CEDULA);
		beneficiario.setDocumento(docProxy);
		beneficiario.setNombres("cesarito");
		beneficiario.setApellidos("sanabrita");

	    context.insertarBeneficiario(beneficiario);
	    
	    eventBus.fireEvent(new EditBeneficiarioEvent(beneficiario, context));
	}
}
