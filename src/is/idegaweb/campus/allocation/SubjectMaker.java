package is.idegaweb.campus.allocation;

import is.idegaweb.campus.presentation.Edit;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.finance.presentation.*;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.building.data.*;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.util.List;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class SubjectMaker extends ModuleObjectContainer{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "fin_action";
  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public SubjectMaker() {

  }

  protected void control(ModuleInfo modinfo){


      if(isAdmin){
        if(modinfo.getParameter("save") != null){
          doUpdate(modinfo);
        }
        else if(modinfo.getParameter("delete")!= null){
          doDelete(modinfo);
        }
        this.add(makeInputTable());
      }
      else
        this.add(new Text(iwrb.getLocalizedString("access_denied","Access Denied")));

  }

  public ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  public ModuleObject makeInputTable(){

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
    List L = ApplicationFinder.listOfSubject();
    Table T = new Table();
    TextInput Description = new TextInput("app_subj_desc");
    DateInput ExpireDate = new DateInput("app_subj_xdate",true);
    ExpireDate.setDate(idegaTimestamp.RightNow().getSQLDate());
    SubmitButton SaveButton = new SubmitButton("save","Save");
    T.add(iwrb.getLocalizedString("description", "Description") +" :",1,1);
    T.add(iwrb.getLocalizedString("expiredate", "Expiredate") +" :",2,1);
    T.add(Description,1,2);
    T.add(ExpireDate,2,2);
    T.add(SaveButton,3,2);
    if(L != null){
      int len = L.size();
      int a = 3;
      for (int i = 0; i < len; i++) {
        ApplicationSubject AS = (ApplicationSubject) L.get(i);
        T.add(Edit.formatText(AS.getDescription()),1,a);
        T.add(Edit.formatText(new idegaTimestamp(AS.getExpires()).getISLDate()),2,a);
        T.add((getDeleteLink(AS)),3,a);
        a++;
      }

    }
    Form F = new Form();
    F.add(T);
    Right.add(F);

    return Frame;
  }

  public Link getDeleteLink(ApplicationSubject AS){
    Link L = new Link("X");
    L.addParameter("delete",AS.getID());
    return L;
  }

  public void doDelete(ModuleInfo modinfo){
    try {
      int id = Integer.parseInt(modinfo.getParameter("delete"));
      ApplicationSubject AS = new ApplicationSubject(id);
      AS.delete();
    }
    catch (Exception ex) {

    }

  }

  public void doUpdate(ModuleInfo modinfo){
    String sDesc= modinfo.getParameter("app_subj_desc").trim();
    String sDate = modinfo.getParameter("app_subj_xdate");
    if(sDesc.length() > 0){
      ApplicationSubject AS = new ApplicationSubject();
      AS.setDescription(sDesc);
      AS.setExpires(new idegaTimestamp(sDate).getSQLDate());
      try {
        AS.insert();
      }
      catch (SQLException ex) {

      }
    }
  }

   public void main(ModuleInfo modinfo){
    try{
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){
      isAdmin = false;
    }
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    control(modinfo);
  }

}