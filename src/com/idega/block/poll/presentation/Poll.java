// idega 2000 - gimmi
package com.idega.block.poll.presentation;

import java.sql.SQLException;
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

private Table _myTable;
public static String _prmPollID = "po.poll_id";
public static String _prmPollCollection = "po.poll_collection";
public static String _prmShowVotes = "po.show_votes";
public static String _prmNumberOfPolls = "po.number_of_votes";
private boolean _newObjInst = false;
private boolean _newWithAttribute = false;
private String _parameterString;
private String _styleAttribute;
private String _questionStyleAttribute;

private String _pollWidth;
private int _numberOfShownPolls;
private boolean _showVotes;
private boolean _showCollection;
private idegaTimestamp _date;

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
    _iwb = getBundle(iwc);

    _isAdmin = AccessControl.hasEditPermission(this,iwc);
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
    Link adminLink = new Link(_iwrb.getImage("pollmanager.gif"));
      adminLink.setWindowToOpen(PollAdminWindow.class,this.getICObjectInstanceID());
      adminLink.addParameter(PollAdminWindow.prmID,pollID);
      if(newObjInst)
        adminLink.addParameter(PollAdminWindow.prmObjInstId,getICObjectInstanceID());
      else if(newWithAttribute)
        adminLink.addParameter(PollAdminWindow.prmAttribute,_sAttribute);

    return adminLink;
  }

  private Form getPoll(IWContext iwc, PollEntity poll) {
    LocalizedText locText = null;
    PollQuestion pollQuestion = PollBusiness.getQuestion(poll);
    Image submitImage = _iwrb.getImage("vote.gif");
    Image olderPollsImage = _iwrb.getImage("older_polls.gif");
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

    if ( locText != null ) {
			Form form = new Form();
        form.setWindowToOpen(PollResult.class);

      Table pollTable = new Table(2,3);
				pollTable.setCellpadding(5);
				pollTable.setCellspacing(0);
        pollTable.mergeCells(1,1,2,1);
        pollTable.mergeCells(1,2,2,2);
				pollTable.setWidth(_pollWidth);
				pollTable.setAlignment(1,1,"center");
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
      else {
        System.out.println("locText is null");
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
    else {
      return new Form();
    }
  }

  private void setDefaultValues() {
    _pollWidth = "150";
    _styleAttribute = "font-face: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt";
    _questionStyleAttribute = "font-face: Verdana, Arial, Helvetica, sans-serif; font-size: 11pt; font-weight: bold";
    _numberOfShownPolls = 3;
    _showVotes = true;
    _showCollection = true;
    _pollID = -1;
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
}