package com.idega.block.application.presentation;

import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectInfo;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.business.ApplicationBusiness;
import com.idega.presentation.Block;
import com.idega.block.IWBlock;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.PresentationObject;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;
import com.idega.util.text.Edit;
import java.util.List;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class ApplicationSubjectMaker extends Block{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "appsmaction";
  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="com.block.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb,core;


  public ApplicationSubjectMaker() {

  }

  protected void control(IWContext iwc){


      if(isAdmin){
        if(iwc.isParameterSet("save")){
          doUpdate(iwc);
        }
        else if(iwc.isParameterSet("delete")){
          doDelete(iwc);
        }
        this.add(makeInputTable());
      }
      else
        this.add(new Text(iwrb.getLocalizedString("access_denied","Access Denied")));

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

  public void doDelete(IWContext iwc){
    int id = Integer.parseInt(iwc.getParameter("delete"));
    ApplicationBusiness.deleteApplicationSubject(id);
  }

  public void doUpdate(IWContext iwc){
    String sDesc= iwc.getParameter("app_subj_desc").trim();
    String sDate = iwc.getParameter("app_subj_xdate");
    if(sDesc.length() > 0){
      ApplicationBusiness.saveApplicationSubject(-1,sDesc,new idegaTimestamp(sDate).getSQLDate());
    }
  }

   public void main(IWContext iwc){
    isAdmin = iwc.hasEditPermission(this);
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    core = iwc.getApplication().getBundle( iwc.getApplication().CORE_BUNDLE_IDENTIFIER );
    control(iwc);
  }

}
