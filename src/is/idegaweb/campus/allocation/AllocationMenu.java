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
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class AllocationMenu extends KeyEditor{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "fin_action";


  public AllocationMenu(String sHeader) {
    super(sHeader);
  }

  protected void control(ModuleInfo modinfo){


      if(isAdmin)
        this.add((makeSubjectTable()));
      else
        this.add(new Text("Ekki Réttindi"));

  }

  public ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);
    return LinkTable;
  }

  public ModuleObject makeSubjectTable(){
    //List L = ApplicationFinder.listOfSubject();
    Table Frame = new Table(3,2);
      Frame.setCellpadding(0);
      Frame.setCellspacing(0);
    Table Left = new Table();
      Left.setCellpadding(0);
      Left.setCellspacing(0);
    Table Right = new Table();
      Right.setCellpadding(0);
      Right.setCellspacing(0);
    Form myForm = new Form();
    myForm.add(Left);
    Frame.add(myForm,1,1);
    Frame.setWidth(2,"40");
    Frame.add(Right,3,1);
/*
    if(L != null){
      String sParameter = "app_sub_id";
      ApplicationSubject AS;
      int len = L.size();
      for (int i = 0; i < len; i++) {
        AS = (ApplicationSubject) L.get(i);
        RadioButton R = new RadioButton(sParameter,String.valueOf(AS.getID()));
        Left.add(R,1,i+1);
        Left.add(AS.getDescription(),2,i+1);
      }
    }
    else{
      Left.add(new Text("Ekkert úthlutunartímabil til"));
    }
*/

    Link Approve = new Link("Samþykkja nýskráðar umsóknir","/allocation/approve.jsp");
    Link Contracts = new Link("Samningar","/allocation/contracts.jsp");
    Link Waitinglist = new Link("Biðlistar","/allocation/waitinglists.jsp");
    Link RoughOrder = new Link("Grófraða úthlutun","/allocation/roughorder.jsp");
    Link Allocate = new Link("Úthluta","/allocation/allocation.jsp");
    Link Subject = new Link("Nýtt umsóknartímabil","/allocation/subject.jsp");
    Link Properties = new Link("Kerfisstillingar","/allocation/sysprops.jsp");
    Link Emails = new Link("Tölvupóstar","/allocation/emails.jsp");
    Link ContractText = new Link("Samningstextar","/allocation/contracttext.jsp");

    Right.add(Approve,1,1);
    Right.add(Contracts,1,2);
    Right.add(Waitinglist,1,3);
    Right.add(RoughOrder,1,4);
    Right.add(Allocate,1,5);
    Right.add(Subject,1,6);
    Right.add(Properties,1,7);
    Right.add(Emails,1,8);
    Right.add(ContractText,1,9);

    return Frame;
  }
}