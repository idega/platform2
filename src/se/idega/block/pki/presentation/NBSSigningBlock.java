/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.presentation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.block.pki.data.NBSSignedEntity;
import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSException;
import se.nexus.nbs.sdk.NBSMessageHttp;
import se.nexus.nbs.sdk.NBSMessageResult;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.NBSSignResult;
import se.nexus.nbs.sdk.servlet.ServletUtil;

import com.idega.builder.business.BuilderLogic;
import com.idega.builder.data.IBPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Parameter;


/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSSigningBlock extends Block implements Builderaware{

	//Session key for text to be signed
	private final static String SIGNED_TEXT = "se.idega.block.pki.presentation.NBSSigningBlock.SIGNED_TEXT";
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.block.pki";	
	
	public final static String INIT_DONE = "se.idega.block.pki.INIT_DONE";
				
	public final static String NBS_SIGNED_ENTITY = "se.idega.block.pki.business.NBS_SIGNED_ENTITY";
	
	//localization of signature date
	public final static String LOCAL_SIGNATURE_DATE = "se.idega.block.pki.business.SIGNATURE_DATE";
	
	private Map _addedParameters = new HashMap();
		
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}	
	
	public NBSSigningBlock() {
		super();
	}	
		
	
	public void main(IWContext iwc) throws Exception{
		IWResourceBundle iwrb = getResourceBundle(iwc);
		try{
			
			if (iwc.isInEditMode() || iwc.isInPreviewMode()){
				return;
			}
			NBSSignedEntity signedEntity = getNBSSignedEntity(iwc);
			
			DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, iwc.getCurrentLocale());
						
			String toBeSigned = signedEntity.getText() + "\n\n\n" + iwrb.getLocalizedString(LOCAL_SIGNATURE_DATE, "DOCUMENT SIGNED:") + dateFormat.format(new Date());
			iwc.setSessionAttribute(SIGNED_TEXT, toBeSigned);
			//TODO: (roar) TBD:
			System.out.println("### ToBeSigned: " + toBeSigned);
			
			switch(signedEntity.getAction()){
				case NBSSignedEntity.ACTION_INIT:
					NBSSigningApplet applet = new NBSSigningApplet(initSignContract(iwc, toBeSigned));
					Iterator i = _addedParameters.entrySet().iterator();
					while(i.hasNext()){
						Map.Entry entry = (Map.Entry) i.next();
						applet.addParameter(new Parameter((String) entry.getKey(), (String) entry.getValue()));
					}

					add(applet);
					break;
										
				case NBSSignedEntity.ACTION_PROCESS:
					processSignContract(iwc);	
					forwardToIBPage(iwc,getParentPage(), getGotoPage());					
					break;
				
			}
			
			signedEntity.setNextAction();
			

		}catch(NBSException ex){
			String errorMsg = null;
			switch(ex.getCode()){
				case NBSException.ERROR_SRVCLNT_CONFIG:
					errorMsg = "Server client configuration";
					break;
				case NBSException.ERROR_SRVCLNT_SERVER_UNAVAILABLE:
					errorMsg = "Server unavailable";	
					break;	
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
			add(new Text(iwrb.getLocalizedString("NBSException_code_"+ex.getCode(), errorMsg + " ErrorCode: " + ex.getCode())));			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static IBPage _page;	
		
	public void setGotoPage(IBPage page){
		_page = page;
	}
	
	public IBPage getGotoPage(){
		return _page;
	}
			

	public void setParameter(String name, String value){
		_addedParameters.put(name, value);
	}
			
			
	public NBSMessageHttp initSignContract(IWContext iwc, String toBeSigned) throws NBSException, Exception{
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
		if (iwc.getCurrentUser() == null){
			throw new Exception(getResourceBundle(iwc).getLocalizedString("nbssb_session_lost", "Session lost"));
		}
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
			entity.setSignedBy(((Integer)iwc.getCurrentUser().getPrimaryKey()).intValue());
			entity.setSignedDate(new java.sql.Date( java.lang.System.currentTimeMillis()));
			entity.setText((String) iwc.getSessionAttribute(SIGNED_TEXT));
			entity.store();
		} catch(ClassCastException ex){
			ex.printStackTrace();
			throw new NBSException();	
		}
	}

	public NBSSignedEntity getNBSSignedEntity(IWContext iwc){
		return (NBSSignedEntity) iwc.getSessionAttribute(NBS_SIGNED_ENTITY);
	}
	

	/**
	 * Gets the BidtServer instance.
	 */
	private NBSServerHttp getNBSServer(IWContext iwc) throws NBSException {
		return new NBSLoginBusinessBean().getNBSServer(iwc);
	}

	protected void forwardToIBPage(IWContext iwc,Page fromPage,IBPage pageTo){
		StringBuffer URL = new StringBuffer();
		URL.append(BuilderLogic.getInstance().getIBPageURL(iwc.getApplicationContext(), ((Integer) pageTo.getPrimaryKey()).intValue()));
		//URL.append('&');
		//URL.append(getRequest().getQueryString());
		fromPage.setToRedirect(URL.toString());
		fromPage.empty();	
	}	
	
}
