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

public class PollQuestionEditor extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
private boolean isAdmin = false;
private boolean save = false;
private boolean update = false;
private int pollQuestionID = -1;
private static String prmQuestionParameter = "poll.question";

private IWBundle iwb;
private IWResourceBundle iwrb;

public PollQuestionEditor(){
  setWidth(570);
  setHeight(430);
  setUnMerged();
}

  public void main(ModuleInfo modinfo) throws Exception {
    isAdmin = AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("poll_admin","Poll Question Editor"));
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
        closePollQuestion(modinfo);
      }
      else if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {
        savePollQuestion(modinfo,iLocaleId);
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

    addLeft(iwrb.getLocalizedString("question","Question")+":",questionInput,true);

    addSubmitButton(new SubmitButton(iwrb.getImage("close.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(iwrb.getImage("save.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));
  }

  private void deletePollQuestion(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    PollBusiness.deletePollQuestion(pollQuestionID);
    setParentToReload();
    close();
  }

  private void savePollQuestion(ModuleInfo modinfo,int iLocaleID) {
    String pollQuestionString = modinfo.getParameter(this.prmQuestionParameter);
    if ( pollQuestionString == null ) {
      pollQuestionString = iwrb.getLocalizedString("no_text","No question entered");
    }

    PollBusiness.savePollQuestion(pollQuestionID,pollQuestionString,iLocaleID);
    setParentToReload();
    close();
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