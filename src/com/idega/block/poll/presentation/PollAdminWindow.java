package com.idega.block.poll.presentation;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import com.idega.block.poll.business.PollBusiness;
import com.idega.block.poll.business.PollFinder;
import com.idega.block.poll.data.PollEntity;
import com.idega.block.poll.data.PollQuestion;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

public class PollAdminWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
private boolean isAdmin = false;
private boolean superAdmin = false;
private boolean save = false;
private int iObjInsId = -1;
public  static String prmID = "poll.id";
public  static String prmAttribute = "poll.attribute";
public  static String prmLocale = "poll.localedrp";
public  static String prmObjInstId = "poll.icobjinstid";
private static String prmHeadline = "poll.headline";

private int _pollID = -1;
private int _userID = -1;
private int _newObjInst = -1;
private String _newWithAttribute;
private int _pollQuestionID = -1;
private Image editImage;
private Image createImage;
private Image deleteImage;

private IWBundle iwb;
private IWResourceBundle iwrb;

public PollAdminWindow(){
  setWidth(570);
  setHeight(430);
  setUnMerged();
  setMethod("get");
}

  public void main(IWContext iwc) throws Exception {
    /**
     * @todo permission
     */
    this.isAdmin = iwc.hasEditPermission(this);
    this.superAdmin = iwc.isSuperAdmin();
    this.iwb = iwc.getIWMainApplication().getBundle(Builderaware.IW_CORE_BUNDLE_IDENTIFIER);
    this.iwrb = getResourceBundle(iwc);
    addTitle(this.iwrb.getLocalizedString("poll_admin","Poll Admin"));
    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;

    try {
      this._userID = LoginBusinessBean.getUser(iwc).getID();
    }
    catch (Exception e) {
      this._userID = -1;
    }

    this.editImage = this.iwb.getImage("shared/edit.gif");
    this.createImage = this.iwb.getImage("shared/create.gif");
    this.deleteImage = this.iwb.getImage("shared/delete.gif");

    String sLocaleId = iwc.getParameter(prmLocale);

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
      processForm(iwc, iLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId) {
    if ( iwc.getParameter(prmID) != null ) {
      try {
        this._pollID = Integer.parseInt(iwc.getParameter(prmID));
        iwc.setSessionAttribute(prmID,Integer.toString(this._pollID));
      }
      catch (NumberFormatException e) {
        this._pollID = -1;
      }
    }
    else if ( (String) iwc.getSessionAttribute(prmID) != null ) {
      try {
        this._pollID = Integer.parseInt((String) iwc.getSessionAttribute(prmID));
      }
      catch (NumberFormatException e) {
        this._pollID = -1;
      }
    }

    if ( iwc.getParameter(prmObjInstId) != null ) {
      try {
        this._newObjInst = Integer.parseInt(iwc.getParameter(prmObjInstId));
        iwc.setSessionAttribute(prmObjInstId,new Integer(this._newObjInst));
      }
      catch (NumberFormatException e) {
        this._newObjInst = -1;
      }
    }
    else if ( (Integer) iwc.getSessionAttribute(prmObjInstId) != null ) {
      try {
        this._newObjInst = ((Integer) iwc.getSessionAttribute(prmObjInstId)).intValue();
      }
      catch (NumberFormatException e) {
        this._newObjInst = -1;
      }
    }

    if ( iwc.getParameter(prmAttribute) != null ) {
      this._newWithAttribute = iwc.getParameter(prmAttribute);
      iwc.setSessionAttribute(prmAttribute,this._newWithAttribute);
    }
    else if ( (String) iwc.getSessionAttribute(prmAttribute) != null ) {
      this._newWithAttribute = (String) iwc.getSessionAttribute(prmAttribute);
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    addLeft(this.iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);

    Table pollTable = new Table(3,1);
      pollTable.setCellpadding(0);
      pollTable.setCellspacing(0);

    Text choosePollText = formatText(this.iwrb.getLocalizedString("choose_poll_question","Choose Question")+":&nbsp;",true);
    Link choosePollLink = new Link(this.editImage);
      choosePollLink.setWindowToOpen(PollQuestionChooser.class);
      choosePollLink.addParameter(Poll._prmPollID,this._pollID);
    Link createPollLink = new Link(this.createImage);
      createPollLink.setWindowToOpen(PollQuestionEditor.class);
      createPollLink.addParameter(Poll._prmPollID,this._pollID);
      pollTable.add(choosePollText,1,1);
      pollTable.setWidth(2,1,"5");
      pollTable.add(createPollLink,3,1);
      pollTable.add(choosePollLink,3,1);

    addLeft(pollTable,false);

    String pollQuestionID = (String) iwc.getSessionAttribute(PollQuestionChooser.prmQuestions);
    if ( pollQuestionID != null ) {
      //iwc.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
      try {
        this._pollQuestionID = Integer.parseInt(pollQuestionID);
      }
      catch (NumberFormatException e) {
        this._pollQuestionID = -1;
      }
    }

    if ( iwc.getParameter(PollBusiness._PARAMETER_MODE) != null ) {
      if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {
        closePoll(iwc);
      }
      else if ( iwc.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {
        savePoll(iwc);
      }
    }

    if ( this._pollQuestionID == -1 ) {
      PollQuestion pollQuestion = PollBusiness.getQuestion(this._pollID);
      if ( pollQuestion != null ) {
        try {
          this._pollQuestionID = pollQuestion.getID();
        }
        catch (NumberFormatException e) {
          this._pollQuestionID = -1;
        }
      }
      else {
        if ( this._newWithAttribute != null ) {
          PollEntity poll = PollFinder.getPoll(this._newWithAttribute);
          if ( poll != null ) {
            this._pollID = poll.getID();
            iwc.setSessionAttribute(prmID,Integer.toString(this._pollID));
            try {
              this._pollQuestionID = poll.getPollQuestionID();
            }
            catch (NumberFormatException e) {
              this._pollQuestionID = -1;
            }
          }
        }
      }
    }
    else {
      PollQuestion pollQuestion = PollBusiness.getPollQuestion(this._pollQuestionID);
      if ( pollQuestion == null ) {
        this._pollQuestionID = -1;
      }
    }

    if ( this._pollQuestionID == 0 ) {
      this._pollQuestionID = -1;
    }

    getPoll(this._pollQuestionID,iLocaleId);
  }

  private void getPoll(int pollQuestionID, int iLocaleID) {
    PollQuestion _pollQuestion = null;
    String pollQuestion = PollBusiness.getLocalizedQuestion(pollQuestionID,iLocaleID);
    if ( pollQuestion == null ) {
      pollQuestion = this.iwrb.getLocalizedString("no_text_in_language","No text in this language");
    }
    if ( pollQuestionID == -1 ) {
      pollQuestion = this.iwrb.getLocalizedString("no_question","No question selected");
    }
    else {
      _pollQuestion = PollBusiness.getPollQuestion(pollQuestionID);
    }


    String[] pollAnswers = PollBusiness.getLocalizedAnswers(pollQuestionID,iLocaleID);
    String[] pollAnswersIDs = PollBusiness.getAnswerIDs(pollQuestionID);

    Text questionText = new Text(pollQuestion);
      questionText.setFontFace(Text.FONT_FACE_VERDANA);
      questionText.setBold();
      questionText.setFontSize(Text.FONT_SIZE_10_HTML_2);

    Link questionEditLink = new Link(this.editImage);
      questionEditLink.setWindowToOpen(PollQuestionEditor.class);
      questionEditLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
      questionEditLink.addParameter(Poll._prmPollID,this._pollID);
    Link questionDeleteLink = new Link(this.deleteImage);
      questionDeleteLink.setWindowToOpen(PollQuestionEditor.class);
      questionDeleteLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
      questionDeleteLink.addParameter(PollBusiness._PARAMETER_DELETE,PollBusiness._PARAMETER_TRUE);

    Table questionTable = new Table(3,1);
      questionTable.setCellpadding(0);
      questionTable.setCellspacing(0);
      questionTable.add(questionText,1,1);
      if ( pollQuestionID != -1 && _pollQuestion != null ) {
        if ( this._userID == _pollQuestion.getUserID() || this.superAdmin ) {
          questionTable.setWidth(2,1,"5");
          questionTable.add(questionEditLink,3,1);
          questionTable.add(questionDeleteLink,3,1);
        }
      }

    addLeft(this.iwrb.getLocalizedString("question","Question")+":",questionTable,true,false);

    if ( pollQuestionID != -1 ) {
      Table answerTable = new Table();
      int row = 1;

      if ( pollAnswers != null && pollAnswersIDs != null ) {
        for ( int a = 0; a < pollAnswers.length; a++ ) {
          Link editAnswerLink = new Link(this.editImage);
            editAnswerLink.setWindowToOpen(PollAnswerEditor.class);
            editAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_ANSWER,pollAnswersIDs[a]);

          Link deleteAnswerLink = new Link(this.deleteImage);
            deleteAnswerLink.setWindowToOpen(PollAnswerEditor.class);
            deleteAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_ANSWER,pollAnswersIDs[a]);
            deleteAnswerLink.addParameter(PollBusiness._PARAMETER_DELETE,PollBusiness._PARAMETER_TRUE);

          answerTable.add("<li>",1,row);
          answerTable.add(formatText(pollAnswers[a],true),1,row);
          if ( this._userID == _pollQuestion.getUserID() || this.superAdmin ) {
            answerTable.setWidth(2,row,"5");
            answerTable.add(editAnswerLink,3,row);
            answerTable.add(deleteAnswerLink,3,row);
          }
          row++;
        }
      }

      Link createAnswerLink = new Link(this.createImage);
        createAnswerLink.setWindowToOpen(PollAnswerEditor.class);
        createAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
        if ( this._userID == _pollQuestion.getUserID() || this.superAdmin ) {
          answerTable.add(createAnswerLink,1,row);
        }

      addLeft(this.iwrb.getLocalizedString("answers","Answers")+":",answerTable,true,false);
      addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID)));
    }

    addSubmitButton(new SubmitButton(this.iwrb.getLocalizedImageButton("close","CLOSE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(this.iwrb.getLocalizedImageButton("save","SAVE"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));

  }

  private void savePoll(IWContext iwc) {
    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    iwc.removeSessionAttribute(prmID);
    iwc.removeSessionAttribute(PollQuestionChooser.prmQuestions);
    PollBusiness.savePoll(this._pollID,this._pollQuestionID,this._newObjInst,this._newWithAttribute);
    setParentToReload();
    close();
  }

  private void closePoll(IWContext iwc) {
    iwc.removeSessionAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    iwc.removeSessionAttribute(prmID);
    iwc.removeSessionAttribute(PollQuestionChooser.prmQuestions);
    close();
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(this.iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton());
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
