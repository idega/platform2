/*
 * $Id: CampusApply.java,v 1.17 2004/05/24 14:21:40 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.application.presentation.CampusApplicationForm;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;

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
    T.add(new CampusApplicationForm(),1,1);
    add(T);
  }

}

