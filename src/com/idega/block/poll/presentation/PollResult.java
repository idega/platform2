package com.idega.block.poll.presentation;

import com.idega.presentation.ui.*;
import com.idega.presentation.IWContext;
import com.idega.presentation.*;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.block.poll.business.*;
import com.idega.block.poll.data.*;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import java.util.List;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class PollResult extends Window {

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
protected IWResourceBundle _iwrb;
protected IWBundle _iwb;

private boolean _isAdmin;
private boolean _showCollection = false;
private boolean _save = false;
private boolean _showVotes = true;
private int _numberOfPolls = 3;
private int _pollID = -1;
private int _pollQuestionID = -1;
private int _iLocaleID;
private Image line;
private int border = 0;

public static final String prmPollFirst = "i_poll_first";

Table layoutTable;

public PollResult() {
  setWidth(280);
  setHeight(230);
  setResizable(true);
  setScrollbar(false);
}

  public void main(IWContext iwc) throws Exception {
    _iwrb = getResourceBundle(iwc);
    _iwb = getBundle(iwc);

    setAllMargins(0);
    setTitle(_iwrb.getLocalizedString("results","Results"));

    /**
     * @todo permission
     */
    _isAdmin = true; //AccessControl.hasEditPermission(this,iwc);
    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());

    String collectionString = iwc.getParameter(Poll._prmPollCollection);
    String showVotesString = iwc.getParameter(Poll._prmShowVotes);
    String numberOfPollsString = iwc.getParameter(Poll._prmNumberOfPolls);
    String pollIDString = iwc.getParameter(Poll._prmPollID);

    try {
      _pollQuestionID = Integer.parseInt(iwc.getParameter(PollBusiness._PARAMETER_POLL_QUESTION));
    }
    catch (NumberFormatException e) {
      _pollQuestionID = -1;
    }

    drawLayout();

    if ( collectionString != null ) {
      _showCollection = true;
    }
    if ( showVotesString != null ) {
      if ( showVotesString.equalsIgnoreCase(PollBusiness._PARAMETER_TRUE) ) {
        _showVotes = true;
      }
      else {
        _showVotes = false;
      }
    }
    if ( numberOfPollsString != null ) {
      try {
        _numberOfPolls = Integer.parseInt(numberOfPollsString);
      }
      catch (NumberFormatException e) {
        _numberOfPolls = 3;
      }
    }
    if ( pollIDString != null ) {
      try {
        _pollID = Integer.parseInt(pollIDString);
      }
      catch (NumberFormatException e) {
        _pollID = -1;
      }
    }

    if ( _showCollection ) {
      showCollection(iwc);
    }
    else {
      if (PollBusiness.thisObjectSubmitted(iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER))){
        PollBusiness.handleInsert(iwc);
        if ( iwc.getParameter(PollBusiness._PARAMETER_CLOSE) != null ) {
          setParentToReload();
          close();
        }
      }

      if ( _pollQuestionID != -1 )
        layoutTable.add(showResults(_pollQuestionID),1,3);
    }
  }

  private void drawLayout() {
    layoutTable = new Table(1,5);
    layoutTable.setWidth(280);
    layoutTable.setCellpadding(0);
    layoutTable.setCellspacing(0);
    layoutTable.setHeight(2,"9");
    layoutTable.setHeight(4,"17");
    layoutTable.setAlignment(1,5,"center");
    layoutTable.setBorder(border);

    Image header = _iwrb.getImage("top.gif");
    line = _iwb.getImage("/shared/line.gif");
    CloseButton close = new CloseButton();
    //CloseButton close = new CloseButton(_iwrb.getImage("close.gif"));

    layoutTable.add(header,1,1);
    layoutTable.add(line,1,2);
    layoutTable.add(line,1,4);
    layoutTable.add(close,1,5);

    add(layoutTable);
  }

  private Table showResults(int pollQuestionID) {
    Table myTable = new Table();
      myTable.setBorder(border);
      myTable.setWidth(1,1,"2");
      myTable.mergeCells(2,1,6,1);
      myTable.setWidth("100%");

    PollAnswer[] answers = PollBusiness.getAnswers(pollQuestionID);
    PollQuestion question = null;
    LocalizedText questionLocText = null;
    try {
      question = ((com.idega.block.poll.data.PollQuestionHome)com.idega.data.IDOLookup.getHomeLegacy(PollQuestion.class)).findByPrimaryKeyLegacy(pollQuestionID);
      questionLocText = TextFinder.getLocalizedText(question,_iLocaleID);
    }
    catch (SQLException e) {
      question = null;
    }

    int total = 0;
    int row = 1;
    int current_hits = 0;

    if ( question != null ) {
      Text questionText = new Text(questionLocText.getHeadline());
        questionText.setBold();
      myTable.add(questionText,2,1);

      int numberOfAnswers = PollBusiness.getNumberOfAnswers(question);
      setWindowHeight(numberOfAnswers);

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
                answerText.setFontSize(1);
              Text hitsText = new Text(Integer.toString(answers[i].getHits()));
                hitsText.setFontSize(1);
              Text percentText = new Text(com.idega.util.text.TextSoap.decimalFormat(percent,1)+"%");
                percentText.setFontSize(1);

              myTable.add(answerText,2,row);
              if ( _showVotes )
                myTable.add(hitsText,4,row);
              myTable.add(percentText,6,row);
            }
          }
        }
      }

      myTable.setWidth(1,row,"2");
      myTable.setWidth(2,row,"500");
      myTable.setWidth(3,row,"2");
      myTable.setWidth(4,row,"2");
      myTable.setWidth(5,row,"2");
      myTable.setWidth(6,row,"2");
      myTable.setWidth(7,row,"2");
      myTable.setAlignment(4,row,"right");
      myTable.setAlignment(6,row,"right");
    }

    return myTable;
  }

  private void showCollection(IWContext iwc) {
    int pollSize = 0;
    int first = 0;
    int numberOfAnswers = 0;
    List pollQuestions = PollBusiness.getPollQuestions(_pollID);
    if ( pollQuestions != null ) {
      pollSize = pollQuestions.size();
    }

    String first_string = iwc.getParameter(prmPollFirst);
    try {
      first = Integer.parseInt(first_string);
      if (first < 0 ) {
        first = 0;
      }
      else {
        if ( pollQuestions != null ) {
          if ( first > pollSize - 1 ) {
            first = pollSize - 1;
          }
        }
      }
    }
    catch (NumberFormatException n) {
      first = 0;
    }

    int row = 1;
    int tableSize = _numberOfPolls * 2;
    if ( pollSize < _numberOfPolls ) {
      tableSize = pollSize * 2;
    }

    Table myTable = new Table(1,tableSize);
      myTable.setWidth("100%");
      myTable.setBorder(border);

    if ( pollQuestions != null ) {
      for (int i = first; i < pollSize; i++) {
        if (i == first + _numberOfPolls) {
          break;
        }

        PollQuestion pollQuestion = (PollQuestion) pollQuestions.get(i);
        numberOfAnswers += PollBusiness.getNumberOfAnswers(pollQuestion);

        myTable.add(showResults(pollQuestion.getID()),1,row);
        row++;

        if (this.line != null) {
          if ( row < tableSize ) {
            myTable.add(line,1,row);
            row++;
          }
        }
      }
    }

    setWindowHeight(numberOfAnswers,pollSize);

    Text nextText = new Text(_iwrb.getLocalizedString("next","Next"));
        nextText.setFontSize(1);
    Text prevText = new Text(_iwrb.getLocalizedString("lst","Last"));
        prevText.setFontSize(1);

    Link next = new Link(nextText);
        next.addParameter(Poll._prmPollCollection,PollBusiness._PARAMETER_TRUE);
        next.addParameter(Poll._prmPollID,Integer.toString(_pollID));
        next.addParameter(PollResult.prmPollFirst,Integer.toString(first + _numberOfPolls));
    Link prev = new Link(prevText);
        prev.addParameter(Poll._prmPollCollection,PollBusiness._PARAMETER_TRUE);
        prev.addParameter(Poll._prmPollID,Integer.toString(_pollID));
        prev.addParameter(PollResult.prmPollFirst,Integer.toString(first - _numberOfPolls));


    Table table = new Table(4,1);
        table.setWidth("100%");
        table.setBorder(border);
        table.setAlignment(3,1,"right");
        table.setWidth(1,1,"2");
        table.setWidth(4,1,"10");
        if (! (first - _numberOfPolls < 0)) {
          table.add(prev,2,1);
        }
        else {
          prevText.setFontColor("#999999");
          table.add(prevText,2,1);
        }

        if ( !( first + _numberOfPolls >= pollSize ) ) {
            table.add(next,3,1);
        }
        else {
          nextText.setFontColor("#999999");
          table.add(nextText,3,1);
        }

    myTable.add(table,1,tableSize);
    layoutTable.add(myTable,1,3);
  }

  private void setWindowHeight(int numberOfAnswers) {
    setWindowHeight(numberOfAnswers,1);
  }

  private void setWindowHeight(int numberOfAnswers,int numberOfQuestions) {
    int questionHeight = 26;
    int answerHeight = 26;
    int height = 116;
    if ( numberOfQuestions > 1 ) {
      height += 26;
    }

    int windowHeight = (numberOfAnswers * answerHeight) + (numberOfQuestions * questionHeight) + height;

    setOnLoad("window.resizeTo("+Integer.toString(292)+","+windowHeight+");");
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}

