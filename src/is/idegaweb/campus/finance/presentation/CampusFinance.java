/*
 * $Id: CampusFinance.java,v 1.2 2001/10/05 08:05:32 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.finance.presentation;

import com.idega.presentation.text.*;
import com.idega.presentation.ui.IFrame;
import is.idegaweb.campus.phone.presentation.PhoneFiles;
import com.idega.presentation.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusFinance extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  public final static String FRAME_NAME = "fin_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusFinance() {
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
     iFrame2.setSrc(CampusFinanceIndex.class);
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
      FL.add(getLink(CampusTariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys"),CampusFinance.FRAME_NAME),1,1);
      FL.add(getLink(CampusAccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys"),CampusFinance.FRAME_NAME),1,2);
      FL.add(getLink(CampusTariffIndexEditor.class,iwrb.getLocalizedString("indexes","Indexes"),CampusFinance.FRAME_NAME),1,3);
      FL.add(getLink(CampusTariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs"),CampusFinance.FRAME_NAME),1,4);
      FL.add(getLink(CampusAssessments.class,iwrb.getLocalizedString("assessment","Assessment"),CampusFinance.FRAME_NAME),1,5);
      FL.add(getLink(PhoneFiles.class,iwrb.getLocalizedString("phonefiles","Phone files"),CampusFinance.FRAME_NAME),1,6);
      FL.add(getLink(CampusEntryGroups.class,iwrb.getLocalizedString("bunks","Bunks"),CampusFinance.FRAME_NAME),1,7);
      return FL;
  }

  public Link getLink(Class cl,String name,String target){
    Link L = new Link(name,cl);
    L.setTarget(target );
    L.setFontSize(2);
    return L;
  }


}
