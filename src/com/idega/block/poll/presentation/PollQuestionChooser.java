package com.idega.block.poll.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.data.ICLocale;
import com.idega.block.poll.data.*;
import com.idega.block.poll.business.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.text.business.TextFinder;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class PollQuestionChooser extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
private boolean isAdmin = false;
private boolean close = false;
private int pollID = -1;
public static String prmQuestions = "poll.questions";

private IWBundle iwb;
private IWResourceBundle iwrb;

public PollQuestionChooser(){
  setWidth(400);
  setHeight(150);
}

  public void main(ModuleInfo modinfo) throws Exception {
    /**
     * @todo permission
     */
    isAdmin = true; //AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("poll_question_chooser","Poll Question Chooser"));
    Locale currentLocale = modinfo.getCurrentLocale(),chosenLocale;

    String sLocaleId = modinfo.getParameter(PollAdminWindow.prmLocale);

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = TextFinder.getLocale(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    if ( isAdmin ) {
      processForm(modinfo, iLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(ModuleInfo modinfo, int iLocaleId) {
    String pollQuestion = modinfo.getParameter(prmQuestions);
    String pollIDString = modinfo.getParameter(Poll._prmPollID);
    try {
      pollID = Integer.parseInt(pollIDString);
    }
    catch (NumberFormatException e) {
      pollID = -1;
    }

    int pollQuestionID = -1;
    try {
      pollQuestionID = Integer.parseInt(pollQuestion);
    }
    catch (NumberFormatException e) {
      pollQuestionID = -1;
    }

    if ( pollQuestionID != -1 ) {
      setToClose(modinfo, pollQuestionID);
    }
    else {
      Form myForm = new Form();

      DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);
        localeDrop.setAttribute("style",STYLE);
        localeDrop.setToSubmit();
        localeDrop.setSelectedElement(Integer.toString(iLocaleId));

      DropdownMenu questionDrop = PollBusiness.getQuestions(prmQuestions,pollID,iLocaleId);
        questionDrop.setAttribute("style",STYLE);
        questionDrop.setToSubmit();

      Text localeText = this.formatText(iwrb.getLocalizedString("locale","Locale"),true);
      Text questionText = this.formatText(iwrb.getLocalizedString("questions","Questions"),true);

      Table table = new Table(1,2);
        table.setCellpadding(8);
        table.setAlignment("center");
        table.setWidth("100%");

      table.add(localeText,1,1);
      table.add(Text.getBreak(),1,1);
      table.add(localeDrop,1,1);

      table.add(questionText,1,2);
      table.add(Text.getBreak(),1,2);
      table.add(questionDrop,1,2);

      myForm.add(table);
      add(myForm);
    }
  }

  private void setToClose(ModuleInfo modinfo, int pollQuestionID) {
    modinfo.setApplicationAttribute(prmQuestions,Integer.toString(pollQuestionID));
    setParentToReload();
    close();
  }

  private void noAccess() throws IOException,SQLException {
    close();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}