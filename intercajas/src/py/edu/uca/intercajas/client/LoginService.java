package py.edu.uca.intercajas.client;

import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("LoginService")
public interface LoginService extends RemoteService
{
    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util
    {
        private static LoginServiceAsync instance;
 
        public static LoginServiceAsync getInstance()
        {
            if (instance == null)
            {
                instance = GWT.create(LoginService.class);
            }
            return instance;
        }
    }
 
    UserDTO loginServer(String name, String password);
 
    UserDTO loginFromSessionServer();
     
    boolean changePassword(String name, String newPassword);
 
    void logout();
    
}