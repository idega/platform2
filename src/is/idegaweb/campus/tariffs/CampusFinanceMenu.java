/*
 * $Id: CampusFinanceMenu.java,v 1.2 2001/08/30 05:43:03 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.tariffs;

import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.FrameList;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusFinanceMenu extends FrameList{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6 = 6,ACT7 = 7;
  private final String strAction = "fin_action";
  private String DarkColor = "#27334B";
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public CampusFinanceMenu() {
  }

  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    setLinkStyle("font-family: Verdana, Arial, sans-serif; font-weight: bold; font-size: 7pt; text-decoration: none;");
    makeLinkTable();
    setZebraColors("#FFFFFF","#ECECEC");
  }

  public void makeLinkTable(){
    Link Link6 = new Link(iwrb.getLocalizedString("phone_tariffs","Telephone"),"/finance/phone.jsp");
      Link6.setFontColor(this.DarkColor);
      Link6.addParameter(this.strAction,String.valueOf(this.ACT6));
      Link6.setTarget("rightFrame");
    Link Link7 = new Link(iwrb.getLocalizedString("indexes","Indexes"),"/finance/indexes.jsp");
      Link7.setFontColor(this.DarkColor);
      Link7.addParameter(this.strAction,String.valueOf(this.ACT7));
      Link7.setTarget("rightFrame");

      addToList(TariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys"),CampusFinance.FRAME_NAME);
      addToList(AccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys"),CampusFinance.FRAME_NAME);
      //addToList(Link7);
      addToList(CampusTariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs"),CampusFinance.FRAME_NAME);
      addToList(CampusTariffer.class,iwrb.getLocalizedString("assessment","Assessment"),CampusFinance.FRAME_NAME);
     // addToList(Link6);
    /*
    addToList(CampusApprover.class,iwrb.getLocalizedString("applications","Applications"),CampusAllocation.FRAME_NAME);
    addToList(CampusContracts.class,iwrb.getLocalizedString("contracts","Contracts"),CampusAllocation.FRAME_NAME);
    addToList(RoughOrderForm.class,iwrb.getLocalizedString("roughorder","Rough order"),CampusAllocation.FRAME_NAME);
    addToList(CampusAllocator.class,iwrb.getLocalizedString("allocate","Allocate"),CampusAllocation.FRAME_NAME);
    addToList(EmailSetter.class,iwrb.getLocalizedString("emails","Emails"),CampusAllocation.FRAME_NAME);
    addToList(ContractTextSetter.class,iwrb.getLocalizedString("contracttexts","Contract Texts"),CampusAllocation.FRAME_NAME);
    addToList(SubjectMaker.class,iwrb.getLocalizedString("subjects","Subjects"),CampusAllocation.FRAME_NAME);
    addToList(AprtTypePeriodMaker.class,iwrb.getLocalizedString("periods","Periods"),CampusAllocation.FRAME_NAME);
    addToList(SysPropsSetter.class,iwrb.getLocalizedString("properties","Properties"),CampusAllocation.FRAME_NAME);
    */
  }
}