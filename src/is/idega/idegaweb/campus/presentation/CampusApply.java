/*
 * $Id: CampusApply.java,v 1.12 2002/03/12 22:29:47 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;

import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.campus.block.application.presentation.CampusApplicationForm;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class CampusApply extends PresentationObjectContainer {
  private static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

  /**
   *
   */
  public CampusApply() {
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return IW_RESOURCE_BUNDLE;
  }

  public void main(IWContext iwc) {
    IWBundle iwb = getBundle(iwc);
    IWResourceBundle iwrb = getResourceBundle(iwc);
    Table T = new Table(1,1);
    T.setWidth("100%");
    T.setAlignment(1,1,"center");
    T.setVerticalAlignment(1,1,"top");

    if(iwc.hasEditPermission(this))
      T.add("Átt þú ekki að skrá umsóknir á öðrum stað "+iwc.getUser().getName()+" !!",1,1);
    else
      T.add(iwrb.getLocalizedString("app_opens_soon","The site will be accepting applications online from April 1st 2002"),1,1);
//      T.add(new CampusApplicationForm(),1,1);

    add(T);
  }
}
