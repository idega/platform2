package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */


public class SubjectMaker extends CampusBlock{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  protected boolean isAdmin = false;
  


  public String getLocalizedNameKey(){
    return "subjects";
  }

  public String getLocalizedNameValue(){
    return "Subjects";
  }

 

  protected void control(IWContext iwc){


      if(isAdmin){
        if(iwc.getParameter("save") != null){
          doUpdate(iwc);
        }
        else if(iwc.getParameter("delete")!= null){
          doDelete(iwc);
        }
        this.add(makeInputTable());
      }
      else
        this.add(getNoAccessObject(iwc));

  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  public PresentationObject makeInputTable(){

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
    ExpireDate.setDate(IWTimestamp.RightNow().getSQLDate());
    SubmitButton SaveButton = new SubmitButton("save","Save");
    T.add(getHeader(localize("description", "Description")) +" :",1,1);
    T.add(getHeader(localize("expiredate", "Expiredate")) +" :",2,1);
    T.add(Description,1,2);
    T.add(ExpireDate,2,2);
    T.add(SaveButton,3,2);
    if(L != null){
      int len = L.size();
      int a = 3;
      for (int i = 0; i < len; i++) {
        ApplicationSubject AS = (ApplicationSubject) L.get(i);
        T.add(Edit.formatText(AS.getDescription()),1,a);
        T.add(Edit.formatText(new IWTimestamp(AS.getExpires()).getLocaleDate(LocaleUtil.getIcelandicLocale())),2,a);
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

  public void doDelete(IWContext iwc){
    try {
      int id = Integer.parseInt(iwc.getParameter("delete"));
      ApplicationSubject AS = ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).findByPrimaryKeyLegacy(id);
      AS.delete();
    }
    catch (Exception ex) {

    }

  }

  public void doUpdate(IWContext iwc){
    String sDesc= iwc.getParameter("app_subj_desc").trim();
    String sDate = iwc.getParameter("app_subj_xdate");
    if(sDesc.length() > 0){
      ApplicationSubject AS = ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).createLegacy();
      AS.setDescription(sDesc);
      AS.setExpires(new IWTimestamp(sDate).getSQLDate());
      try {
        AS.insert();
      }
      catch (SQLException ex) {

      }
    }
  }

   public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}
