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

public class PollAnswerEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
private boolean isAdmin = false;
private boolean save = false;
private boolean update = false;
private int pollAnswerID = -1;
private int pollQuestionID = -1;
private static String prmAnswerParameter = "poll.answer";

private IWBundle iwb;
private IWResourceBundle iwrb;

public PollAnswerEditor(){
  setWidth(570);
  setHeight(430);
  setUnMerged();
}

  public void main(ModuleInfo modinfo) throws Exception {
    isAdmin = AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("poll_admin","Poll Answer Editor"));
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
    if ( modinfo.getParameter(PollBusiness._PARAMETER_POLL_ANSWER) != null ) {
      try {
        pollAnswerID = Integer.parseInt(modinfo.getParameter(PollBusiness._PARAMETER_POLL_ANSWER));
        modinfo.setApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(pollAnswerID));
      }
      catch (NumberFormatException e) {
        pollAnswerID = -1;
      }
    }
    else if ( (String) modinfo.getApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER) != null ) {
      try {
        pollAnswerID = Integer.parseInt((String) modinfo.getApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER));
      }
      catch (NumberFormatException e) {
        pollAnswerID = -1;
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
    else if ( (String) modinfo.getApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {
      try {
        pollQuestionID = Integer.parseInt((String) modinfo.getApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION));
      }
      catch (NumberFormatException e) {
        pollQuestionID = -1;
      }
    }

    if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE) != null ) {
      if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {
        closePollAnswer(modinfo);
      }
      else if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {
        savePollAnswer(modinfo,iLocaleId);
      }
    }

    if ( pollAnswerID != -1 ) {
      if ( modinfo.getParameter(PollBusiness._PARAMETER_DELETE) != null ) {
        deletePollQuestion();
      }
      else {
        update = true;
      }
    }

    initializeFields(iLocaleId);
  }

  private void initializeFields(int iLocaleID) {
    String pollAnswer = PollBusiness.getLocalizedAnswer(pollAnswerID,iLocaleID);

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleID));
    addLeft(iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);

    TextInput questionInput = new TextInput(prmAnswerParameter);
      questionInput.setLength(40);
      if ( update && pollAnswer != null ) {
        questionInput.setContent(pollAnswer);
      }

    addLeft(iwrb.getLocalizedString("answer","Answer")+":",questionInput,true);

    addSubmitButton(new SubmitButton(iwrb.getImage("close.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(iwrb.getImage("save.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));
  }

  private void closePollAnswer(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER);
    close();
  }

  private void savePollAnswer(ModuleInfo modinfo,int iLocaleID) {
    String pollAnswerString = modinfo.getParameter(this.prmAnswerParameter);
    if ( pollAnswerString == null ) {
      pollAnswerString = iwrb.getLocalizedString("no_text","No question entered");
    }

    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER);
    PollBusiness.savePollAnswer(pollQuestionID,pollAnswerID,pollAnswerString,iLocaleID);
    setParentToReload();
    close();
  }

  private void deletePollQuestion() {
    PollBusiness.deletePollAnswer(pollAnswerID);
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