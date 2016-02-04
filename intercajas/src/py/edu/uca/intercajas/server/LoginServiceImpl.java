package py.edu.uca.intercajas.server;

import javax.ejb.EJB;

import py.edu.uca.intercajas.client.LoginService;
import py.edu.uca.intercajas.server.ejb.UserLogin;
import py.edu.uca.intercajas.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
    private static final long serialVersionUID = 4456105400553118785L;
 
    @EJB
    UserLogin userLoign;

    @Override
    public UserDTO loginServer(String name, String password)
    {
    	
    	UserDTO user = userLoign.login(name, password, this.getThreadLocalRequest().getSession().getId());	
//    	storeUserInSession(user);
        return user;
        
    }
 
    @Override
    public UserDTO loginFromSessionServer()
    {
        return getUserAlreadyFromSession();
    }
 
    @Override
    public void logout()
    {
        deleteUserFromSession();
    }
 
    @Override
    public boolean changePassword(String name, String oldPassword, String newPassword)
    {
        return userLoign.changePassword(name, oldPassword, newPassword);
    }
 
    private UserDTO getUserAlreadyFromSession()
    {
    	
        return userLoign.getValidUser(this.getThreadLocalRequest().getSession().getId());
    }
 
    private void deleteUserFromSession()
    {
    	userLoign.logout(this.getThreadLocalRequest().getSession().getId());
    }

 
}