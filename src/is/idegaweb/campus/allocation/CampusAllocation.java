/*
 * $Id: CampusAllocation.java,v 1.9 2001/10/02 00:13:56 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationFinder;
import is.idegaweb.campus.entity.SystemProperties;
import java.util.List;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.allocation.AllocationMenu;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAllocation extends JModuleObject{

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  public final static String FRAME_NAME = "cal_rightFrame";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

   public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    SysPropsSetter.isSysPropsInMemoryElseLoad(modinfo);

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

  public ModuleObject getLinkTable(){
      Table FL = new Table();
      //FL.setListpadding(1);
      FL.add(getLink(CampusApprover.class,iwrb.getLocalizedString("applications","Applications"),CampusAllocation.FRAME_NAME),1,1);;
      FL.add(getLink(CampusContracts.class,iwrb.getLocalizedString("contracts","Contracts"),CampusAllocation.FRAME_NAME),1,2);
      FL.add(getLink(RoughOrderForm.class,iwrb.getLocalizedString("roughorder","Rough order"),CampusAllocation.FRAME_NAME),1,3);
      FL.add(getLink(CampusAllocator.class,iwrb.getLocalizedString("allocate","Allocate"),CampusAllocation.FRAME_NAME),1,4);
      FL.add(getLink(EmailSetter.class,iwrb.getLocalizedString("emails","Emails"),CampusAllocation.FRAME_NAME),1,5);
      FL.add(getLink(ContractTextSetter.class,iwrb.getLocalizedString("contracttexts","Contract Texts"),CampusAllocation.FRAME_NAME),1,6);
      FL.add(getLink(SubjectMaker.class,iwrb.getLocalizedString("subjects","Subjects"),CampusAllocation.FRAME_NAME),1,7);
      FL.add(getLink(AprtTypePeriodMaker.class,iwrb.getLocalizedString("periods","Periods"),CampusAllocation.FRAME_NAME),1,8);
      FL.add(getLink(SysPropsSetter.class,iwrb.getLocalizedString("properties","Properties"),CampusAllocation.FRAME_NAME),1,9);
      return FL;
  }

  public Link getLink(Class cl,String name,String target){
    Link L = new Link(name,cl);
    L.setTarget(target );
    L.setFontSize(2);
    return L;
  }

   public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}