/*
 * $Id: CampusAllocation.java,v 1.2 2001/12/19 13:17:06 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;



import is.idega.idegaweb.campus.presentation.EmailSetter;
import is.idega.idegaweb.campus.block.building.presentation.AprtTypePeriodMaker;
import is.idega.idegaweb.campus.block.application.presentation.*;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.SysPropsSetter;
import is.idega.idegaweb.campus.block.allocation.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.*;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationFinder;

import java.util.List;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;




/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAllocation extends Block{

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.allocation";
  public final static String FRAME_NAME = "cal_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  public static String prmClass = "alloc_clss";

   public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    SysPropsSetter.isSysPropsInMemoryElseLoad(iwc);

    Table T = new Table();
    T.setWidth("100%");
   // T.setHeight("100%");
    T.setCellpadding(0);
    T.setCellspacing(0);

    String className = null;
    if(iwc.isParameterSet(prmClass)){
      className = iwc.getParameter(prmClass);
      iwc.setSessionAttribute(prmClass,className);
    }
    else if(iwc.getSessionAttribute(prmClass)!=null){
      className = (String) iwc.getSessionAttribute(prmClass);
    }
    if(className !=null){
      try{
       T.add(getLinkTable(),1,1);

      Object obj =  Class.forName(className).newInstance();
      if(obj instanceof PresentationObject)
        T.add((PresentationObject)obj,1,2);

      }
      catch(Exception e){}
    }

    else{
      T.add(getBoxedLinks(),1,1);
    }
    add(T);
  }
  private void getFrame(){
    Table myTable = new Table(2,2);
      myTable.setBorder(1);
      myTable.setBorderColor("#000000");
      myTable.mergeCells(2,1,2,2);
      myTable.setWidth(1,"130");
      myTable.setWidth(2,"100%");
      myTable.setCellpadding(3);
      myTable.setWidth("100%");
      myTable.setHeight("100%");
      myTable.setColumnAlignment(1,"left");
      //myTable.setColumnAlignment(2,"left");

      myTable.setBorder(0);
      myTable.setVerticalAlignment(1,1,"top");
      myTable.setVerticalAlignment(2,1,"top");
      myTable.setVerticalAlignment(1,2,"top");
/*
    IFrame iFrame = new IFrame("menuFrame");
      iFrame.setSrc(AllocationMenu.class);
      iFrame.setWidth(120);
      iFrame.setHeight(250);
      iFrame.setBorder(IFrame.FRAMEBORDER_OFF);
      iFrame.setScrolling(IFrame.SCROLLING_YES);
      iFrame.setStyle("border: 1 solid #000000");
      //iFrame.setAlignment(IFrame.ALIGN_LEFT);
      myTable.add(iFrame,1,1);
*/
    myTable.add(getLinkTable(),1,1);
    IFrame iFrame2 = new IFrame(FRAME_NAME);
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
      FL.add(getLink(CampusApprover.class,iwrb.getLocalizedString("applications","Applications")),1,1);;
      FL.add(getLink(CampusContracts.class,iwrb.getLocalizedString("contracts","Contracts")),2,1);
      FL.add(getLink(RoughOrderForm.class,iwrb.getLocalizedString("roughorder","Rough order")),3,1);
      FL.add(getLink(CampusAllocator.class,iwrb.getLocalizedString("allocate","Allocate")),4,1);
      FL.add(getLink(EmailSetter.class,iwrb.getLocalizedString("emails","Emails")),5,1);
      FL.add(getLink(ContractTextSetter.class,iwrb.getLocalizedString("contracttexts","Contract Texts")),6,1);
      FL.add(getLink(SubjectMaker.class,iwrb.getLocalizedString("subjects","Subjects")),7,1);
      FL.add(getLink(AprtTypePeriodMaker.class,iwrb.getLocalizedString("periods","Periods")),8,1);
      FL.add(getLink(SysPropsSetter.class,iwrb.getLocalizedString("properties","Properties")),9,1);
      return FL;
  }

  public PresentationObject getBoxedLinks(){
    Table frame = new Table(3,3);
    frame.setWidth("100%");
    frame.setHeight("100%");
    Table box = new Table();
      int row = 1;
      box.add(getLink(CampusApprover.class,iwrb.getLocalizedString("applications","Applications")),1,1);;
      box.add(getLink(CampusContracts.class,iwrb.getLocalizedString("contracts","Contracts")),2,1);
      box.add(getLink(RoughOrderForm.class,iwrb.getLocalizedString("roughorder","Rough order")),3,1);
      box.add(getLink(CampusAllocator.class,iwrb.getLocalizedString("allocate","Allocate")),4,1);
      box.add(getLink(EmailSetter.class,iwrb.getLocalizedString("emails","Emails")),5,1);
      box.add(getLink(ContractTextSetter.class,iwrb.getLocalizedString("contracttexts","Contract Texts")),6,1);
      box.add(getLink(SubjectMaker.class,iwrb.getLocalizedString("subjects","Subjects")),7,1);
      box.add(getLink(AprtTypePeriodMaker.class,iwrb.getLocalizedString("periods","Periods")),8,1);
      box.add(getLink(SysPropsSetter.class,iwrb.getLocalizedString("properties","Properties")),9,1);

      box.setColor(Edit.colorLight);
    frame.add(box,2,2);
    return frame;
  }

  public Link getLink(Class cl,String name,String target){
    Link L = new Link(name,cl);
    L.setTarget(target );
    L.setFontSize(2);
    return L;
  }

    public Link getLink(Class cl,String name){
    Link L = new Link(name);
    //L.addParameter(Finance.getCategoryParameter(iCategoryId));
    L.addParameter(getFinanceObjectParameter(cl));
    L.setFontSize(1);
    L.setFontColor(Edit.colorDark);
    return L;
  }

  public Parameter getFinanceObjectParameter(Class objectClass){
    return new Parameter(prmClass,objectClass.getName());
  }

   public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
