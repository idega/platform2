package is.idega.travel.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
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

  public LinkGenerator() {
    super.setWidth(600);
    super.setHeight(100);
    super.setTitle("idegaWeb Travel");
  }

  public void main(ModuleInfo modinfo) {
    super.main(modinfo);

    String productId = modinfo.getParameter(parameterProductId);

    String link = modinfo.getServerName()+":"+modinfo.getServerPort()+"/servlet/ObjectInstanciator?idegaweb_instance_class="+Booking.class.getName()+"&"+Booking.parameterProductId+"="+productId;

    Text tLink = (Text) text.clone();
      tLink.setBold();
      tLink.setText("Link");
    add(tLink);
    add(Text.getBreak());

    add("&lt;a href=\"http://"+link+"\"&gt;Book&lt;/a&gt;");
    add(Text.getBreak());
    add(Text.getBreak());
    add("<a target=\"_blank\" href=\"http://"+link+"\">Try link</a>");

  }

}
