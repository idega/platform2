package com.idega.block.poll.presentation;





import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import com.idega.block.poll.business.PollBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;



public class PollQuestionEditor extends IWAdminWindow{



private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";

private boolean isAdmin = false;

private boolean save = false;

private boolean update = false;

private int pollQuestionID = -1;

private int pollID = -1;

private static String prmQuestionParameter = "question";

private static String prmInformationParameter = "information";

private static String prmStartDateParameter = "start_date";

private static String prmEndDateParameter = "end_date";



private IWBundle iwb;

private IWResourceBundle iwrb;



public PollQuestionEditor(){

  setWidth(430);

  setHeight(330);

  setUnMerged();

  setMethod("get");

}



  public void main(IWContext iwc) throws Exception {

    /**

     * @todo permission

     */

    isAdmin = true; //AccessControl.hasEditPermission(this,iwc);

    iwb = getBundle(iwc);

    iwrb = getResourceBundle(iwc);

    addTitle(iwrb.getLocalizedString("poll_question_editor","Poll Question Editor"));

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



  private void processForm(IWContext iwc, int iLocaleId, String sLocaleId) {

    if ( iwc.getParameter(Poll._prmPollID) != null ) {

      try {

        pollID = Integer.parseInt(iwc.getParameter(Poll._prmPollID));

      }

      catch (NumberFormatException e) {

        pollID = -1;

      }

    }



    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {

      try {

        pollQuestionID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION));

      }

      catch (NumberFormatException e) {

        pollQuestionID = -1;

      }

    }



    /*if ( sLocaleId != null ) {

      savePollQuestion(iwc,iLocaleId);

    }*/



    if ( iwc.getParameter(PollBusiness._PARAMETER_MODE) != null ) {

      if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {

        closePollQuestion(iwc);

      }

      else if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {

        if ( pollID != -1 )

          savePollQuestion(iwc,iLocaleId);

        else

          closePollQuestion(iwc);

      }

    }



    if ( (String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {

      try {

        pollQuestionID = Integer.parseInt((String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION));

      }

      catch (NumberFormatException e) {

        pollQuestionID = -1;

      }

    }



    if ( pollQuestionID != -1 ) {

      if ( iwc.getParameter(PollBusiness._PARAMETER_DELETE) != null ) {

        deletePollQuestion(iwc);

      }

      else {

        update = true;

      }

    }



    initializeFields(iLocaleId);

  }



  private void initializeFields(int iLocaleID) {

    String pollQuestion = PollBusiness.getLocalizedQuestion(pollQuestionID,iLocaleID);

    String pollInformation = PollBusiness.getLocalizedInformation(pollQuestionID,iLocaleID);



    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);

      localeDrop.setToSubmit();

      localeDrop.setSelectedElement(Integer.toString(iLocaleID));

    addLeft(iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);



    TextInput questionInput = new TextInput(prmQuestionParameter);

      questionInput.setLength(40);

      if ( update && pollQuestion != null ) {

        questionInput.setContent(pollQuestion);

      }



    TextArea infoArea = new TextArea(prmInformationParameter,40,5);

      if ( update && pollInformation != null ) {

        infoArea.setContent(pollInformation);

      }





    IWTimestamp stampur = new IWTimestamp();



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

    addLeft(iwrb.getLocalizedString("information","Information")+":",infoArea,true);

    addLeft(iwrb.getLocalizedString("start_date","Start date:"),startDate,true);

    addLeft(iwrb.getLocalizedString("end_date","End date:"),endDate,true);

    addHiddenInput(new HiddenInput(Poll._prmPollID,Integer.toString(pollID)));

    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID)));

    addHiddenInput(new HiddenInput("iLocaleID",Integer.toString(iLocaleID)));



    addSubmitButton(new SubmitButton(iwrb.getLocalizedImageButton("close","CLOSE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));

    addSubmitButton(new SubmitButton(iwrb.getLocalizedImageButton("save","SAVE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));

  }



  private void deletePollQuestion(IWContext iwc) {

    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION);

    PollBusiness.deletePollQuestion(pollQuestionID);

    setParentToReload();

    close();

  }



  private void savePollQuestion(IWContext iwc,int iLocaleID) {

    String pollQuestionString = iwc.getParameter(prmQuestionParameter);

    String pollInformationString = iwc.getParameter(prmInformationParameter);

    String pollStartDate = iwc.getParameter(prmStartDateParameter);

    String pollEndDate = iwc.getParameter(prmEndDateParameter);



    String localeString = iwc.getParameter("iLocaleID");

    int _pollQuestionID = -1;

    int _userID = -1;



    try {

      _userID = LoginBusinessBean.getUser(iwc).getID();

    }

    catch (Exception e) {

      _userID = -1;

    }



    if ( pollQuestionString == null || pollQuestionString.length() == 0 ) {

      pollQuestionString = iwrb.getLocalizedString("no_text","No question entered");

    }

    if ( localeString != null ) {

      _pollQuestionID = PollBusiness.savePollQuestion(_userID,pollID,pollQuestionID,pollQuestionString,pollInformationString,pollStartDate,pollEndDate,Integer.parseInt(localeString));

    }

    iwc.setSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(_pollQuestionID));

  }



  private void closePollQuestion(IWContext iwc) {

    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION);

    iwc.setSessionAttribute(PollQuestionChooser.prmQuestions,Integer.toString(pollQuestionID));

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
