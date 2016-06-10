package py.edu.uca.intercajas.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.fileupload.FileItem;

import py.edu.uca.intercajas.server.ejb.UserLogin;
import py.edu.uca.intercajas.server.pdfSign.Signatures;
import py.edu.uca.intercajas.shared.UserDTO;
import sun.reflect.generics.visitor.Reifier;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

/**
 * This is an example of how to use UploadAction class.
 * 
 * This servlet saves all received files in a temporary folder, and deletes them
 * when the user sends a remove request.
 * 
 * @author Manolo Carrasco Moñino
 *
 */
public class MyCustomizedUploadServlet extends UploadAction {

	
	@EJB
	UserLogin userLogin;
	
	
	private static final long serialVersionUID = 1L;

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */

	String principalDir = null;

	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {

		
		UserDTO user = userLogin.getValidUser(request.getSession().getId());
        if (user == null) {
        	throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("usuario no valido").build());
        }
        
		
		principalDir = getPrincipal();

		String response = "";

		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					// / Create a new file based on the remote file name in the
					// client
					// String saveName =
					// item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+",
					// "_");
					// File file =new File("/tmp/" + saveName);

					// / Create a temporary file placed in /tmp (only works in
					// unix)
					// File file = File.createTempFile("upload-", ".bin", new
					// File("/tmp"));

					// / Create a temporary file placed in the default system
					// temp folder

					String newFileName = principalDir + "/imgs/intercajas-"
							+ new Date().getTime() + ".bin";
					File file = new File(newFileName);

					item.write(file);
					
					try {
						Signatures s = new Signatures();
						if (!s.verifySignatures(user.getName(), newFileName)){
							//throw new UploadActionException("Documento no firmado correctamente");
							//throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Documento no firmado correctamente").build());
							return "ErrorFirma";
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new WebApplicationException(Response.status(Status.FORBIDDEN).entity("Documento no firmado correctamente..").build());
						//throw new UploadActionException("Documento no firmado correctamente");
					}
					

					// / Save a list with the received files
					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(),
							item.getContentType());

					// / Send a customized message to the client.
					if (response.length() == 0) {
						response += "/imgs/" + "|" + file.getName() + "|"
								+ item.getName();
					} else {
						response += "|" + "/imgs/" + "|" + file.getName() + "|"
								+ item.getName();
					}

				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// Send your customized message to the client.
		return response;
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		principalDir = getPrincipal();

		String fieldName = request.getParameter(UConsts.PARAM_SHOW);

		String nombreDescarga = request.getParameter("nombreDescarga");

		File f = new File(principalDir + "/" + fieldName);

		if (f.exists()) {
			if (nombreDescarga != null && nombreDescarga.length() > 0) {
				response.setHeader("Content-Disposition",
						"attachment;filename=\"" + nombreDescarga + "\"");
			}
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
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}

	public String getPrincipal() {

		principalDir = System.getProperty("principalDir").toString();
		if (principalDir == null || principalDir.length() < 2) {
			throw new RuntimeException(
					"No se definio el principalDir, favor contacte con el administrador del sistema");
		}

		if (principalDir.substring(principalDir.length() - 1,
				principalDir.length()).equals("/")) {
			principalDir = principalDir.substring(0, principalDir.length() - 1);
		}

		File imgs = new File(principalDir + "/imgs");
		// if the directory does not exist, create it
		if (!imgs.exists()) {

			try {
				imgs.mkdir();
			} catch (SecurityException se) {
				throw new RuntimeException(
						"No se ex posible crear el direcotrio imgs en principal, favor contacte con el administrador del sistema");
			}
		}

		File reports = new File(principalDir + "/reports");
		// if the directory does not exist, create it
		if (!reports.exists()) {

			try {
				reports.mkdir();
			} catch (SecurityException se) {
				throw new RuntimeException(
						"No se ex posible crear el direcotrio reports en principal, favor contacte con el administrador del sistema");
			}
		}

		return principalDir;
	}

}