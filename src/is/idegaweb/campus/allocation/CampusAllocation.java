/*
 * $Id: CampusAllocation.java,v 1.2 2001/06/25 09:59:12 aron Exp $
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
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.finance.presentation.*;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationFinder;
import java.util.List;

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
      if(isAdmin)
        this.addMain((makeSubjectTable()));
      else
        this.addMain(new Text("Ekki Réttindi"));

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

  public ModuleObject makeSubjectTable(){
    List L = ApplicationFinder.ListOfSubject();
    Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    Table Left = new Table();
      Left.setCellpadding(0);
      Left.setCellspacing(0);
    Table Right = new Table();
      Right.setCellpadding(0);
      Right.setCellspacing(0);
    Frame.add(Left,1,1);
    Frame.add(Right,3,1);
    if(L != null){
      String sParameter = "app_sub_id";
      ApplicationSubject AS;
      int len = L.size();
      for (int i = 0; i < len; i++) {
        AS = (ApplicationSubject) L.get(i);
        RadioButton R = new RadioButton(sParameter,String.valueOf(AS.getID()));
        Left.add(R,1,i+1);
        Left.add(AS.getName(),2,i+1);
      }
    }
    else{
      Left.add(new Text("Ekkert úthlutunartímabil til"));
    }
    Link Approve = new Link("Samþykkja nýskráðar umsóknir");
    Link Contracts = new Link("Samningar");
    Link Waitinglist = new Link("Biðlistar");
    Link RoughOrder = new Link("Grófraða úthlutun");
    Link Allocate = new Link("Úthluta");
    Link Subject = new Link("Nýtt umsóknartímabil");

    Right.add(Approve,1,1);
    Right.add(Contracts,1,2);
    Right.add(Waitinglist,1,3);
    Right.add(RoughOrder,1,4);
    Right.add(Allocate,1,5);
    Right.add(Subject,1,6);

    return Frame;

  }
}