/*
 * $Id: CampusRefInfo.java,v 1.4 2004/05/24 14:21:40 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.application.presentation.ReferenceNumberInfo;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;

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

    T.add(new ReferenceNumberInfo(),1,1);
    T.mergeCells(1,1,2,1);
    T.setWidth(1,"100%");

    add(T);
  }
}
