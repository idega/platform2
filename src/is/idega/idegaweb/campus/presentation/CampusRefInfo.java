/*
 * $Id: CampusRefInfo.java,v 1.1 2001/11/29 11:17:34 palli Exp $
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
import is.idega.idegaweb.campus.block.application.presentation.ReferenceNumberInfo;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusRefInfo extends PresentationObjectContainer {
  private static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

  /**
   *
   */
  public CampusRefInfo() {
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return IW_RESOURCE_BUNDLE;
  }

  /**
   *
   */
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

    T.add(new ReferenceNumberInfo(),1,1);
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