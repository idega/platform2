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

public class PollAdminWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
private boolean isAdmin = false;
private boolean save = false;
private int iObjInsId = -1;
public  static String prmID = "poll.id";
public  static String prmAttribute = "poll.attribute";
public  static String prmLocale = "poll.localedrp";
public  static String prmObjInstId = "poll.icobjinstid";
private static String prmHeadline = "poll.headline";

private int _pollID = -1;
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

  public void main(ModuleInfo modinfo) throws Exception {
    isAdmin = AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    addTitle(iwrb.getLocalizedString("poll_admin","Poll Admin"));
    Locale currentLocale = modinfo.getCurrentLocale(),chosenLocale;

    editImage = iwrb.getImage("change.gif");
      editImage.setHorizontalSpacing(4);
      editImage.setVerticalSpacing(3);
    createImage = iwrb.getImage("create.gif");
      createImage.setHorizontalSpacing(4);
      createImage.setVerticalSpacing(3);
    deleteImage = iwrb.getImage("delete.gif");
      deleteImage.setHorizontalSpacing(4);
      deleteImage.setVerticalSpacing(3);

    String sLocaleId = modinfo.getParameter(prmLocale);

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
    if ( modinfo.getParameter(prmID) != null ) {
      try {
        _pollID = Integer.parseInt(modinfo.getParameter(prmID));
        modinfo.setApplicationAttribute(prmID,Integer.toString(_pollID));
      }
      catch (NumberFormatException e) {
        _pollID = -1;
      }
    }
    else if ( (String) modinfo.getApplicationAttribute(prmID) != null ) {
      try {
        _pollID = Integer.parseInt((String) modinfo.getApplicationAttribute(prmID));
      }
      catch (NumberFormatException e) {
        _pollID = -1;
      }
    }

    if ( modinfo.getParameter(prmObjInstId) != null ) {
      try {
        _newObjInst = Integer.parseInt(modinfo.getParameter(prmObjInstId));
        modinfo.setApplicationAttribute(prmObjInstId,new Integer(_newObjInst));
      }
      catch (NumberFormatException e) {
        _newObjInst = -1;
      }
    }
    else if ( (Integer) modinfo.getApplicationAttribute(prmObjInstId) != null ) {
      try {
        _newObjInst = ((Integer) modinfo.getApplicationAttribute(prmObjInstId)).intValue();
      }
      catch (NumberFormatException e) {
        _newObjInst = -1;
      }
    }

    if ( modinfo.getParameter(prmAttribute) != null ) {
      _newWithAttribute = modinfo.getParameter(prmAttribute);
      modinfo.setApplicationAttribute(prmAttribute,_newWithAttribute);
    }
    else if ( (String) modinfo.getApplicationAttribute(prmAttribute) != null ) {
      _newWithAttribute = (String) modinfo.getApplicationAttribute(prmAttribute);
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

    String pollQuestionID = (String) modinfo.getApplicationAttribute(PollQuestionChooser.prmQuestions);
    if ( pollQuestionID != null ) {
      modinfo.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
      try {
        _pollQuestionID = Integer.parseInt(pollQuestionID);
      }
      catch (NumberFormatException e) {
        _pollQuestionID = -1;
      }
    }

    if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE) != null ) {
      if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_CLOSE) ) {
        closePoll(modinfo);
      }
      else if ( modinfo.getParameter(PollBusiness._PARAMETER_MODE).equalsIgnoreCase(PollBusiness._PARAMETER_SAVE) ) {
        savePoll(modinfo);
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
            modinfo.setApplicationAttribute(prmID,Integer.toString(_pollID));
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
    String pollQuestion = PollBusiness.getLocalizedQuestion(pollQuestionID,iLocaleID);
    if ( pollQuestion == null ) {
      pollQuestion = iwrb.getLocalizedString("no_text_in_language","No text in this language");
    }
    if ( pollQuestionID == -1 ) {
      pollQuestion = iwrb.getLocalizedString("no_question","No question selected");
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
      if ( pollQuestionID != -1 ) {
        questionTable.add(questionEditLink,1,2);
        questionTable.add(questionDeleteLink,2,2);
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
          answerTable.add(editAnswerLink,2,row);
          answerTable.add(deleteAnswerLink,3,row);
          row++;
        }
      }

      Link createAnswerLink = new Link(createImage);
        createAnswerLink.setWindowToOpen(PollAnswerEditor.class);
        createAnswerLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestionID);
        answerTable.add(createAnswerLink,1,row);

      addLeft(iwrb.getLocalizedString("answers","Answers")+":",answerTable,true,false);
      addHiddenInput(new HiddenInput(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestionID)));
    }

    addSubmitButton(new SubmitButton(iwrb.getImage("close.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_CLOSE));
    addSubmitButton(new SubmitButton(iwrb.getImage("save.gif"),PollBusiness._PARAMETER_MODE,PollBusiness._PARAMETER_SAVE));

  }

  private void savePoll(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    modinfo.removeApplicationAttribute(prmID);
    modinfo.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
    PollBusiness.savePoll(_pollID,_pollQuestionID,_newObjInst,_newWithAttribute);
    setParentToReload();
    close();
  }

  private void closePoll(ModuleInfo modinfo) {
    modinfo.removeApplicationAttribute(PollBusiness._PARAMETER_POLL_QUESTION);
    modinfo.removeApplicationAttribute(prmID);
    modinfo.removeApplicationAttribute(PollQuestionChooser.prmQuestions);
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