package py.edu.uca.intercajas.server.ejb;

import javax.naming.InitialContext;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class BeanLocator implements ServiceLocator {
    @Override
    public Object getInstance(Class<?> clazz) {
        return lookupBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookupBean(Class<T> clazz) {
        try {
            return (T) InitialContext.doLookup("java:module/" + clazz.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
