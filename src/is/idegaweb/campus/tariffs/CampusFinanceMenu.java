/*
 * $Id: CampusFinanceMenu.java,v 1.7 2001/10/02 00:13:56 aron Exp $
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
import is.idegaweb.campus.phone.presentation.PhoneFiles;
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

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void makeLinkTable(){
      setListpadding(1);
      addToList(TariffKeyEditor.class,iwrb.getLocalizedString("tariff_keys","Tariff keys"),CampusFinance.FRAME_NAME);
      addToList(AccountKeyEditor.class,iwrb.getLocalizedString("account_keys","Account keys"),CampusFinance.FRAME_NAME);
      addToList(TariffIndexEditor.class,iwrb.getLocalizedString("indexes","Indexes"),CampusFinance.FRAME_NAME);
      addToList(CampusTariffEditor.class,iwrb.getLocalizedString("tariff","Tariffs"),CampusFinance.FRAME_NAME);
      addToList(CampusTariffer.class,iwrb.getLocalizedString("assessment","Assessment"),CampusFinance.FRAME_NAME);
      addToList(PhoneFiles.class,iwrb.getLocalizedString("phonefiles","Phone files"),CampusFinance.FRAME_NAME);

  }
}