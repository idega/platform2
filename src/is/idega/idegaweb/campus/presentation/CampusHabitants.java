package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.finance.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.IFrame;
import is.idega.idegaweb.campus.block.phone.presentation.PhoneFiles;
import com.idega.presentation.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import com.idega.presentation.PresentationObjectContainer;
import com.idega.block.reports.presentation.*;
import com.idega.presentation.IWContext;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

 public class CampusHabitants extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.finance";
  public final static String FRAME_NAME = "fin_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusHabitants() {
  }
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

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

      /*
    IFrame iFrame = new IFrame("menuFrame");
      iFrame.setSrc(CampusFinanceMenu.class);
      iFrame.setWidth(120);
      iFrame.setHeight(200);
      iFrame.setBorder(IFrame.FRAMEBORDER_OFF);
      iFrame.setScrolling(IFrame.SCROLLING_YES);
      iFrame.setStyle("border: 1 solid #000000");
      //iFrame.setAlignment(IFrame.ALIGN_LEFT);

      myTable.add(iFrame,1,1);

    */
    myTable.add(getLinkTable() ,1,1);

    IFrame iFrame2 = new IFrame(FRAME_NAME);
     //iFrame2.setSrc(CampusFinanceIndex.class);
      iFrame2.setWidth("100%");
      iFrame2.setHeight("100%");
      iFrame2.setBorder(IFrame.FRAMEBORDER_OFF);
      iFrame2.setScrolling(IFrame.SCROLLING_YES);
      iFrame2.setAlignment(IFrame.ALIGN_LEFT);
      iFrame2.setStyle("border: 1 solid #000000");
      myTable.add(iFrame2,2,1);


    add(myTable);
  }

  public PresentationObject getLinkTable(){
      Table FL = new Table();
      //FL.setListpadding(1);
      FL.add(getLink(CampusHabitantsLister.class,iwrb.getLocalizedString("lister","Lister"),CampusFinance.FRAME_NAME),1,1);
      return FL;
  }

  public Link getLink(Class cl,String name,String target){
    Link L = new Link(name,cl);
    L.setTarget(target );
    L.setFontSize(2);
    return L;
  }
}


