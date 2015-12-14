package py.edu.uca.intercajas.server.entity;


import py.edu.uca.intercajas.server.ejb.BeanLocator;
import py.edu.uca.intercajas.server.ejb.GestionBeneficiario;
import py.edu.uca.intercajas.server.ejb.GestionCosto;
import py.edu.uca.intercajas.server.ejb.GestionUsuario;

import com.google.web.bindery.requestfactory.shared.Locator;

/**
 * Entity Locator for all entities
 */
public class EntityLocator extends Locator<EntityBase, Long> {

    @Override
    public EntityBase create(Class<? extends EntityBase> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public EntityBase find(Class<? extends EntityBase> clazz, Long id) {
        if (clazz.equals(Usuario.class)) {
			return BeanLocator.lookupBean(GestionUsuario.class).find(id);
        }
        if (clazz.equals(Beneficiario.class)) {
			return BeanLocator.lookupBean(GestionBeneficiario.class).find(id);
        }
        
        
        return null;
    }

    @Override
    public Class<EntityBase> getDomainType() {
        // TODO que diablos es esto?
        return EntityBase.class;
    }

    @Override
    public Long getId(EntityBase e) {
        return e.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Object getVersion(EntityBase e) {
        return e.getVersion();
    }
	
}