// idega 2000 - gimmi
package com.idega.block.poll.presentation;

import java.sql.SQLException;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.Page;
import com.idega.presentation.Image;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.*;
import com.idega.block.IWBlock;
import com.idega.block.poll.data.*;
import com.idega.core.data.ICObjectInstance;
import com.idega.block.poll.business.*;
import com.idega.block.text.business.*;
import com.idega.block.text.data.LocalizedText;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.idegaTimestamp;


public class Poll extends Block implements IWBlock{

private boolean _isAdmin;
private int _pollID;
private String _sAttribute = null;
private int _iLocaleID;

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;
protected IWBundle _iwbPoll;

private boolean _styles = true;
private Table _myTable;
public static String _prmPollID = "po.poll_id";
public static String _prmPollCollection = "po.poll_collection";
public static String _prmShowVotes = "po.show_votes";
public static String _prmNumberOfPolls = "po.number_of_votes";
private boolean _newObjInst = false;
private boolean _newWithAttribute = false;
private String _parameterString;
private String _styleAttribute;
private String _hoverStyle;
private String _questionStyleAttribute;
private String _votedColor;
private String _whiteColor;

private String _pollWidth;
private int _numberOfShownPolls;
private boolean _showVotes;
private boolean _showCollection;
private idegaTimestamp _date;
private Image _linkImage;
private Image _linkOverImage;
private Image _questionImage;
private boolean _showInformation = false;
private String _questionAlignment;
private String _name;

public static final int RADIO_BUTTON_VIEW = 1;
public static final int LINK_VIEW = 2;
private int _layout = RADIO_BUTTON_VIEW;

  public Poll(){
    setDefaultValues();
  }

  public Poll(String sAttribute){
    this();
    _pollID = -1;
    _sAttribute = sAttribute;
  }

  public Poll(int pollID){
    this();
    _pollID = pollID;
  }

  public void main(IWContext iwc)throws Exception{
    _iwrb = getResourceBundle(iwc);
    _iwb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    _iwbPoll = getBundle(iwc);

    _isAdmin = iwc.hasEditPermission(this);
    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
    _parameterString = iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER);
    _date = new idegaTimestamp();

    PollEntity poll = null;

    _myTable = new Table(1,2);
      _myTable.setCellpadding(0);
      _myTable.setCellspacing(0);
      _myTable.setBorder(0);
      _myTable.setWidth(_pollWidth);

    if(_pollID <= 0){
      String sPollID = iwc.getParameter(_prmPollID);
      if(sPollID != null)
        _pollID = Integer.parseInt(sPollID);
      else if(getICObjectInstanceID() > 0){
        _pollID = PollFinder.getObjectInstanceID(getICObjectInstance());
        if(_pollID <= 0 ){
          _newObjInst = true;
          PollBusiness.savePoll(_pollID,-1,getICObjectInstanceID(),null);
        }
      }
    }

    if ( _newObjInst ) {
      _pollID = PollFinder.getObjectInstanceID(new ICObjectInstance(getICObjectInstanceID()));
    }

    if(_pollID > 0) {
      poll = new PollEntity(_pollID);
    }
    else if ( _sAttribute != null ){
      poll = PollFinder.getPoll(_sAttribute);
      if ( poll != null ) {
        _pollID = poll.getID();
      }
      _newWithAttribute = true;
    }

    int row = 1;
    if(_isAdmin){
      _myTable.add(getAdminPart(_pollID,_newObjInst,_newWithAttribute),1,row);
      row++;
    }

