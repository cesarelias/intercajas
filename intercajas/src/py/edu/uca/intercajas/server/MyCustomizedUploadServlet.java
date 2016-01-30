package py.edu.uca.intercajas.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import sun.reflect.generics.visitor.Reifier;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

/**
 * This is an example of how to use UploadAction class.
 *  
 * This servlet saves all received files in a temporary folder, 
 * and deletes them when the user sends a remove request.
 * 
 * @author Manolo Carrasco Moñino
 *
 */
public class MyCustomizedUploadServlet extends UploadAction {

  private static final long serialVersionUID = 1L;
  
  Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
  /**
   * Maintain a list with received files and their content types. 
   */
  Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

  /**
   * Override executeAction to save the received files in a custom place
   * and delete this items from session.  
   */
  @Override
  public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
    String response = "";
    for (FileItem item : sessionFiles) {
      if (false == item.isFormField()) {
        try {
          /// Create a new file based on the remote file name in the client
          // String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
          // File file =new File("/tmp/" + saveName);
          
          /// Create a temporary file placed in /tmp (only works in unix)
          // File file = File.createTempFile("upload-", ".bin", new File("/tmp"));
          
          /// Create a temporary file placed in the default system temp folder
        	
        	String newFileName = "/home/cesar/imgs/intercajas-" + new Date().getTime() + ".bin";
          File file = new File(newFileName);
          
          item.write(file);
          
          /// Save a list with the received files
          receivedFiles.put(item.getFieldName(), file);
          receivedContentTypes.put(item.getFieldName(), item.getContentType());

          
          /// Send a customized message to the client.
          if (response.length() == 0) {
        	  response +=  item.getName() + "|" + file.getName();
          }  else {
        	  response += "|" + item.getName() + "|" + file.getName();
       	  }
        		  
          
          System.out.println("respuesta: ");
          System.out.println(response);

        } catch (Exception e) {
          throw new UploadActionException(e);
        }
      }
    }
    
    /// Remove files from session because we have a copy of them
    removeSessionFileItems(request);
    
    /// Send your customized message to the client.
    return response;
  }
  
  /**
   * Get the content of an uploaded file.
   */
  @Override
  public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String fieldName = request.getParameter(UConsts.PARAM_SHOW);
    
    File f = new File("/home/cesar/imgs/"+fieldName);
    
    if (f.exists()) {
      response.setContentType(receivedContentTypes.get(fieldName));
      FileInputStream is = new FileInputStream(f);
      copyFromInputStreamToOutputStream(is, response.getOutputStream());
    } else {
      renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
   }
  }
  
  /**
   * Remove a file when the user sends a delete request.
   */
  @Override
  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
    File file = receivedFiles.get(fieldName);
    receivedFiles.remove(fieldName);
    receivedContentTypes.remove(fieldName);
    if (file != null) {
      file.delete();
    }
  }
}