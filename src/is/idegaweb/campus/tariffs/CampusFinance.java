/*
 * $Id: CampusFinance.java,v 1.4 2001/08/14 23:44:16 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.tariffs;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusFinance extends KeyEditor{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6 = 6;
  private final String strAction = "fin_action";
   private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public CampusFinance(String sHeader) {
    super(sHeader);
  }

  protected void control(ModuleInfo modinfo){
    this.add((this.makeLinkTable(0)));
  }

  public ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table();

    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);

    Link Link1 = new Link("Heim");
    Link1.setFontColor(this.DarkColor);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link("Bókhaldsliðir","/finance/accountkey.jsp");
    Link2.setFontColor(this.DarkColor);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link("Gjaldliðir","/finance/tariffkey.jsp");
    Link3.setFontColor(this.DarkColor);
    Link3.addParameter(this.strAction,String.valueOf(this.ACT3));
    Link Link4 = new Link("Gjöld","/finance/tariff.jsp");
    Link4.setFontColor(this.DarkColor);
    Link4.addParameter(this.strAction,String.valueOf(this.ACT4));
    Link Link5 = new Link("Álagning","/finance/assessment.jsp");
    Link5.setFontColor(this.DarkColor);
    Link5.addParameter(this.strAction,String.valueOf(this.ACT5));
    Link Link6 = new Link("Símagjöld","/finance/phone.jsp");
    Link6.setFontColor(this.DarkColor);
    Link6.addParameter(this.strAction,String.valueOf(this.ACT6));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,2);
      LinkTable.add(Link3,1,3);
      LinkTable.add(Link4,1,4);
      LinkTable.add(Link5,1,5);
      LinkTable.add(Link6,1,6);
    }
    return LinkTable;
  }
}