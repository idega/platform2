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
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.text.business.TextFinder;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

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
    isAdmin = iwc.getAccessController().hasEditPermission(this,iwc);
    superAdmin = iwc.getAccessController().isAdmin(iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    addTitle(iwrb.getLocalizedString("poll_admin","Poll Admin"));
    Locale currentLocale = iwc.getCurrentLocale(),chosenLocale;

    try {
      _userID = LoginBusiness.getUser(iwc).getID();
    }
    catch (Exception e) {
      _userID = -1;
    }

    editImage = iwrb.getImage("edit.gif");
      editImage.setHorizontalSpacing(4);
      editImage.setVerticalSpacing(3);
    createImage = iwrb.getImage("create.gif");
      createImage.setHorizontalSpacing(4);
      createImage.setVerticalSpacing(3);
    deleteImage = iwrb.getImage("delete.gif");
      deleteImage.setHorizontalSpacing(4);
      deleteImage.setVerticalSpacing(3);

    String sLocaleId = iwc.getParameter(prmLocale);

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
      processForm(iwc, iLocaleId);
    }
    else {
      noAccess();
    }
  }

  private void processForm(IWContext iwc, int iLocaleId) {
    if ( iwc.getParameter(prmID) != null ) {
      try {
        _pollID = Integer.parseInt(iwc.getParameter(prmID));
        iwc.setApplicationAttribute(prmID,Integer.toString(_pollID));
      }
      catch (NumberFormatException e) {
        _pollID = -1;
      }
    }
    else if ( (String) iwc.getApplicationAttribute(prmID) != null ) {
      try {
        _pollID = Integer.parseInt((String) iwc.getApplicationAttribute(prmID));
      }
      catch (NumberFormatException e) {
        _pollID = -1;
      }
    }

    if ( iwc.getParameter(prmObjInstId) != null ) {
      try {
        _newObjInst = Integer.parseInt(iwc.getParameter(prmObjInstId));
        iwc.setApplicationAttribute(prmObjInstId,new Integer(_newObjInst));
      }
      catch (NumberFormatException e) {
        _newObjInst = -1;
      }
    }
    else if ( (Integer) iwc.getApplicationAttribute(prmObjInstId) != null ) {
      try {
        _newObjInst = ((Integer) iwc.getApplicationAttribute(prmObjInstId)).intValue();
      }
      catch (NumberFormatException e) {
        _newObjInst = -1;
      }
    }

    if ( iwc.getParameter(prmAttribute) != null ) {
      _newWithAttribute = iwc.getParameter(prmAttribute);
      iwc.setApplicationAttribute(prmAttribute,_newWithAttribute);
    }
    else if ( (String) iwc.getApplicationAttribute(prmAttribute) != null ) {
      _newWithAttribute = (String) iwc.getApplicationAttribute(prmAttribute);
    }

    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PollAdminWindow.prmLocale);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    addLeft(iwrb.getLocalizedString("locale","Locale")+": ",localeDrop,false);

    Table pollTable = new Table(3,1);
      pollTable.setCellpadding(0);
      pollTable.setCellspacing(0);

    Text choosePollText = formatText(iwrb.getLocalizedString("choose_poll_question","Choose Question")+":&nbsp;",true);
    Link choosePollLink = new Link(iwrb.getImage("choose.gif"));
      choosePollLink.setWindowToOpen(PollQuestionChooser.class);
      choosePollLink.addParameter(Poll._prmPollID,_pollID);
    Link createPollLink = new Link(createImage);
      createPollLink.setWindowToOpen(PollQuestionEditor.class);
      createPollLink.addParameter(Poll._prmPollID,_pollID);
      pollTable.add(choosePollText,1,1);
      pollTable.add(choosePollLink,2,1);
      pollTable.add(createPollLink,3,1);

    addLeft(pollTable,false);

    String pollQuestionID = (String) iwc.getApplicationAttribute(PollQuestionChooser.prmQuestions);
    if ( pollQuestionID != null ) {
      //iwc.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
      try {
        _pollQuestionID = Integer.parseInt(pollQuestionID);
      }
      catch (NumberFormatException e) {
        _pollQuestionID = -1;
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

    if ( _pollQuestionID == -1 ) {
      PollQuestion pollQuestion = PollBusiness.getQuestion(_pollID);
      if ( pollQuestion != null ) {
        try {
          _pollQuestionID = pollQuestion.getID();
        }
        catch (NumberFormatException e) {
          _pollQuestionID = -1;
        }
      }
      else {
        if ( _newWithAttribute != null ) {
          PollEntity poll = PollFinder.getPoll(_newWithAttribute);
          if ( poll != null ) {
            _pollID = poll.getID();
            iwc.setApplicationAttribute(prmID,Integer.toString(_pollID));
            try {
              _pollQuestionID = poll.getPollQuestionID();
            }
            catch (NumberFormatException e) {
              _pollQuestionID = -1;
            }
          }
        }
      }
    }
    else {
      PollQuestion pollQuestion = PollBusiness.getPollQuestion(_pollQuestionID);
      if ( pollQuestion == null ) {
        _pollQuestionID = -1;
      }
    }

    if ( _pollQuestionID == 0 ) {
      _pollQuestionID = -1;
    }

    getPoll(_pollQuestionID,iLocaleId);
  }

  private void getPoll(int pollQuestionID, int iLocaleID) {
    PollQuestion _pollQuestion = null;
    String pollQuestion = PollBusiness.getLocalizedQuestion(pollQuestionID,iLocaleID);
    if ( pollQuestion == null ) {
      pollQuestion = iwrb.getLocalizedString("no_text_in_language","No text in this language");
    }
    if ( pollQuestionID == -1 ) {
      pollQuestion = iwrb.getLocalizedString("no_question","No question selected");
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

    Link questionEditLink = new Link(editImage);
      questionEditLink.setWindowToOpen(PollQuestionEditor.class);
      questionEditLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
      questionEditLink.addParameter(Poll._prmPollID,_pollID);
    Link questionDeleteLink = new Link(deleteImage);
      questionDeleteLink.setWindowToOpen(PollQuestionEditor.class);
      questionDeleteLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
      questionDeleteLink.addParameter(PollBusiness._PARAMETER_DELETE,PollBusiness._PARAMETER_TRUE);

    Table questionTable = new Table(2,2);
      questionTable.setCellpadding(0);
      questionTable.setCellspacing(0);
      questionTable.mergeCells(1,1,2,1);
      questionTable.add(questionText,1,1);
      if ( pollQuestionID != -1 && _pollQuestion != null ) {
        if ( _userID == _pollQuestion.getUserID() || superAdmin ) {
          questionTable.add(questionEditLink,1,2);
          questionTable.add(questionDeleteLink,2,2);
        }
      }

    addLeft(iwrb.getLocalizedString("question","Question")+":",questionTable,true,false);

    if ( pollQuestionID != -1 ) {
      Table answerTable = new Table();
      int row = 1;

      if ( pollAnswers != null && pollAnswersIDs != null ) {
        for ( int a = 0; a < pollAnswers.length; a++ ) {
          Link editAnswerLink = new Link(editImage);
            editAnswerLink.setWindowToOpen(PollAnswerEditor.class);
            editAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_ANSWER,pollAnswersIDs[a]);

          Link deleteAnswerLink = new Link(deleteImage);
            deleteAnswerLink.setWindowToOpen(PollAnswerEditor.class);
            deleteAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_ANSWER,pollAnswersIDs[a]);
            deleteAnswerLink.addParameter(PollBusiness._PARAMETER_DELETE,PollBusiness._PARAMETER_TRUE);

          answerTable.add("<li>",1,row);
          answerTable.add(formatText(pollAnswers[a],true),1,row);
          if ( _userID == _pollQuestion.getUserID() || superAdmin ) {
            answerTable.add(editAnswerLink,2,row);
            answerTable.add(deleteAnswerLink,3,row);
          }
          row++;
        }
      }

      Link createAnswerLink = new Link(createImage);
        createAnswerLink.setWindowToOpen(PollAnswerEditor.class);
        createAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
        if ( _userID == _pollQuestion.getUserID() || superAdmin ) {
          answerTable.add(createAnswerLink,1,row);
        }

      addLeft(iwrb.getLocalizedString("answers","Answers")+":",answerTable,true,false);
      addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID)));
    }

    addSubmitButton(new SubmitButton(iwrb.getImage("close.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(iwrb.getImage("save.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));

  }

  private void savePoll(IWContext iwc) {
    iwc.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    iwc.removeApplicationAttribute(prmID);
    iwc.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
    PollBusiness.savePoll(_pollID,_pollQuestionID,_newObjInst,_newWithAttribute);
    setParentToReload();
    close();
  }

  private void closePoll(IWContext iwc) {
    iwc.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    iwc.removeApplicationAttribute(prmID);
    iwc.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
    close();
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    addSubmitButton(new CloseButton(iwrb.getImage("close.gif")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
