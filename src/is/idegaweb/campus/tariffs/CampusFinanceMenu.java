/*
 * $Id: CampusFinanceMenu.java,v 1.1 2001/08/27 11:16:36 laddi Exp $
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
    Link Link6 = new Link("Símagjöld","/finance/phone.jsp");
      Link6.setFontColor(this.DarkColor);
      Link6.addParameter(this.strAction,String.valueOf(this.ACT6));
      Link6.setTarget("rightFrame");
    Link Link7 = new Link("Vísitölur","/finance/indexes.jsp");
      Link7.setFontColor(this.DarkColor);
      Link7.addParameter(this.strAction,String.valueOf(this.ACT7));
      Link7.setTarget("rightFrame");

      addToList(TariffKeyEditor.class,"Gjaldliðir",CampusFinance.FRAME_NAME);
      addToList(AccountKeyEditor.class,"Bókhaldsliðir",CampusFinance.FRAME_NAME);
      addToList(Link7);
      addToList(CampusTariffEditor.class,"Gjöld",CampusFinance.FRAME_NAME);
      addToList(CampusTariffer.class,"Álagning",CampusFinance.FRAME_NAME);
      addToList(Link6);
  }
}