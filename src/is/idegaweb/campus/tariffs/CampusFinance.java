/*
 * $Id: CampusFinance.java,v 1.6 2001/08/27 11:16:36 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.tariffs;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.IFrame;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.tariffs.CampusFinanceMenu;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusFinance extends JModuleObject {

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  public final static String FRAME_NAME = "rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusFinance() {
  }

  public void main(ModuleInfo modinfo){
    Table myTable = new Table(2,2);
      myTable.mergeCells(2,1,2,2);
      myTable.setWidth(1,"160");
      myTable.setCellpadding(3);
      myTable.setWidth("100%");
      myTable.setHeight("100%");
      myTable.setBorder(0);
      myTable.setVerticalAlignment(1,1,"top");
      myTable.setVerticalAlignment(2,1,"top");
      myTable.setVerticalAlignment(1,2,"top");

    IFrame iFrame = new IFrame("menuFrame");
      iFrame.setSrc(CampusFinanceMenu.class);
      iFrame.setWidth(160);
      iFrame.setHeight(150);
      iFrame.setBorder(IFrame.FRAMEBORDER_OFF);
      iFrame.setScrolling(IFrame.SCROLLING_YES);
      iFrame.setStyle("border: 1 solid #000000");
      myTable.add(iFrame,1,1);

    IFrame iFrame2 = new IFrame(FRAME_NAME);
      iFrame2.setWidth("100%");
      iFrame2.setHeight("100%");
      iFrame2.setBorder(IFrame.FRAMEBORDER_OFF);
      iFrame2.setScrolling(IFrame.SCROLLING_YES);
      iFrame2.setStyle("border: 1 solid #000000");
      myTable.add(iFrame2,2,1);

    add(myTable);
  }

}