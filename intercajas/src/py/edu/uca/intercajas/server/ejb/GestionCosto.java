package py.edu.uca.intercajas.server.ejb;

import java.util.logging.Logger;

import javax.resource.spi.IllegalStateException;

import py.edu.uca.intercajas.shared.UnknownException;

public class GestionCosto {
	private static final Logger LOG = Logger.getLogger(GestionCosto.class.getName());
	
	public static void test(String nombre) throws IllegalStateException  {
		LOG.info("insertarCosto String llamado");
		if ("a".equals("a")) {
			throw new IllegalStateException("throw error de prueba");
		}
	}
		
}
