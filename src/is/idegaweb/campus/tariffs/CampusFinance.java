/*
 * $Id: CampusFinance.java,v 1.9 2001/10/01 13:07:28 aron Exp $
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
      myTable.setBorderColor("#000000");
      myTable.mergeCells(2,1,2,2);
      myTable.setWidth(1,"130");
      myTable.setWidth(2,"100%");
      myTable.setCellpadding(3);
      myTable.setWidth("100%");
      myTable.setHeight("100%");
      myTable.setColumnAlignment(1,"left");

      myTable.setBorder(0);
      myTable.setVerticalAlignment(1,1,"top");
      myTable.setVerticalAlignment(2,1,"top");
      myTable.setVerticalAlignment(1,2,"top");

    IFrame iFrame = new IFrame("menuFrame");
      iFrame.setSrc(CampusFinanceMenu.class);
      iFrame.setWidth(120);
      iFrame.setHeight(150);
      iFrame.setBorder(IFrame.FRAMEBORDER_ON);
      iFrame.setScrolling(IFrame.SCROLLING_YES);
      iFrame.setStyle("border: 1 solid #000000");
      //iFrame.setAlignment(IFrame.ALIGN_LEFT);

      myTable.add(iFrame,1,1);


    IFrame iFrame2 = new IFrame(FRAME_NAME);
     iFrame2.setSrc(CampusFinanceIndex.class);
      iFrame2.setWidth("100%");
      iFrame2.setHeight("100%");
      iFrame2.setBorder(IFrame.FRAMEBORDER_ON);
      iFrame2.setScrolling(IFrame.SCROLLING_YES);
      iFrame2.setAlignment(IFrame.ALIGN_LEFT);
      iFrame2.setStyle("border: 1 solid #000000");
      myTable.add(iFrame2,2,1);

    add(myTable);
  }


}
