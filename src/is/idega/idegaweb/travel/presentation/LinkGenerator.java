package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.text.*;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class LinkGenerator extends TravelWindow {

  public static String parameterProductId = "linkGeneratorProductId";
  //private static String http = "http";
  private static Class defaultClass = PublicBooking.class;
  private static String http = "https";

  public LinkGenerator() {
    super.setWidth(600);
    super.setHeight(200);
    super.setTitle("idegaWeb Travel");
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

  private static String getLinkText(IWContext iwc, int serviceId, Class classToInstanciate) {
    StringBuffer text = new StringBuffer(http+"://"+iwc.getServerName());
    if (!http.equals("https")) {
      text.append(":"+iwc.getServerPort());
    }

    String parName = parameterProductId;
    String className = iwc.getApplication().getEncryptedClassName(classToInstanciate);
    if (classToInstanciate.getName().equals(Booking.class.getName())) {
      parName = Booking.parameterProductId;
    }

    String url = iwc.getApplication().getObjectInstanciatorURI(classToInstanciate)+"&"+parName+"="+serviceId;
    text.append(url);
//    text.append("/servlet/ObjectInstanciator?idegaweb_instance_class="+className+"&"+parName+"="+serviceId);
    return text.toString();
  }
}
