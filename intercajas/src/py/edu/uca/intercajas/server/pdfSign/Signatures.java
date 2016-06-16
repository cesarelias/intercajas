package py.edu.uca.intercajas.server.pdfSign;

/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.CertificateVerification;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.VerificationException;
 
public class Signatures {
 
	String principalDir;

	public void signPdf(String src, String dest)
			throws IOException, DocumentException, GeneralSecurityException {

		Security.addProvider(new BouncyCastleProvider());
    	
    	principalDir = System.getProperty("principalDir").toString();
    	
        String path = principalDir + "/firmas/SistemaIntercajas.pfx"; //properties.getProperty("PRIVATE");
        String keystore_password = "123456"; //properties.getProperty("PASSWORD");
        String key_password = "123456"; //properties.getProperty("PASSWORD");
        
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String)ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        // reader and stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        // appearance
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        //appearance.setImage(Image.getInstance(RESOURCE));
        appearance.setReason("Firma automatica el Sistema Intercajas.");
        appearance.setLocation("Pie");
        appearance.setVisibleSignature(new Rectangle(0, 0, 200, 100), 1,"first");
        // digital signature
        ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");
        ExternalDigest digest = new BouncyCastleDigest();
        MakeSignature.signDetached(appearance, digest, es, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
    }
 
    public Boolean verifySignatures(String usuario, String pdfPath) throws GeneralSecurityException, IOException {
    
    	Security.addProvider(new BouncyCastleProvider());
    	
    	principalDir = System.getProperty("principalDir").toString();
    	
    	String certPath = principalDir + "/firmas/" + usuario + ".cer";
    	
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        FileInputStream is2 = new FileInputStream(certPath);
        X509Certificate cert2 = (X509Certificate) cf.generateCertificate(is2);
        ks.setCertificateEntry("cacert", cert2);
 
//        PrintWriter out = new PrintWriter(new FileOutputStream("/tmp/verifFirma.txt"));
        
        PdfReader reader = new PdfReader(pdfPath);
        AcroFields af = reader.getAcroFields();
        ArrayList<String> names = af.getSignatureNames();
        for (String name : names) {
//            out.println("Signature name: " + name);
//            out.println("Signature covers whole document: " + af.signatureCoversWholeDocument(name));
//            out.println("Document revision: " + af.getRevision(name) + " of " + af.getTotalRevisions());
            PdfPKCS7 pk = af.verifySignature(name);
            Calendar cal = pk.getSignDate();
            Certificate[] pkc = pk.getCertificates();
//            out.println("Subject: " + CertificateInfo.getSubjectFields(pk.getSigningCertificate()));
//            out.println("Revision modified: " + !pk.verify());
            List<VerificationException> errors = CertificateVerification.verifyCertificates(pkc, ks, null, cal);
            if (errors.size() == 0) {
//                out.println("Certificates verified against the KeyStore");
            	System.out.println("Certificates verified against the KeyStore");
            	return true;
            } else {
//                out.println(errors);
//                out.flush();
//                out.close();
                return false;
            }
        }
//        out.flush();
//        out.close();
        return false;
    }    
    

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws GeneralSecurityException 
     */
    public static void main(String[] args)
        throws IOException, DocumentException, GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        
        Signatures signatures = new Signatures();
        
        //signatures.signPdf("/home/cesar/Descargas/cerTifCesar/test.pdf", "/home/cesar/Descargas/cerTifCesar/testFirmado.pdf");
        signatures.verifySignatures("/home/cesar/Descargas/cerTifCesar/intercajas.cer","/home/cesar/Descargas/cerTifCesar/testFirmado.pdf");
        
        
    }
}