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
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class PollQuestionEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
private boolean isAdmin = false;
private boolean save = false;
private boolean update = false;
private int pollQuestionID = -1;
private int pollID = -1;
private static String prmQuestionParameter = "question";
private static String prmStartDateParameter = "start_date";
private static String prmEndDateParameter = "end_date";

private IWBundle iwb;
private IWResourceBundle iwrb;

public PollQuestionEditor(){
  setWidth(570);
  setHeight(430);
  setUnMerged();
  setMethod("get");
}

  public void main(ModuleInfo modinfo) throws Exception {
    /**
     * @todo permission
     */
    isAdmin = true; //AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("poll_question_editor","Poll Question Editor"));
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
      processForm(modinfo, iLocaleId, sLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(ModuleInfo modinfo, int iLocaleId, String sLocaleId) {
    if ( modinfo.getParameter(Poll._prmPollID) != null ) {
      try {
        pollID = Integer.parseInt(modinfo.getParameter(Poll._prmPollID));
      }
      catch (NumberFormatException e) {
        pollID = -1;
      }
    }

    if ( modinfo.getParameter(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {
      try {
        pollQuestionID = Integer.parseInt(modinfo.getParameter(PollBusiness._PARAMETER_POLL_QUESTION));
        modinfo.setApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID));
      }
      catch (NumberFormatException e) {
        pollQuestionID = -1;
      }
    }

    if ( sLocaleId != null ) {
      savePollQuestion(modinfo,iLocaleId,false);
    }

    if ( (String) modinfo.getApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {
      try {
        pollQuestionID = Integer.parseInt((String) modinfo.getApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION));
      }
      catch (NumberFormatException e) {
        pollQuestionID = -1;
      }
    }

    if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE) != null ) {
      if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {
        closePollQuestion(modinfo);
      }
      else if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {
        if ( pollID != -1 )
          savePollQuestion(modinfo,iLocaleId,true);
        else
          closePollQuestion(modinfo);
      }
    }

    if ( pollQuestionID != -1 ) {
      if ( modinfo.getParameter(PollBusiness._PARAMETER_DELETE) != null ) {
        deletePollQuestion(modinfo);
      }
      else {
        update = true;
      }
    }

    initializeFields(iLocaleId);
  }

  private void initializeFields(int iLocaleID) {
    String pollQuestion = PollBusiness.getLocalizedQuestion(pollQuestionID,iLocaleID);

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleID));
    addLeft(iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);

    TextInput questionInput = new TextInput(prmQuestionParameter);
      questionInput.setLength(40);
      if ( update && pollQuestion != null ) {
        questionInput.setContent(pollQuestion);
      }

    idegaTimestamp stampur = new idegaTimestamp();

    DateInput startDate = new DateInput(prmStartDateParameter,true);
      startDate.setYearRange(stampur.getYear(),stampur.getYear()+10);
      if ( update && PollBusiness.getStartDate(pollQuestionID) != null ) {
        startDate.setDate(new java.sql.Date(PollBusiness.getStartDate(pollQuestionID).getTimestamp().getTime()));
      }

    DateInput endDate = new DateInput(prmEndDateParameter,true);
      endDate.setYearRange(stampur.getYear(),stampur.getYear()+10);
      if ( update && PollBusiness.getEndDate(pollQuestionID) != null ) {
        endDate.setDate(new java.sql.Date(PollBusiness.getEndDate(pollQuestionID).getTimestamp().getTime()));
      }

    addLeft(iwrb.getLocalizedString("question","Question")+":",questionInput,true);
    addLeft(iwrb.getLocalizedString("start_date","Start date:"),startDate,true);
    addLeft(iwrb.getLocalizedString("end_date","End date:"),endDate,true);
    addHiddenInput(new HiddenInput(Poll._prmPollID,Integer.toString(pollID)));
    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID)));
    addHiddenInput(new HiddenInput("iLocaleID",Integer.toString(iLocaleID)));

    addSubmitButton(new SubmitButton(iwrb.getImage("close.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(iwrb.getImage("save.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));
  }

  private void deletePollQuestion(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    PollBusiness.deletePollQuestion(pollQuestionID);
    setParentToReload();
    close();
  }

  private void savePollQuestion(ModuleInfo modinfo,int iLocaleID, boolean close) {
    String pollQuestionString = modinfo.getParameter(prmQuestionParameter);
    String pollStartDate = modinfo.getParameter(prmStartDateParameter);
    String pollEndDate = modinfo.getParameter(prmEndDateParameter);

    String localeString = modinfo.getParameter("iLocaleID");
    int _pollQuestionID = -1;
    int _userID = -1;

    try {
      _userID = LoginBusiness.getUser(modinfo).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }

    if ( pollQuestionString == null || pollQuestionString.length() == 0 ) {
      pollQuestionString = iwrb.getLocalizedString("no_text","No question entered");
    }
    if ( localeString != null ) {
      _pollQuestionID = PollBusiness.savePollQuestion(_userID,pollID,pollQuestionID,pollQuestionString,pollStartDate,pollEndDate,Integer.parseInt(localeString));
    }
    modinfo.setApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(_pollQuestionID));

    if ( close ) {
      modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
      modinfo.setApplicationAttribute(PollQuestionChooser.prmQuestions,Integer.toString(_pollQuestionID));
      setParentToReload();
      close();
    }
  }

  private void closePollQuestion(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    close();
  }

  private void noAccess() throws IOException,SQLException {
    close();
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}