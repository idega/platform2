/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.presentation;

import se.nexus.nbs.sdk.NBSException;


import com.idega.builder.data.IBPage;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import java.io.File;

import se.idega.block.pki.data.NBSSignedEntity;
import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSMessageHttp;
import se.nexus.nbs.sdk.NBSMessageResult;
import se.nexus.nbs.sdk.NBSServerFactory;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.NBSSignResult;
import se.nexus.nbs.sdk.servlet.ServletUtil;


/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSSigningBlock extends Block implements Builderaware{

	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.block.pki";	
	
	
	/** Names for objects stored in the servlet context or session. */
	private final static String 
		SERVER_FACTORY = "se.idega.block.pki.ServerFactory",
		SERVER = "se.idega.block.pki.Server",
		SERVLET_URI = "se.nexus.cbt.ServletURI";
	public final static String INIT_DONE = "se.idega.block.pki.INIT_DONE";
				
	public final static String NBS_SIGNED_ENTITY = "se.idega.block.pki.business.NBS_SIGNED_ENTITY";
	
	private final static String BIDT_SDK_PATH_PROPERTY = "bidt_sdk_path";	
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}	
	
	public NBSSigningBlock() {
		super();
	}	
		
	
	public void main(IWContext iwc) throws Exception{
		try{
		
			add(new Text("<h2>Sign Contract</h2>"));
			
			if (iwc.isInEditMode() || iwc.isInPreviewMode()){
				return;
			}
	
			boolean initDone = iwc.getSessionAttribute(INIT_DONE) != null;
			
			String toBeSigned = getNBSSignedEntity(iwc).getText();
			
		
			if (! initDone){
				System.out.println("contractIdSession == null");
				add(new NBSSigningApplet(initSignContract(iwc, toBeSigned)));
				iwc.setSessionAttribute(INIT_DONE, new Object());			
				
			}else {	
				System.out.println("contractIdSession != null");			
				iwc.removeSessionAttribute(INIT_DONE);	
				processSignContract(iwc);	
				iwc.forwardToIBPage(getParentPage(), getGotoPage());	
	
			}
		}catch(NBSException ex){
			String errorMsg = null;
			switch(ex.getCode()){
				case NBSException.ERROR_SRV_CERT_EXPIRED: 		
					errorMsg = "Certificate expired";	
					break;	
				case NBSException.ERROR_SRV_BAD_TBS: 
					errorMsg = "Bad TBS";	
					break;	
				case NBSException.ERROR_SRV_CERT_SIG_INVALID:
					errorMsg = "Invalid signature";	
					break;	
				case NBSException.ERROR_CLNT_NO_KEY:
					errorMsg = "No key";	
					break;	
				default:
					errorMsg = "NBS Error";	
					break;											
			}
			add(new Text(errorMsg + "( Error code: " + ex.getCode() + ")"));			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static IBPage _page;	
		
	public void setGotoPage(IBPage page){
		_page = page;
	}
	
	public IBPage getGotoPage(){
		System.out.println("forwardToIBPage : " + _page.getName());
		return _page;
	}
			
			
			
	public NBSMessageHttp initSignContract(IWContext iwc, String toBeSigned) throws NBSException, Exception{
		System.out.println("initSignContract()");

		System.out.println("toBeSigned: " + toBeSigned);	
				
		NBSServerHttp server = getNBSServer(iwc);	
		HttpMessage httpReq = new HttpMessage();
		ServletUtil.servletRequestToHttpMessage(iwc.getRequest(), httpReq);
							
		NBSMessageResult result =  (NBSMessageResult) server.doSign(toBeSigned, httpReq);	
		
		if (result != null)
		{
			// Send the message to the Client.
			return (NBSMessageHttp) result.getMessage();
		}			
		return null;
	}
	
	
	public void processSignContract(IWContext iwc) throws NBSException, Exception{
		System.out.println("processSignContract");

		NBSSignedEntity entity = getNBSSignedEntity(iwc);				
					
		NBSServerHttp server = getNBSServer(iwc);	
		HttpMessage httpReq = new HttpMessage();
		ServletUtil.servletRequestToHttpMessage(iwc.getRequest(), httpReq);		
		
		try {
			NBSSignResult result = (NBSSignResult) server.handleMessage(httpReq);
			// Interpret the result.
			String signedData = new String(result.getSignedData().getEncoded());
			entity.setXmlSignedData(signedData);	
			entity.setSignedFlag(true);	
			entity.setSignedBy(iwc.getCurrentUser().getID());
			entity.setSignedDate(new java.sql.Date( java.lang.System.currentTimeMillis()));
			entity.store();
		} catch(ClassCastException ex){
			ex.printStackTrace();
			throw new NBSException();	
		}
	}

	public NBSSignedEntity getNBSSignedEntity(IWContext iwc){
		return (NBSSignedEntity) iwc.getSessionAttribute(NBS_SIGNED_ENTITY);
	}
	

	
//	private boolean verifySign(int contractId) throws NBSException {
//		try{
//			Contract contract =
//			((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHomeLegacy(Contract.class))
//				.findByPrimaryKeyLegacy(contractId);
//				
//			return contract.getSignedFlag().booleanValue();
//	
//	//		XMLParser xmlp = new XMLParser();
//	//		XMLDocument xmld = xmlp.parse(new StringReader(contract.getXmlSignedData()));
//	//		XMLElement element = xmld.getRootElement();
//	//	
//							
////			NBSServerCrypt nbsServerCrypt = new NBSServerCrypt();
////			
////			NBSResult result = nbsServerCrypt.verifySignature(contract.getText(), contract.getXmlSignedData());
////			return true; //TODO roar What to return....
//		} catch(SQLException ex){
//			ex.printStackTrace();
//		}		
//		return false;
//	}
		
	/**
	 * Gets the BidtServer instance.
	 */
	private NBSServerHttp getNBSServer(IWContext iwc) throws NBSException {
		System.out.println("getNBSServer()");
		
		NBSServerFactory serverGenerator = getServerGenerator(iwc);

		// If created, the server should be in the session.
		NBSServerHttp server = (NBSServerHttp) iwc.getSession().getAttribute(SERVER);

		// If not created, create it now.
		if (server == null)
		{
			System.out.println("Creating new server");
			// Create a server and save it in the session.
			server = (NBSServerHttp) serverGenerator.getInstance("Http");
			iwc.getSession().setAttribute(SERVER, server);
			
		} else {
			// In case of a replicated session environment
			serverGenerator.updateInstance(server);
		}
		String servletUri = (String) (iwc.getServletContext().getAttribute(SERVLET_URI));
	
		server.setActionUrl(servletUri);
		
		System.out.println("getNBSServer returning");	
		return server;
	}	
	
	public NBSServerFactory getServerGenerator(IWContext iwc) throws NBSException{
		System.out.println("getServerGenerator()");
//		iwc.getApplicationContext().removeApplicationAttribute(SERVER_FACTORY); //TODO Roar - Temporary
		
		NBSServerFactory serverGenerator = (NBSServerFactory) iwc.getApplicationContext().getApplicationAttribute(SERVER_FACTORY);
		if (serverGenerator == null){
			File configFile = new File(getConfigFilePath(iwc));		
			System.out.println("configFile: "+ configFile);	
			serverGenerator = new NBSServerFactory();
			serverGenerator.init(configFile);
			iwc.getApplicationContext().setApplicationAttribute(SERVER_FACTORY, serverGenerator);			
		}
		
		return serverGenerator;
	}
	

	private String getConfigFilePath(IWContext iwc){

		System.out.println("getConfigFilePath()");
		String path = getBundle(iwc).getProperty(BIDT_SDK_PATH_PROPERTY);
		return path != null ? path : "bidt_sdk.properties";
	}
	
	
}
