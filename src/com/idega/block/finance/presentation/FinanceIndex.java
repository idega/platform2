/*
 * $Id: FinanceIndex.java,v 1.1 2001/12/17 00:34:16 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.finance.presentation;


import com.idega.presentation.text.*;
import com.idega.presentation.ui.IFrame;
import com.idega.presentation.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.*;
import com.idega.block.finance.business.FinanceObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.text.presentation.TextReader;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class FinanceIndex extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
  public final static String FRAME_NAME = "fin_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private int iCategoryId = -1;
  private List FinanceObjects = null;

  public FinanceIndex() {

  }
  public FinanceIndex(int iCategoryId){
    this.iCategoryId =  iCategoryId;
  }
  public void setCategoryId(int iCategoryId){
     this.iCategoryId =  iCategoryId;
  }
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
/*
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
*/
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
    /*
    myTable.add(getLinkTable() ,1,1);

    IFrame iFrame2 = new IFrame(FRAME_NAME);
     //iFrame2.setSrc(FinanceIndex.class);
      iFrame2.setWidth("100%");
      iFrame2.setHeight("100%");
      iFrame2.setBorder(IFrame.FRAMEBORDER_OFF);
      iFrame2.setScrolling(IFrame.SCROLLING_YES);
      iFrame2.setAlignment(IFrame.ALIGN_LEFT);
      iFrame2.setStyle("border: 1 solid #000000");
      myTable.add(iFrame2,2,1);
    */
    add(getBoxLinks());
  }

  public PresentationObject getLinkTable(){
      Table FL = new Table();
      FL.setWidth("100%");
      //FL.setListpadding(1);
      int row = 1;
      FL.add(getLink(PaymentTypeEditor.class,iwrb.getLocalizedString("payment_types","Payment types"),FRAME_NAME),1,row++);
      FL.add(getLink(TariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys"),FRAME_NAME),1,row++);
      FL.add(getLink(AccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys"),FRAME_NAME),1,row++);
      FL.add(getLink(TariffIndexEditor.class,iwrb.getLocalizedString("indexes","Indexes"),FRAME_NAME),1,row++);
      FL.add(getLink(TariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs"),FRAME_NAME),1,row++);
      FL.add(getLink(TariffAssessments.class,iwrb.getLocalizedString("assessment","Assessment"),FRAME_NAME),1,row++);
      //FL.add(getLink(PhoneFiles.class,iwrb.getLocalizedString("phonefiles","Phone files"),CampusFinance.FRAME_NAME),1,6);
      FL.add(getLink(EntryGroups.class,iwrb.getLocalizedString("bunks","Bunks"),FRAME_NAME),1,row++);
      FL.add(getLink(Accounts.class,iwrb.getLocalizedString("accounts","Accounts"),FRAME_NAME),1,row++);
      if(FinanceObjects != null){
        java.util.Iterator I = FinanceObjects.iterator();
        FinanceObject obj;
        while(I.hasNext()){
          obj = (FinanceObject) I.next();
          FL.add(getLink(obj.getClass(),iwrb.getLocalizedString(obj.getKey(),obj.getValue()),FRAME_NAME),1,row++);
        }
      }
      //FL.add(getLink(CampusTariffReports.class,iwrb.getLocalizedString("reports","Reports"),CampusFinance.FRAME_NAME),1,8);
      return FL;
  }

  public PresentationObject getBoxLinks(){
    Table frame = new Table(3,3);
    frame.setWidth("100%");
    frame.setHeight("100%");
    Table box = new Table();
      int row = 1;
      box.add(getLink(PaymentTypeEditor.class,iwrb.getLocalizedString("payment_types","Payment types")),1,row++);
      box.add(getLink(TariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys")),1,row++);
      box.add(getLink(AccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys")),1,row++);
      box.add(getLink(TariffIndexEditor.class,iwrb.getLocalizedString("indexes","Indexes")),1,row++);
      box.add(getLink(TariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs")),1,row++);
      box.add(getLink(TariffAssessments.class,iwrb.getLocalizedString("assessment","Assessment")),1,row++);
      //FL.add(getLink(PhoneFiles.class,iwrb.getLocalizedString("phonefiles","Phone files"),CampusFinance.FRAME_NAME),1,6);
      box.add(getLink(EntryGroups.class,iwrb.getLocalizedString("bunks","Bunks")),1,row++);
      box.add(getLink(Accounts.class,iwrb.getLocalizedString("accounts","Accounts")),1,row++);
    frame.add(box,2,2);
    return frame;
  }

  public Link getLink(Class cl,String name,String target){
    Link L = new Link(name,cl);
    L.addParameter(Finance.getCategoryParameter(iCategoryId));
    L.setTarget(target );
    L.setFontSize(2);
    return L;
  }

   public Link getLink(Class cl,String name){
    Link L = new Link(name,cl);
    L.addParameter(Finance.getCategoryParameter(iCategoryId));
    //L.setTarget(target );
    L.setFontSize(2);
    return L;
  }

  public void addFinanceObject(FinanceObject obj){
    if(FinanceObjects == null)
      FinanceObjects = new Vector();
    FinanceObjects.add(obj);
  }

  public void addFinanceObjectAll(java.util.Collection coll){
    if(FinanceObjects == null)
      FinanceObjects = new Vector();
    FinanceObjects.addAll(coll);
  }

  public synchronized Object clone() {
    FinanceIndex obj = null;
    try {
      obj = (FinanceIndex)super.clone();
      obj.FinanceObjects  = FinanceObjects;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