    _myTable.add(getPoll(iwc, poll),1,row);
    add(_myTable);
  }

  private Link getAdminPart(int pollID,boolean newObjInst,boolean newWithAttribute) {
    Link adminLink = new Link(_iwb.getImage("shared/edit.gif"));
      adminLink.setWindowToOpen(PollAdminWindow.class,this.getICObjectInstanceID());
      adminLink.addParameter(PollAdminWindow.prmID,pollID);
      if(newObjInst)
        adminLink.addParameter(PollAdminWindow.prmObjInstId,getICObjectInstanceID());
      else if(newWithAttribute)
        adminLink.addParameter(PollAdminWindow.prmAttribute,_sAttribute);

    return adminLink;
  }

  private PresentationObject getPoll(IWContext iwc, PollEntity poll) {
    LocalizedText locText = null;
    PollQuestion pollQuestion = PollBusiness.getQuestion(poll);
    idegaTimestamp after;
    boolean pollByDate = false;

    if(pollQuestion != null){
      if ( pollQuestion.getEndTime() != null ) {
        after = new idegaTimestamp(pollQuestion.getEndTime());
        if ( _date.isLaterThan(after) ) {
          pollQuestion = PollBusiness.getPollByDate(poll,_date);
          pollByDate = true;
        }
      }
    }
    else {
      pollQuestion = PollBusiness.getPollByDate(poll,_date);
      pollByDate = true;
    }

    if ( pollQuestion != null ) {
      locText = TextFinder.getLocalizedText(pollQuestion,_iLocaleID);
      if ( pollByDate ) {
        PollBusiness.setPollQuestion(poll,pollQuestion);
      }
    }

    PresentationObject obj = null;

    if ( locText != null ) {
      switch (_layout) {
        case RADIO_BUTTON_VIEW:
          obj = getRadioButtonView(locText,pollQuestion);
          break;
        case LINK_VIEW:
          obj = getLinkView(iwc,locText,pollQuestion);
          break;
      }
      return obj;
    }
    else {
      return new Form();
    }
  }

  private Form getRadioButtonView(LocalizedText locText,PollQuestion pollQuestion) {
    Image submitImage = _iwrb.getImage("vote.gif");
    Image olderPollsImage = _iwrb.getImage("older_polls.gif");

      Form form = new Form();
        form.setWindowToOpen(PollResult.class);

      Table pollTable = new Table(2,3);
        pollTable.setCellpadding(5);
        pollTable.setCellspacing(0);
        pollTable.mergeCells(1,1,2,1);
        pollTable.mergeCells(1,2,2,2);
        pollTable.setWidth(_pollWidth);
        pollTable.setAlignment(1,1,_questionAlignment);
        pollTable.setAlignment(2,3,"right");
      form.add(pollTable);

      Text question = new Text(locText.getHeadline());
        question.setFontStyle(_questionStyleAttribute);

      pollTable.add(question,1,1);

      RadioGroup radioGroup = new RadioGroup(PollBusiness._PARAMETER_POLL_ANSWER);

      PollAnswer[] answers = PollBusiness.getAnswers(pollQuestion.getID());
      boolean hasAnswers = false;

      if ( answers != null ) {
        for ( int a = 0; a < answers.length; a++ ) {
          LocalizedText locAnswerText = TextFinder.getLocalizedText(answers[a],_iLocaleID);
          if ( locAnswerText != null ) {
            hasAnswers = true;
            radioGroup.addRadioButton(answers[a].getID(),new Text(locAnswerText.getHeadline()),false,_styleAttribute,_styleAttribute);
          }
        }
      }

      if ( hasAnswers ) {
        radioGroup.setStyle(_styleAttribute);
        pollTable.add(radioGroup,1,2);
      }

      Link collectionLink = new Link(olderPollsImage);
        collectionLink.setWindowToOpen(PollResult.class);
        collectionLink.addParameter(Poll._prmPollID,_pollID);
        collectionLink.addParameter(Poll._prmPollCollection,PollBusiness._PARAMETER_TRUE);
        collectionLink.addParameter(Poll._prmNumberOfPolls,_numberOfShownPolls);
        if ( _showVotes ) {
          collectionLink.addParameter(Poll._prmShowVotes,PollBusiness._PARAMETER_TRUE);
        }
        else {
          collectionLink.addParameter(Poll._prmShowVotes,PollBusiness._PARAMETER_FALSE);
        }

      if ( _showCollection ) {
        pollTable.add(collectionLink,1,3);
      }
      pollTable.add(new SubmitButton(submitImage),2,3);
      pollTable.add(new Parameter(PollBusiness._PARAMETER_POLL_VOTER,PollBusiness._PARAMETER_TRUE));
      pollTable.add(new Parameter(PollBusiness._PARAMETER_POLL_QUESTION,Integer.toString(pollQuestion.getID())));
      if ( _showVotes ) {
        pollTable.add(new Parameter(Poll._prmShowVotes,PollBusiness._PARAMETER_TRUE));
      }
      else {
        pollTable.add(new Parameter(Poll._prmShowVotes,PollBusiness._PARAMETER_FALSE));
      }

      return form;
  }

  private Table getLinkView(IWContext iwc,LocalizedText locText,PollQuestion pollQuestion) {
    setStyles();
    Image olderPollsImage = _iwrb.getImage("older_polls.gif");

    Table pollTable = new Table();
      pollTable.setCellpadding(3);
      pollTable.setCellspacing(0);
      pollTable.setWidth(_pollWidth);
      pollTable.setAlignment(1,1,_questionAlignment);
    int pollRow = 1;

    Text question = new Text(locText.getHeadline());
      question.setFontStyle(_questionStyleAttribute);

    if ( _questionImage != null ) {
      Table questionTable = new Table(3,1);
        questionTable.setCellpadding(0);
        questionTable.setCellspacing(0);
        questionTable.setAlignment(_questionAlignment);
        questionTable.setVerticalAlignment(1,1,"top");
      _questionImage.setVerticalSpacing(2);
      questionTable.add(_questionImage,1,1);
      questionTable.setWidth(2,1,"4");
      questionTable.add(question,3,1);

      pollTable.add(questionTable,1,pollRow);
    }
    else {
      pollTable.add(question,1,pollRow);
    }
    pollRow++;

    Table answerTable = new Table();
      answerTable.setCellspacing(0);
      answerTable.setCellpadding(0);
      answerTable.setWidth("100%");
    PollAnswer[] answers = PollBusiness.getAnswers(pollQuestion.getID());

    boolean canVote = true;
    if ( iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER) != null )
      canVote = false;
    if ( canVote )
      canVote = PollBusiness.canVote(iwc,pollQuestion.getID());

    if ( canVote ) {
      boolean hasAnswers = false;

      if ( answers != null ) {
        int row = 1;

        for ( int a = 0; a < answers.length; a++ ) {
          LocalizedText locAnswerText = TextFinder.getLocalizedText(answers[a],_iLocaleID);
          if ( locAnswerText != null ) {
            hasAnswers = true;

            Link answerLink = new Link(locAnswerText.getHeadline());
              answerLink.addParameter(PollBusiness._PARAMETER_POLL_QUESTION,pollQuestion.getID());
              answerLink.addParameter(PollBusiness._PARAMETER_POLL_ANSWER,answers[a].getID());
              answerLink.addParameter(PollBusiness._PARAMETER_POLL_VOTER,PollBusiness._PARAMETER_TRUE);
              answerLink.addParameter(PollBusiness._PARAMETER_CLOSE,PollBusiness._PARAMETER_TRUE);
              answerLink.setEventListener(PollListener.class);
              if ( _name != null )
                answerLink.setStyle(_name);

            if ( _linkImage != null ) {
              Table imageTable = new Table(3,1);
                imageTable.setCellspacing(0);
                imageTable.setCellpadding(0);

              Image image = new Image(_linkImage.getMediaServletString());

              image.setVerticalSpacing(3);
              if ( _linkOverImage != null ) {
                  image.setOverImage(_linkOverImage);
                  _linkOverImage.setVerticalSpacing(3);

                answerLink.setAttribute("onMouseOver","swapImage('"+image.getName()+"','','"+_linkOverImage.getMediaServletString()+"',1)");
                answerLink.setAttribute("onMouseOut","swapImgRestore()");
              }

              imageTable.add(image,1,1);
              imageTable.setVerticalAlignment(1,1,"top");
              imageTable.setWidth(2,"8");
              imageTable.add(answerLink,3,1);
              answerTable.add(imageTable,1,row);
            }
            else {
              answerTable.add(answerLink,1,row);
            }
            row++;
            answerTable.setHeight(row,"4");
            row++;
          }
        }
      }

      if ( hasAnswers ) {
        pollTable.add(answerTable,1,pollRow);
        pollRow++;
      }
    }
    else {
      int numberOfAnswers = PollBusiness.getNumberOfAnswers(pollQuestion);
      int total = 0;
      int row = 0;

      if (answers != null) {
        if (answers.length > 0) {
          for ( int i = 0; i < answers.length; i++ ) {
            total += answers[i].getHits();
          }
          for (int i = 0 ; i < answers.length ; i++ ) {
            LocalizedText answerLocText = TextFinder.getLocalizedText(answers[i],_iLocaleID);
            if ( answerLocText != null ) {
              ++row;

              float percent = 0;
              if ( answers[i].getHits() > 0 )
                percent = ( (float) answers[i].getHits() / (float) total ) * 100;

              Text answerText = new  Text(answerLocText.getHeadline());
                if ( _showVotes || _isAdmin ) answerText.addToText(" ("+Integer.toString(answers[i].getHits())+")");
                answerText.setFontSize(1);
                answerText.setFontStyle(_styleAttribute);
              Text percentText = new Text(Text.NON_BREAKING_SPACE+com.idega.util.text.TextSoap.decimalFormat(percent,1)+"%");
                percentText.setFontSize(1);

              answerTable.mergeCells(1,row,2,row);
              answerTable.add(answerText,1,row);
              row++;

              Table table = new Table();
                table.setCellpadding(0);
                table.setCellspacing(1);
                table.setWidth("100%");
                table.setColor("#000000");

              Image transImage = answerTable.getTransparentCell(iwc);
                transImage.setHeight(10);
                transImage.setWidth("100%");
              Image transImage2 = answerTable.getTransparentCell(iwc);
                transImage2.setHeight(10);
                transImage2.setWidth("100%");
              if ( percent > 0 ) {
                table.setColor(1,1,_votedColor);
                table.add(transImage,1,1);
                table.setWidth(1,1,Integer.toString((int) percent)+"%");
                if ( percent < 100 ) {
                  table.setColor(2,1,_whiteColor);
                  table.add(transImage2,2,1);
                  table.setWidth(2,1,Integer.toString(100 - (int) percent)+"%");
                }
              }
              else if ( percent <= 0 ) {
                table.setColor(1,1,_whiteColor);
                table.setWidth(1,1,"100%");
                table.add(transImage2,1,1);
              }

              answerTable.add(table,1,row);
              answerTable.add(percentText,2,row);
              answerTable.setWidth(1,row,"100%");

              row++;
              answerTable.setHeight(row,"6");
            }
          }
        }
        answerTable.setWidth(1,"100%");
        pollTable.add(answerTable,1,pollRow);
        pollRow++;

        String information = PollBusiness.getLocalizedInformation(pollQuestion.getID(),_iLocaleID);
        if ( information != null && _showInformation ) {
          Text informationText = new Text(information);
            informationText.setFontStyle(_styleAttribute);
          pollTable.add(informationText,1,pollRow);
          pollRow++;
        }
      }
    }

    Link collectionLink = new Link(olderPollsImage);
      collectionLink.setWindowToOpen(PollResult.class);
      collectionLink.addParameter(Poll._prmPollID,_pollID);
      collectionLink.addParameter(Poll._prmPollCollection,PollBusiness._PARAMETER_TRUE);
      collectionLink.addParameter(Poll._prmNumberOfPolls,_numberOfShownPolls);
      if ( _showVotes ) {
        collectionLink.addParameter(Poll._prmShowVotes,PollBusiness._PARAMETER_TRUE);
      }
      else {
        collectionLink.addParameter(Poll._prmShowVotes,PollBusiness._PARAMETER_FALSE);
      }

    if ( _showCollection ) {
      pollTable.add(collectionLink,1,pollRow);
    }

    return pollTable;
  }

  private void setDefaultValues() {
    _pollWidth = "150";
    _styleAttribute = "font-face: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; text-decoration: none;";
    _hoverStyle = "font-face: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; text-decoration: none;";
    _questionStyleAttribute = "font-face: Verdana, Arial, Helvetica, sans-serif; font-size: 11pt; font-weight: bold";
    _numberOfShownPolls = 3;
    _showVotes = true;
    _showCollection = true;
    _questionAlignment = "center";
    _pollID = -1;
    _votedColor = "#104584";
    _whiteColor = "#FFFFFF";
  }

  private void setStyles() {
    if ( _name == null )
      _name = this.getName();
    if ( _name == null ) {
      if ( _sAttribute == null )
        _name = "poll_"+Integer.toString(_pollID);
      else
        _name = "poll_"+_sAttribute;
    }

    if ( getParentPage() != null ) {
      getParentPage().setStyleDefinition("A."+_name+":link",_styleAttribute);
      getParentPage().setStyleDefinition("A."+_name+":visited",_styleAttribute);
      getParentPage().setStyleDefinition("A."+_name+":active",_styleAttribute);
      getParentPage().setStyleDefinition("A."+_name+":hover",_hoverStyle);
    }
    else {
      _styles = false;
    }
  }

  public boolean deleteBlock(int ICObjectInstanceId) {
    PollEntity poll = PollFinder.getObjectInstanceFromID(ICObjectInstanceId);
    return PollBusiness.deletePoll(poll);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setWidth(int pollWidth) {
    _pollWidth = Integer.toString(pollWidth);
  }

  public void setWidth(String pollWidth) {
    _pollWidth = pollWidth;
  }

  public void setStyle(String style) {
    _styleAttribute = style;
  }

  public void setHoverStyle(String hoverStyle) {
    _hoverStyle = hoverStyle;
  }

  public void setQuestionStyle(String style) {
    _questionStyleAttribute = style;
  }

  public void setNumberOfShownPolls(int numberOfShownPolls) {
    _numberOfShownPolls = numberOfShownPolls;
  }

  public void showVotes(boolean showVotes) {
    _showVotes = showVotes;
  }

  public void showCollection(boolean collection) {
    _showCollection = collection;
  }

  public void setQuestionImage(Image image) {
    _questionImage = image;
  }

  public void setLinkImage(Image image) {
    _linkImage = image;
  }

  public void setLinkOverImage(Image image) {
    _linkOverImage = image;
  }

  public void setLayout(int layout) {
   _layout = layout;
  }

  public void setShowInformation(boolean showInformation) {
    _showInformation = showInformation;
  }

  public void setQuestionAlignment(String alignment) {
    _questionAlignment = alignment;
  }

  public void setVotedColor(String color) {
    _votedColor = color;
  }

  public void setWhiteColor(String color) {
    _whiteColor = color;
  }

  public Object clone() {
    Poll obj = null;
    try {
      obj = (Poll) super.clone();

      if ( this._myTable != null ) {
        obj._myTable = (Table) this._myTable.clone();
      }
      if ( this._linkImage != null ) {
        obj._linkImage = (Image) this._linkImage.clone();
      }
      if ( this._linkOverImage != null ) {
        obj._linkOverImage = (Image) this._linkOverImage.clone();
      }
      if ( this._questionImage != null ) {
        obj._questionImage = (Image) this._questionImage.clone();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

/**@todo finish this for all states**/
  protected String getCacheState(IWContext iwc, String cacheStatePrefix, String locale, boolean edit){
    String returnString = iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER);
    if( returnString == null ) returnString = "";
    else {
      returnString+= iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION);
      invalidateCache(iwc);
    }
    return  cacheStatePrefix+String.valueOf(PollBusiness.canVote(iwc,_pollID))+returnString;
  }
}