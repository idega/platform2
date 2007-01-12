package com.idega.block.poll.presentation;





import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.block.poll.business.PollBusiness;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;



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

    this.isAdmin = true; //AccessControl.hasEditPermission(this,iwc);

    this.iwb = getBundle(iwc);

    this.iwrb = getResourceBundle(iwc);

    addTitle(this.iwrb.getLocalizedString("poll_answer_editor","Poll Answer Editor"));

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



  private void processForm(IWContext iwc, int iLocaleId, String sLocaleID) {

    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {

      try {

        this.pollQuestionID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION));

        iwc.setSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(this.pollQuestionID));

      }

      catch (NumberFormatException e) {

        this.pollQuestionID = -1;

      }

    }

    else if ( (String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION) != null ) {

      try {

        this.pollQuestionID = Integer.parseInt((String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION));

      }

      catch (NumberFormatException e) {

        this.pollQuestionID = -1;

      }

    }



    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_ANSWER) != null ) {

      try {

        this.pollAnswerID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_ANSWER));

        iwc.setSessionAttribute(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(this.pollAnswerID));

      }

      catch (NumberFormatException e) {

        this.pollAnswerID = -1;

      }

    }



    /*if ( sLocaleID != null ) {

      savePollAnswer(iwc,iLocaleId);

    }*/



    if ( (String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_ANSWER) != null ) {

      try {

        this.pollAnswerID = Integer.parseInt((String) iwc.getSessionAttribute(PollBusiness._PARAMETER_POLL_ANSWER));

      }

      catch (NumberFormatException e) {

        this.pollAnswerID = -1;

      }

    }



    if ( iwc.getParameter(PollBusiness._PARAMETER_MODE) != null ) {

      if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {

        closePollAnswer(iwc);

      }

      else if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {

        savePollAnswer(iwc,iLocaleId);

      }

    }



    if ( this.pollAnswerID != -1 ) {

      if ( iwc.getParameter(PollBusiness._PARAMETER_DELETE) != null ) {

        deletePollQuestion();

      }

      else {

        this.update = true;

      }

    }



    initializeFields(iLocaleId);

  }



  private void initializeFields(int iLocaleID) {

    String pollAnswer = PollBusiness.getLocalizedAnswer(this.pollAnswerID,iLocaleID);



    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);

      localeDrop.setToSubmit();

      localeDrop.setSelectedElement(Integer.toString(iLocaleID));

    addLeft(this.iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);



    TextInput questionInput = new TextInput(prmAnswerParameter);

      questionInput.setLength(40);

      if ( this.update && pollAnswer != null ) {

        questionInput.setContent(pollAnswer);

      }



    addLeft(this.iwrb.getLocalizedString("answer","Answer")+":",questionInput,true);

    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(this.pollQuestionID)));

    addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(this.pollAnswerID)));

    addHiddenInput(new HiddenInput("iLocaleID",Integer.toString(iLocaleID)));



    addSubmitButton(new SubmitButton(this.iwrb.getLocalizedImageButton("close","CLOSE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));

    addSubmitButton(new SubmitButton(this.iwrb.getLocalizedImageButton("save","SAVE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));

  }



  private void closePollAnswer(IWContext iwc) {

    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_ANSWER);

    setParentToReload();

    close();

  }



  private void savePollAnswer(IWContext iwc,int iLocaleID) {

    String pollAnswerString = iwc.getParameter(PollAnswerEditor.prmAnswerParameter);

    String localeString = iwc.getParameter("iLocaleID");



    if ( pollAnswerString == null || pollAnswerString.length() == 0 ) {

      pollAnswerString = this.iwrb.getLocalizedString("no_text","No answer entered");

    }



    if ( localeString != null ) {

      this.pollAnswerID = PollBusiness.savePollAnswer(this.pollQuestionID,this.pollAnswerID,pollAnswerString,Integer.parseInt(localeString));

    }



    iwc.setSessionAttribute(PollBusiness._PARAMETER_POLL_ANSWER,Integer.toString(this.pollAnswerID));

  }



  private void deletePollQuestion() {

    PollBusiness.deletePollAnswer(this.pollAnswerID);

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
