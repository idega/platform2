package is.idega.idegaweb.travel.presentation;

import com.idega.core.builder.business.BuilderConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
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
	  	IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
	  	IWBundle iwb = iwma.getBundle(TravelWindow.IW_BUNDLE_IDENTIFIER);  	
	  	
	  	String serverName = iwb.getProperty(PROPERTY_SERVER_NAME);
	  	String pageID = iwb.getProperty(PROPERTY_REFUNDER_PAGE_ID);
	  	if (serverName == null) {
	  		serverName = iwc.getServerName();
	  	}
	  	if (pageID == null) {
	  		return "";
	  	}
	  	
	  	StringBuffer text = new StringBuffer(http+"://"+serverName);
    if (!http.equals("https")) {
      text.append(":"+iwc.getServerPort());
    }
    text.append("/servlet/IBMainServlet?"+BuilderConstants.IB_PAGE_PARAMETER+"="+pageID);
    
    if (refNumber != null) {
    		text.append("&"+BookingRefunder.PARAMETER_EMAILED_REFERENCE_NUMBER+"="+refNumber);
    }
    
    return text.toString();
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
    String className = iwc.getIWMainApplication().getEncryptedClassName(classToInstanciate);
    if (classToInstanciate.getName().equals(Booking.class.getName())) {
      parName = Booking.parameterProductId;
    }

    String url = iwc.getIWMainApplication().getObjectInstanciatorURI(classToInstanciate)+"&"+parName+"="+serviceId;
    text.append(url);
//    text.append("/servlet/ObjectInstanciator?idegaweb_instance_class="+className+"&"+parName+"="+serviceId);
    return text.toString();
  }
}
