package is.idega.idegaweb.tracker.business;

import com.idega.presentation.IWContext;
import java.util.Enumeration;

/**
 * Title:        is.idega.idegaweb.tracker.business.TrackerBusiness
 * Description:  The main business object for the idegaWeb Tracker statistics application
 * Copyright:    Copyright (c) 2002
 * Company:      Idega software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class TrackerBusiness {

  public TrackerBusiness() {
    IWContext iwc = IWContext.getInstance();
    Enumeration enum = iwc.getRequest().getHeaderNames();

    while (enum.hasMoreElements()) {
      iwc.getRequest().getHeader((String)enum.nextElement());

    }

  }



}