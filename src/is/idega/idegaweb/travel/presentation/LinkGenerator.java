package is.idega.idegaweb.travel.presentation;

import java.rmi.RemoteException;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class LinkGenerator extends TravelWindow {

	public static final String PROPERTY_SERVER_NAME = "server_name";
	public static final String PROPERTY_REFUNDER_PAGE_ID = "refunder_form_page_id";
	public static final String PROPERTY_CVC_EXPLANATION_PAGE = "cvc_explanation_page";
	public static final String PROPERTY_PRIVACY_STATEMENT = "privacy_statement_page_id";
	public static final String PROPERTY_TERMS_AND_CONDITION = "terms_and_conditions_page_id";
	
  public static String parameterProductId = "linkGeneratorProductId";
  //private static String http = "http";
  private static Class defaultClass = PublicBooking.class;
  private static String http = "https";

  public LinkGenerator() {
    super.setWidth(600);
    super.setHeight(200);
    super.setTitle("idegaWeb Travel");
  }

  public static boolean getIsHttps() {
  		return http.equals("https");
  }
  
  public void main(IWContext iwc) throws Exception{
    super.main(iwc);

    String productId = iwc.getParameter(parameterProductId);

    String link = getLinkText(iwc, Integer.parseInt(productId), defaultClass);

    Text tLink = (Text) text.clone();
      tLink.setBold();
      tLink.setText("Link");
    add(tLink);
    add(Text.getBreak());

    Link lLink = getLink(iwc,Integer.parseInt(productId));

    add("&lt;a target=\"_blank\" href=\""+link+"\"&gt;Book&lt;/a&gt;");
    add(Text.getBreak());
    add(Text.getBreak());
    add(lLink);
  }

  public static Link getLink(IWContext iwc, int serviceId) {
    return getLink(iwc, serviceId, defaultClass);
  }

  public static Link getLink(IWContext iwc, int serviceId, Class classToInstanciate) {
    Link link = new Link("Try link",getLinkText(iwc, serviceId, classToInstanciate));
    if (classToInstanciate.getName().equals(defaultClass.getName())) {
      link.setTarget(Link.TARGET_BLANK_WINDOW);
    }
    return link;
  }

  public static String getUrlToRefunderPage(IWContext iwc, String refNumber) {
	  	StringBuffer text = getUrlToPage(iwc, PROPERTY_REFUNDER_PAGE_ID);
    
    if (refNumber != null) {
    		text.append("&"+BookingRefunder.PARAMETER_EMAILED_REFERENCE_NUMBER+"="+refNumber);
    }
    
    text.append("&"+LocaleSwitcher.languageParameterString+"="+iwc.getCurrentLocale().toString());
    
    return text.toString();
  }
  
	private static StringBuffer getUrlToPage(IWContext iwc, String bundleParameterToPage) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle iwb = iwma.getBundle(TravelWindow.IW_BUNDLE_IDENTIFIER);  	
	
		String pageID = "-1";
		if (bundleParameterToPage != null) {
			pageID = iwb.getProperty(bundleParameterToPage);
		}
		String serverName = iwb.getProperty(PROPERTY_SERVER_NAME);
		if (serverName == null) {
			serverName = iwc.getServerName();
		}

		
		StringBuffer text = new StringBuffer(http+"://"+serverName);
	   if (!http.equals("https")) {
	     text.append(":"+iwc.getServerPort());
	   }
	   BuilderService bs;
		try {
			bs = BuilderServiceFactory.getBuilderService(iwc);
			text.append(bs.getPageURI(pageID));
		} catch (NumberFormatException e) {
			
		}	catch (RemoteException e) {
			e.printStackTrace();
		}
		if (pageID != null) {
	   }
	   else{
	   	text.append("/");
	   }
	   
	   //text.append(iwc.getIWMainApplication().getBuilderServletURI());
	   //if (pageID != null) {
	   //	text.append("?"+BuilderConstants.IB_PAGE_PARAMETER+"="+pageID);
	   //}
	   return text;
	}

public static Link getLinkToRefunderForm(IWContext iwc) {
  		return new Link("Test", getUrlToRefunderPage(iwc, null));
  }
    
  private static String getLinkText(IWContext iwc, int serviceId, Class classToInstanciate) {
	  	IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
	  	IWBundle iwb = iwma.getBundle(TravelWindow.IW_BUNDLE_IDENTIFIER);  	
	  	
	  	String serverName = iwb.getProperty(PROPERTY_SERVER_NAME);
	  	if (serverName == null) {
	  		serverName = iwc.getServerName();
	  	}
  	
    StringBuffer text = new StringBuffer(http+"://"+serverName);
    if (!http.equals("https")) {
      text.append(":"+iwc.getServerPort());
    }

    String parName = parameterProductId;
    //String className = iwc.getIWMainApplication().getEncryptedClassName(classToInstanciate);
    if (classToInstanciate.getName().equals(Booking.class.getName())) {
      parName = Booking.parameterProductId;
    }

    String url = iwc.getIWMainApplication().getObjectInstanciatorURI(classToInstanciate)+"&"+parName+"="+serviceId;
    text.append(url);
    //text.append("/servlet/ObjectInstanciator?idegaweb_instance_class="+className+"&"+parName+"="+serviceId);
    return text.toString();
  }
  
  public static Link getLinkCVCExplanationPage(IWContext iwc, Text text) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle iwb = iwma.getBundle(TravelWindow.IW_BUNDLE_IDENTIFIER);  	
	
		String pageID = iwb.getProperty(PROPERTY_CVC_EXPLANATION_PAGE);
		StringBuffer url = getUrlToPage(iwc,null);
  		Window window = new Window(text.getText(), 400, 500, url.toString());
  		
		Link link = new Link(text, window);
  		//Link link = new Link(text, window);
		if (pageID != null) {
			link.setPage(Integer.parseInt(pageID));
		}
  		link.setTarget(Link.TARGET_BLANK_WINDOW);

  		return link;
	}
  
  public static Link getLinkToPrivacyStatement(IWContext iwc, Text text) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle iwb = iwma.getBundle(TravelWindow.IW_BUNDLE_IDENTIFIER);  	
	
		String pageID = iwb.getProperty(PROPERTY_PRIVACY_STATEMENT);
		StringBuffer url = getUrlToPage(iwc,null);
  		Window window = new Window(text.getText(), 400, 500, url.toString());
  		
		Link link = new Link(text, window);
  		//Link link = new Link(text, window);
		if (pageID != null) {
			link.setPage(Integer.parseInt(pageID));
		}
  		link.setTarget(Link.TARGET_BLANK_WINDOW);

  		return link;  }
  
  public static Link getLinkToTermsAndContition(IWContext iwc, Text text) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle iwb = iwma.getBundle(TravelWindow.IW_BUNDLE_IDENTIFIER);  	
	
		String pageID = iwb.getProperty(PROPERTY_TERMS_AND_CONDITION);
		StringBuffer url = getUrlToPage(iwc,null);
  		Window window = new Window(text.getText(), 400, 500, url.toString());
  		
		Link link = new Link(text, window);
  		//Link link = new Link(text, window);
		if (pageID != null) {
			link.setPage(Integer.parseInt(pageID));
		}
  		link.setTarget(Link.TARGET_BLANK_WINDOW);

  		return link;  }
  
  public static Link getVoucherLink(String referenceNumber) {
  		Link voucherLink = new Link();
  		return voucherLink;
  }
  
}
