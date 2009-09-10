package com.idega.block.poll.presentation;





import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import com.idega.block.poll.business.PollBusiness;
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

    this.isAdmin = true; //AccessControl.hasEditPermission(this,iwc);

    this.iwb = getBundle(iwc);

    this.iwrb = getResourceBundle(iwc);

    addTitle(this.iwrb.getLocalizedString("poll_question_editor","Poll Question Editor"));

    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;


    String sLocaleId = iwc.getParameter(PollAdminWindow.prmLocale);



    int iLocaleId = -1;

    if(sLocaleId!= null){

      iLocaleId = Integer.parseInt(sLocaleId);

      chosenLocale = ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);

    }

    else{

      chosenLocale = currentLocale;

      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);

    }



    if ( this.isAdmin ) {

      processForm(iwc, iLocaleId, sLocaleId);

    }

    else {

      noAccess();

    }

  }



  private void processForm(IWContext iwc, int iLocaleId, String sLocaleId) {

    if ( iwc.getParameter(Poll._prmPollID) != null ) {

      try {

        this.pollID = Integer.parseInt(iwc.getParameter(Poll._prmPollID));

      }

      catch (NumberFormatException e) {

        this.pollID = -1;

      }

    }



    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {

      try {

        this.pollQuestionID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION));

      }

      catch (NumberFormatException e) {

        this.pollQuestionID = -1;

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

        if ( this.pollID != -1 ) {
			savePollQuestion(iwc,iLocaleId);
		}
		else {
			closePollQuestion(iwc);
		}

      }

    }



    if ( (String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {

      try {

        this.pollQuestionID = Integer.parseInt((String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION));

      }

      catch (NumberFormatException e) {

        this.pollQuestionID = -1;

      }

    }



    if ( this.pollQuestionID != -1 ) {

      if ( iwc.getParameter(PollBusiness._PARAMETER_DELETE) != null ) {

        deletePollQuestion(iwc);

      }

      else {

        this.update = true;

      }

    }



    initializeFields(iLocaleId);

  }



  private void initializeFields(int iLocaleID) {

    String pollQuestion = PollBusiness.getLocalizedQuestion(this.pollQuestionID,iLocaleID);

    String pollInformation = PollBusiness.getLocalizedInformation(this.pollQuestionID,iLocaleID);



    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);

      localeDrop.setToSubmit();

      localeDrop.setSelectedElement(Integer.toString(iLocaleID));

    addLeft(this.iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);



    TextInput questionInput = new TextInput(prmQuestionParameter);

      questionInput.setLength(40);

      if ( this.update && pollQuestion != null ) {

        questionInput.setContent(pollQuestion);

      }



    TextArea infoArea = new TextArea(prmInformationParameter,40,5);

      if ( this.update && pollInformation != null ) {

        infoArea.setContent(pollInformation);

      }





    IWTimestamp stampur = new IWTimestamp();



    DateInput startDate = new DateInput(prmStartDateParameter,true);

      startDate.setYearRange(stampur.getYear(),stampur.getYear()+10);

      if ( this.update && PollBusiness.getStartDate(this.pollQuestionID) != null ) {

        startDate.setDate(new java.sql.Date(PollBusiness.getStartDate(this.pollQuestionID).getTimestamp().getTime()));

      }



    DateInput endDate = new DateInput(prmEndDateParameter,true);

      endDate.setYearRange(stampur.getYear(),stampur.getYear()+10);

      if ( this.update && PollBusiness.getEndDate(this.pollQuestionID) != null ) {

        endDate.setDate(new java.sql.Date(PollBusiness.getEndDate(this.pollQuestionID).getTimestamp().getTime()));

      }



    addLeft(this.iwrb.getLocalizedString("question","Question")+":",questionInput,true);

    addLeft(this.iwrb.getLocalizedString("information","Information")+":",infoArea,true);

    addLeft(this.iwrb.getLocalizedString("start_date","Start date:"),startDate,true);

    addLeft(this.iwrb.getLocalizedString("end_date","End date:"),endDate,true);

    addHiddenInput(new HiddenInput(Poll._prmPollID,Integer.toString(this.pollID)));

    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(this.pollQuestionID)));

    addHiddenInput(new HiddenInput("iLocaleID",Integer.toString(iLocaleID)));



    addSubmitButton(new SubmitButton(this.iwrb.getLocalizedImageButton("close","CLOSE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));

    addSubmitButton(new SubmitButton(this.iwrb.getLocalizedImageButton("save","SAVE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));

  }



  private void deletePollQuestion(IWContext iwc) {

    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION);

    PollBusiness.deletePollQuestion(this.pollQuestionID);

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

      pollQuestionString = this.iwrb.getLocalizedString("no_text","No question entered");

    }

    if ( localeString != null ) {

      _pollQuestionID = PollBusiness.savePollQuestion(_userID,this.pollID,this.pollQuestionID,pollQuestionString,pollInformationString,pollStartDate,pollEndDate,Integer.parseInt(localeString));

    }

    iwc.setSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(_pollQuestionID));

  }



  private void closePollQuestion(IWContext iwc) {

    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION);

    iwc.setSessionAttribute(PollQuestionChooser.prmQuestions,Integer.toString(this.pollQuestionID));

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
