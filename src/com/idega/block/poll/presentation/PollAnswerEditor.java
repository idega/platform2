package com.idega.block.poll.presentation;


import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
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
  setWidth(430);
  setHeight(140);
  setUnMerged();
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    addTitle(iwrb.getLocalizedString("poll_answer_editor","Poll Answer Editor"));
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    String sLocaleId = iwc.getParameter(PollAdminWindow.prmLocale);

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
      processForm(iwc, iLocaleId, sLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId, String sLocaleID) {
    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {
      try {
        pollQuestionID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION));
        iwc.setApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID));
      }
      catch (NumberFormatException e) {
        pollQuestionID = -1;
      }
    }
    else if ( (String) iwc.getApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {
      try {
        pollQuestionID = Integer.parseInt((String) iwc.getApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION));
      }
      catch (NumberFormatException e) {
        pollQuestionID = -1;
      }
    }

    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_ANSWER) != null ) {
      try {
        pollAnswerID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_ANSWER));
        iwc.setApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(pollAnswerID));
      }
      catch (NumberFormatException e) {
        pollAnswerID = -1;
      }
    }

    if ( sLocaleID != null ) {
      savePollAnswer(iwc,iLocaleId,false);
    }

    if ( (String) iwc.getApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER) != null ) {
      try {
        pollAnswerID = Integer.parseInt((String) iwc.getApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER));
      }
      catch (NumberFormatException e) {
        pollAnswerID = -1;
      }
    }

    if ( iwc.getParameter(PollBusiness._PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {
        closePollAnswer(iwc);
      }
      else if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {
        savePollAnswer(iwc,iLocaleId,true);
      }
    }

    if ( pollAnswerID != -1 ) {
      if ( iwc.getParameter(PollBusiness._PARAMETER_DELETE) != null ) {
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
    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID)));
    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(pollAnswerID)));
    addHiddenInput(new HiddenInput("iLocaleID",Integer.toString(iLocaleID)));

    addSubmitButton(new SubmitButton(iwrb.getLocalizedImageButton("close","CLOSE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(iwrb.getLocalizedImageButton("save","SAVE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));
  }

  private void closePollAnswer(IWContext iwc) {
    iwc.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER);
    close();
  }

  private void savePollAnswer(IWContext iwc,int iLocaleID,boolean close) {
    String pollAnswerString = iwc.getParameter(this.prmAnswerParameter);
    String localeString = iwc.getParameter("iLocaleID");

    if ( pollAnswerString == null || pollAnswerString.length() == 0 ) {
      pollAnswerString = iwrb.getLocalizedString("no_text","No answer entered");
    }

    if ( localeString != null ) {
      pollAnswerID = PollBusiness.savePollAnswer(pollQuestionID,pollAnswerID,pollAnswerString,Integer.parseInt(localeString));
    }

    iwc.setApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(pollAnswerID));
    if ( close ) {
      iwc.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_ANSWER);
      setParentToReload();
      close();
    }
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