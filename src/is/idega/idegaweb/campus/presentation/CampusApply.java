/*
 * $Id: CampusApply.java,v 1.5 2002/01/10 12:30:42 aron Exp $
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
    Table T = new Table(2,1);
    T.setWidth("100%");
    T.setWidth(1,"500");
    T.setAlignment(2,1,"center");
    T.setVerticalAlignment(1,1,"top");
    T.setVerticalAlignment(2,1,"top");

    Image textImage = iwb.getImage("/text_pictures/apply.jpg");
    textImage.setVerticalSpacing(12);
    if(iwc.hasEditPermission(this))
      T.add("Átt þú ekki að skrá umsóknir á öðrum stað "+iwc.getUser().getName()+" !!",1,1);
    else
      T.add(new CampusApplicationForm(),1,1);
    if (iwc.getParameter("status") == null || iwc.getParameter("status").equals("2")) {
      T.add(textImage,2,1);
    }
    else {
      T.mergeCells(1,1,2,1);
      T.setWidth(1,"100%");
    }
    add(T);
  }
}
