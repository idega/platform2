/*
 * $Id: CampusAllocation.java,v 1.1 2001/06/22 11:34:14 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAllocation extends KeyEditor{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "fin_action";


  public CampusAllocation(String sHeader) {
    super(sHeader);
  }

  protected void control(ModuleInfo modinfo){

      this.makeView();
      this.addHeader((this.makeLinkTable(0)));
      this.addHeader((Text.getBreak()));

  }

  public ModuleObject makeLinkTable(int menuNr){
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
    Link Link2 = new Link("Bókhaldsliðir","/finance/accountkey.jsp");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link("Gjaldliðir","/finance/tariffkey.jsp");
    Link3.setFontColor(this.LightColor);
    Link3.addParameter(this.strAction,String.valueOf(this.ACT3));
    Link Link4 = new Link("Gjöld","/finance/tariff.jsp");
    Link4.setFontColor(this.LightColor);
    Link4.addParameter(this.strAction,String.valueOf(this.ACT4));
    Link Link5 = new Link("Álagning","/finance/assessment.jsp");
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