package com.idega.block.poll.business;

import java.sql.*;
import javax.servlet.http.Cookie;
import com.idega.presentation.IWContext;
import com.idega.block.poll.data.*;
import com.idega.data.EntityFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.business.TextFinder;
import com.idega.core.data.ICObjectInstance;
import com.idega.util.idegaTimestamp;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.database.ConnectionBroker;
import java.util.List;

public class PollBusiness{

public static final String _PARAMETER_POLL_VOTER = "idega_poll_voter";
public static final String _PARAMETER_POLL_ANSWER = "poll_answer_id";
public static final String _PARAMETER_POLL_QUESTION = "poll_question_id";
public static final String _PARAMETER_MODE = "mode";
public static final String _PARAMETER_TRUE = "true";
public static final String _PARAMETER_FALSE = "false";
public static final String _PARAMETER_VOTE = "vote";
public static final String _PARAMETER_DELETE = "delete";
public static final String _PARAMETER_SAVE = "save";
public static final String _PARAMETER_CLOSE = "close";

public static final String COOKIE_NAME = "idegaPOLL_";

  public static PollEntity[] getPolls(int pollQuestionID) {
    try {
      return (PollEntity[]) PollEntity.getStaticInstance(PollEntity.class).findAllByColumn(PollQuestion.getColumnNameID(),Integer.toString(pollQuestionID),"=");
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static PollEntity[] getPolls(PollQuestion pollQuestion) {
    return getPolls(pollQuestion.getID());
  }

  public static PollQuestion getQuestion(int pollID) {
    try {
      return getQuestion(new PollEntity(pollID));
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static PollQuestion getQuestion(PollEntity poll) {
    try {
      if ( poll != null )
        return new PollQuestion(poll.getPollQuestionID());
      return null;
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static idegaTimestamp getStartDate(int pollQuestionID) {
    PollQuestion question = getPollQuestion(pollQuestionID);

    if ( question != null ) {
      if ( question.getStartTime() != null ) {
        return new idegaTimestamp(question.getStartTime());
      }
    }

    return null;
  }

  public static idegaTimestamp getEndDate(int pollQuestionID) {
    PollQuestion question = getPollQuestion(pollQuestionID);

    if ( question != null ) {
      if ( question.getEndTime() != null ) {
        return new idegaTimestamp(question.getEndTime());
      }
    }

    return null;
  }

  public static PollQuestion getPollQuestion(int pollQuestionID) {
    try {
      return new PollQuestion(pollQuestionID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static PollAnswer getPollAnswer(int pollAnswerID) {
    try {
      return new PollAnswer(pollAnswerID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static String getLocalizedQuestion(int pollQuestionID, int iLocaleID) {
    String locString = null;

    PollQuestion pollQuestion = getPollQuestion(pollQuestionID);
    if ( pollQuestion != null ) {
      LocalizedText locText = TextFinder.getLocalizedText(pollQuestion,iLocaleID);
      if ( locText != null ) {
        locString = locText.getHeadline();
      }
    }

    return locString;
  }

  public static String getLocalizedInformation(int pollQuestionID, int iLocaleID) {
    String locString = null;

    PollQuestion pollQuestion = getPollQuestion(pollQuestionID);
    if ( pollQuestion != null ) {
      LocalizedText locText = TextFinder.getLocalizedText(pollQuestion,iLocaleID);
      if ( locText != null ) {
        locString = locText.getBody();
      }
    }

    if ( locString != null ) {
      if ( locString.length() == 0 )
        locString = null;
    }

    return locString;
  }

  public static PollAnswer[] getAnswers(int pollQuestionID) {
    try {
      return (PollAnswer[]) PollAnswer.getStaticInstance(PollAnswer.class).findAllByColumn(PollQuestion.getColumnNameID(),Integer.toString(pollQuestionID),"=");
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static String[] getLocalizedAnswers(int pollQuestionID, int iLocaleID) {
    String[] answers = null;

    PollAnswer[] pollAnswers = getAnswers(pollQuestionID);
    if ( pollAnswers != null ) {
      answers = new String[pollAnswers.length];
      for ( int a = 0; a < pollAnswers.length; a++ ) {
        LocalizedText text = TextFinder.getLocalizedText(pollAnswers[a],iLocaleID);
        if ( text != null ) {
          answers[a] = text.getHeadline();
        }
        else {
          answers[a] = "Option "+Integer.toString(a+1);
        }
      }
    }

    return answers;
  }

  public static String getLocalizedAnswer(int pollAnswerID, int iLocaleID) {
    String locString = null;

    PollAnswer pollAnswer = getPollAnswer(pollAnswerID);
    if ( pollAnswer != null ) {
      LocalizedText locText = TextFinder.getLocalizedText(pollAnswer,iLocaleID);
      if ( locText != null ) {
        locString = locText.getHeadline();
      }
    }

    return locString;
  }

  public static String[] getAnswerIDs(int pollQuestionID) {
    String[] answers = null;

    PollAnswer[] pollAnswers = getAnswers(pollQuestionID);
    if ( pollAnswers != null ) {
      answers = new String[pollAnswers.length];
      for ( int a = 0; a < pollAnswers.length; a++ ) {
        answers[a] = Integer.toString(pollAnswers[a].getID());
      }
    }

    return answers;
  }

  public static void increaseHits(int pollAnswerID) {
    try {
      increaseHits(new PollAnswer(pollAnswerID));
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void increaseHits(PollAnswer pollAnswer) {
    try {
      pollAnswer.setHits(pollAnswer.getHits()+1);
      pollAnswer.update();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void handleInsert(IWContext iwc) {
    String questionString = iwc.getParameter(_PARAMETER_POLL_QUESTION);
    if ( questionString != null ) {
      try {
        handleInsert(iwc,Integer.parseInt(questionString));
      }
      catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
  }

  public static void handleInsert(IWContext iwc, int pollQuestionID) {
    String pollAnswerID = iwc.getParameter(_PARAMETER_POLL_ANSWER);

    PollAnswer answer = null;
    if ( pollAnswerID != null ) {
      try {
        answer = new PollAnswer(Integer.parseInt(pollAnswerID));
      }
      catch (Exception e) {
        answer = null;
      }
    }

    if ( answer != null && canVote(iwc, pollQuestionID) ) {
      increaseHits(answer);

      Cookie cookie = new Cookie(COOKIE_NAME+Integer.toString(pollQuestionID),"true");
      cookie.setMaxAge(31 * 24 * 60 * 60);
      cookie.setPath("/");
      iwc.addCookies(cookie);
    }
  }

  public static boolean canVote(IWContext iwc, int pollQuestionID) {
    Cookie[] cookies = (Cookie[]) iwc.getCookies();
    boolean returner = true;

    if (cookies != null) {
      if (cookies.length > 0) {
        for (int i = 0 ; i < cookies.length ; i++) {
          if ( cookies[i].getName().equals(COOKIE_NAME+Integer.toString(pollQuestionID)) ) {
            returner = false;
            continue;
          }
        }
      }
    }

    return returner;
  }

  public static boolean thisObjectSubmitted(String parameterString){
    boolean returner = false;

    if (parameterString != null){
      if (parameterString.equals(_PARAMETER_TRUE)){
        returner = true;
      }
    }

    return returner;
  }

  public static DropdownMenu getQuestions(String name, int iLocaleId) {
    DropdownMenu drp = new DropdownMenu(name);
      drp.addMenuElementFirst("-1","");
    PollQuestion[] pollQuestion = null;

    try {
      pollQuestion = (PollQuestion[]) PollQuestion.getStaticInstance(PollQuestion.class).findAll();
    }
    catch (SQLException e) {
      pollQuestion = null;
    }

    if( pollQuestion != null ) {
      for ( int a = 0; a < pollQuestion.length; a++) {
        LocalizedText locText = TextFinder.getLocalizedText(pollQuestion[a],iLocaleId);
        String locString = "No question in this language";
        if ( locText != null ) {
          locString = locText.getHeadline();
        }
        drp.addMenuElement(pollQuestion[a].getID(),locString);
      }
    }
    return drp;
  }

  public static List getPollQuestions(int pollID) {
    try {
      return getPollQuestions(new PollEntity(pollID));
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static List getPollQuestions(PollEntity poll) {
    try {
      return com.idega.data.EntityFinder.findRelated(poll,PollQuestion.getStaticInstance(PollQuestion.class));
    }
    catch (SQLException e) {
      return null;
    }
  }

  public static DropdownMenu getQuestions(String name, int userID, int iLocaleId, boolean superAdmin) {
    DropdownMenu drp = new DropdownMenu(name);
      drp.addMenuElementFirst("-1","");
    PollQuestion[] pollQuestion = null;

    try {
      if ( superAdmin )
        pollQuestion = (PollQuestion[]) PollQuestion.getStaticInstance(PollQuestion.class).findAll();
      else
        pollQuestion = (PollQuestion[]) PollQuestion.getStaticInstance(PollQuestion.class).findAllByColumn(PollQuestion.getColumnNameUserID(),Integer.toString(userID),"=");
    }
    catch (SQLException e) {
      pollQuestion = null;
    }

    if( pollQuestion != null ) {
      for ( int a = 0; a < pollQuestion.length; a++) {
        LocalizedText locText = TextFinder.getLocalizedText(pollQuestion[a],iLocaleId);
        String locString = "No question in this language";
        if ( locText != null ) {
          locString = locText.getHeadline();
        }
        drp.addMenuElement(pollQuestion[a].getID(),locString);
      }
    }
    return drp;
  }

  public static int savePollQuestion(int userID,int pollID,int pollQuestionID,String pollQuestionString,String pollInformationString,String pollStartDate,String pollEndDate,int iLocaleID) {
    boolean update = false;
    boolean newLocText = false;
    int _pollQuestionID = -1;

    if ( pollQuestionID != -1 ) {
      update = true;
    }

    PollQuestion pollQuestion = new PollQuestion();
    if ( update ) {
      try {
        pollQuestion = new PollQuestion(pollQuestionID);
      }
      catch (SQLException e) {
        pollQuestion = new PollQuestion();
        update = false;
      }
    }

    if ( pollStartDate != null && pollStartDate.length() > 0 ) {
      pollQuestion.setStartTime(new idegaTimestamp(pollStartDate).getTimestamp());
    }
    if ( pollEndDate != null && pollEndDate.length() > 0 ) {
      pollQuestion.setEndTime(new idegaTimestamp(pollEndDate).getTimestamp());
    }

    if ( !update ) {
      try {
        pollQuestion.setUserID(userID);
        pollQuestion.insert();
        _pollQuestionID = pollQuestion.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        pollQuestion.update();
        _pollQuestionID = pollQuestion.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }


    LocalizedText locText = TextFinder.getLocalizedText(pollQuestion,iLocaleID);
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setHeadline(pollQuestionString);
    locText.setBody(pollInformationString);
    locText.setCreated(com.idega.util.idegaTimestamp.getTimestampRightNow());

    if ( newLocText ) {
      locText.setLocaleId(iLocaleID);
      try {
        locText.insert();
        locText.addTo(pollQuestion);
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        locText.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }

    return _pollQuestionID;

  }

  public static int savePollAnswer(int pollQuestionID,int pollAnswerID,String pollAnswerString,int iLocaleID) {
    boolean update = false;
    boolean newLocText = false;
    int _pollAnswerID = -1;

    if ( pollAnswerID != -1 ) {
      update = true;
    }

    PollAnswer pollAnswer = new PollAnswer();
    if ( update ) {
      try {
        pollAnswer = new PollAnswer(pollAnswerID);
      }
      catch (SQLException e) {
        pollAnswer = new PollAnswer();
        update = false;
      }
    }


    if ( !update ) {
      pollAnswer.setPollQuestionID(pollQuestionID);
      pollAnswer.setHits(0);
      try {
        pollAnswer.insert();
        _pollAnswerID = pollAnswer.getID();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      _pollAnswerID = pollAnswer.getID();
    }


    LocalizedText locText = TextFinder.getLocalizedText(pollAnswer,iLocaleID);
    if ( locText == null ) {
      locText = new LocalizedText();
      newLocText = true;
    }

    locText.setHeadline(pollAnswerString);

    if ( newLocText ) {
      locText.setLocaleId(iLocaleID);
      try {
        locText.insert();
        locText.addTo(pollAnswer);
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      try {
        locText.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }

    return _pollAnswerID;
  }

  public static boolean deletePoll(PollEntity poll) {
    try {
      if ( poll != null ) {
        poll.delete();
      }
      return true;
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
      return false;
    }
  }

  public static void deletePollQuestion(int pollQuestionID) {
    try {
      Connection Conn = null;
      PollQuestion pollQuestion = new PollQuestion(pollQuestionID);
      try {
        Conn = ConnectionBroker.getConnection();
        Conn.createStatement().executeUpdate("update "+PollEntity.getEntityTableName()+" set "+PollQuestion.getColumnNameID()+" = null where "+PollQuestion.getColumnNameID()+" = "+Integer.toString(pollQuestionID));
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
      finally {
        ConnectionBroker.freeConnection(Conn);
      }
      PollAnswer[] pollAnswers = PollBusiness.getAnswers(pollQuestionID);
      if ( pollAnswers != null ) {
        for ( int a = 0; a < pollAnswers.length; a++ ) {
          pollAnswers[a].delete();
        }
      }
      pollQuestion.removeFrom(PollEntity.getStaticInstance(PollEntity.class));
      pollQuestion.delete();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void deletePollAnswer(int pollAnswerID) {
    try {
      new PollAnswer(pollAnswerID).delete();
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
  }

  public static void savePoll(int pollID,int pollQuestionID,int InstanceId,String sAttribute){
    try {
      boolean update = false;

      PollEntity poll = new PollEntity();
      if ( pollID != -1 ) {
        update = true;
        try {
          poll = new PollEntity(pollID);
        }
        catch (SQLException e) {
          poll = new PollEntity();
          update = false;
        }
      }

      if(sAttribute != null){
        PollEntity pollAttribute = PollFinder.getPoll(sAttribute);
        if ( pollAttribute != null ) {
          poll = pollAttribute;
          update = true;
        }
        poll.setAttribute(sAttribute);
      }

      if ( pollQuestionID != -1 ) {
        poll.setPollQuestionID(pollQuestionID);
      }

      if ( update ) {
        try {
          poll.update();
        }
        catch (SQLException e) {
          e.printStackTrace(System.err);
        }
      }
      else {
        poll.insert();
        if(InstanceId > 0){
          System.err.println("instance er til");
          ICObjectInstance objIns = new ICObjectInstance(InstanceId);
          System.err.println(" object instance "+objIns.getID() +", "+ objIns.getName());
          poll.addTo(objIns);
        }
      }

      if ( pollQuestionID != -1 ) {
        addToPoll(poll,pollQuestionID);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void addToPoll(PollEntity poll, int pollQuestionID) {
    try {
      PollQuestion question = getPollQuestion(pollQuestionID);
      if ( question != null ) {
        PollQuestion[] polls = (PollQuestion[]) poll.findRelated(new PollQuestion(pollQuestionID));
        if ( polls == null || polls.length == 0 ) {
          poll.addTo(question);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public static int getNumberOfAnswers(int pollQuestionID) {
    try {
      return getNumberOfAnswers(new PollQuestion(pollQuestionID));
    }
    catch (SQLException e) {
      return -1;
    }
  }

  public static int getNumberOfAnswers(PollQuestion pollQuestion) {
    try {
      return pollQuestion.getNumberOfRecords("select count(*) from po_poll_answer where "+pollQuestion.getIDColumnName()+"="+Integer.toString(pollQuestion.getID()));
    }
    catch (SQLException e) {
      return -1;
    }
  }

  public static PollQuestion getPollByDate(PollEntity poll, idegaTimestamp date) {
    try {
      boolean isActive = false;
      List polls = getPollQuestions(poll);

      if ( polls != null ) {
        for ( int a = 0; a < polls.size(); a++ ) {
          idegaTimestamp before = new idegaTimestamp(((PollQuestion) polls.get(a)).getStartTime());
          idegaTimestamp after = new idegaTimestamp(((PollQuestion) polls.get(a)).getEndTime());

          if ( before != null ) {
            if ( date.isLaterThan(before) ) {
              isActive = true;
            }
            else {
              isActive = false;
            }
          }
          if ( after != null ) {
            if ( after.isLaterThan(date) && isActive ) {
              isActive = true;
            }
            else {
              isActive = false;
            }
          }

          if ( isActive ) {
            return (PollQuestion) polls.get(a);
          }
        }
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static void setPollQuestion(PollEntity poll,PollQuestion pollQuestion) {
    if ( pollQuestion != null ) {
      poll.setPollQuestionID(pollQuestion.getID());
      try {
        poll.update();
      }
      catch (SQLException e) {
        e.printStackTrace(System.err);
      }
    }
  }
}