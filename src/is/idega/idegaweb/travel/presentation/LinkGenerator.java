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
  private static String http = "https";

  public LinkGenerator() {
    super.setWidth(600);
    super.setHeight(200);
    super.setTitle("idegaWeb Travel");
  }

  public void main(IWContext iwc) {
    super.main(iwc);

    String productId = iwc.getParameter(parameterProductId);

    String link = getLinkText(iwc, Integer.parseInt(productId));

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
    Link link = new Link("Try link",getLinkText(iwc, serviceId));
      link.setTarget(Link.TARGET_BLANK_WINDOW);
    return link;
  }

  private static String getLinkText(IWContext iwc, int serviceId) {
    return http+"://"+iwc.getServerName()+":"+iwc.getServerPort()+"/servlet/ObjectInstanciator?idegaweb_instance_class="+PublicBooking.class.getName()+"&"+parameterProductId+"="+serviceId;
  }
}
