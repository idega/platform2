package com.idega.projects.campus.tariffs;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class CampusFinance extends KeyEditor{

  protected final static int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "fin_action";


  public CampusFinance(String sHeader) {
    super(sHeader);
  }

  protected void control(ModuleInfo modinfo){

    try{
      this.makeView();
      this.addHeader((this.makeLinkTable(0)));
      this.addHeader((Text.getBreak()));

      if(modinfo.getParameter(strAction) == null){
        //add(this.makeLinkTable(0));
      }
      if(modinfo.getParameter(strAction) != null){
        String sAct = modinfo.getParameter(strAction);

        int iAct = Integer.parseInt(sAct);
        switch (iAct) {
          case ACT2 : this.addMain(new AccountKeyEditor("Bókhaldsliðir"));break;
          case ACT3 : this.addMain(new TariffKeyEditor("Gjaldliðir"));    break;
          case ACT4 : this.addMain(new CampusTariffEditor("Gjöld"));      break;
          case ACT5 : this.addMain(new CampusTariffer("Álagning"));      break;
        }
      }
      else if(modinfo.getParameter(CampusTariffEditor.strAction)!=null)
        this.addMain(new CampusTariffEditor("Gjöld"));
      else if(modinfo.getParameter(TariffKeyEditor.strAction)!=null)
        this.addMain(new TariffKeyEditor("Gjaldliðir"));
      else if(modinfo.getParameter(AccountKeyEditor.strAction)!=null)
        this.addMain(new AccountKeyEditor("Bókhaldsliðir"));
      else if(modinfo.getParameter(CampusTariffer.strAction)!=null)
        this.addMain(new AccountKeyEditor("Álagning"));
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  protected ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);
    int last = 6;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(this.DarkColor);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link("Heim");
    Link1.setFontColor(this.LightColor);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link("Bókhaldsliðir");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link("Gjaldliðir");
    Link3.setFontColor(this.LightColor);
    Link3.addParameter(this.strAction,String.valueOf(this.ACT3));
    Link Link4 = new Link("Gjöld");
    Link4.setFontColor(this.LightColor);
    Link4.addParameter(this.strAction,String.valueOf(this.ACT4));
    Link Link5 = new Link("Álagning");
    Link5.setFontColor(this.LightColor);
    Link5.addParameter(this.strAction,String.valueOf(this.ACT5));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,3,1);
      LinkTable.add(Link4,4,1);
      LinkTable.add(Link5,5,1);
    }
    return LinkTable;
  }
}