package com.idega.projects.golf;

/**
 * Title: TournamentResults
 * Description: Displayes the results of a tournament
 * Copyright:    Copyright (c) 2001
 * Company: idega co.
 * @author  Laddi
 * @version 1.3
 */

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.business.*;
import com.idega.projects.golf.entity.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class TournamentResults extends JModuleObject {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private int tournamentId_ = -1;
  private int tournamentGroupId_ = -1;
  private int tournamentType_ = -1;
  private int[] tournamentRounds_ = null;
  private String gender_ = null;
  private int sortBy = -1;

  private Vector result = null;
  private Table myTable = null;
  private Tournament tournament = null;
  private int numberOfRounds = -1;
  private int numberOfColumns = -1;

  public static final int TOTALSTROKES = 1;
  public static final int TOTALSTROKESWITHHANDICAP = 2;
  public static final int TOTALPOINTS = 3;
  public static final int NAME = 4;
  public static final int ABBREVATION = 5;
  public static final int TOURNAMENTROUND = 6;

  public void sortBy(int toSortBy) {
      sortBy = toSortBy;
  }

  public TournamentResults(int tournamentId, int tournamentType) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
  }

  public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
  }

  public TournamentResults(int tournamentId, int tournamentType, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    gender_ = gender;
  }

  public TournamentResults(int tournamentId, int tournamentType, int[] tournamentRounds) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentRounds_ = tournamentRounds;
  }

  public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    gender_ = gender;
  }

  public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    tournamentRounds_ = tournamentRounds;
  }

  public TournamentResults(int tournamentId, int tournamentType, int[] tournamentRounds, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentRounds_ = tournamentRounds;
    gender_ = gender;
  }

  public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds, String gender) {
    tournamentId_ = tournamentId;
    tournamentType_ = tournamentType;
    tournamentGroupId_ = tournamentGroupId;
    tournamentRounds_ = tournamentRounds;
    gender_ = gender;
  }

  public void main(ModuleInfo modinfo) throws SQLException {
    try {
      iwrb = getResourceBundle(modinfo);
      iwb = getBundle(modinfo);
      tournament = new Tournament(tournamentId_);
      numberOfRounds = tournament.getNumberOfRounds();
      getMemberVector();
      sortMemberVector();
      drawResultTable();
      getResults();
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  private void getMemberVector() {
    try {
      ResultDataHandler handler = new ResultDataHandler(tournamentId_,tournamentType_,tournamentGroupId_,tournamentRounds_,gender_);
      result = handler.getTournamentMembers();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void sortMemberVector() {
    ResultComparator comparator = new ResultComparator(sortBy);
    Collections.sort(result,comparator);
  }

  private void drawResultTable() {
    try {
      myTable = new Table();
        myTable.setCellpadding(1);
        myTable.setCellspacing(1);
        myTable.setWidth("100%");
        myTable.setBorder(0);

      String[] headers = { iwrb.getLocalizedString("tournament.position","Position"),iwrb.getLocalizedString("tournament.golfer","Member"),iwrb.getLocalizedString("tournament.club","Club"),iwrb.getLocalizedString("tournament.handicap_short","Hcp.") };
      for ( int a = 0; a < headers.length; a++ ) {
        if ( a == 0 && ( this.sortBy == ResultComparator.NAME || this.sortBy == ResultComparator.ABBREVATION ) ) {
          addHeaders("",a+1,1);
        }
        else {
          addHeaders(headers[a],a+1,1);
        }
        myTable.mergeCells(a+1,1,a+1,2);
      }

      getTotalHeaders();

      add(myTable);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getResults() {
    try {
      int size = 0;
      if ( result != null )
        size = result.size();

      for ( int a = 0; a < size; a++ ) {
        ResultsCollector collector = (ResultsCollector) result.elementAt(a);
        int handicap = collector.getHandicap();
        int finalScore = collector.getTotalScore();
        int difference = collector.getDifference();
        String hole = collector.getHole();

        Text memberText = new Text(collector.getName());
          memberText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Window scoreWindow = new Window(iwrb.getLocalizedString("tournament.scorecard","Scorecard"),650,650,"/tournament/handicap_skor.jsp");
        Image linkImage = iwb.getImage("shared/view.gif",iwrb.getLocalizedString("tournament.view_scorecard","View scorecards"),9,18);
          linkImage.setHorizontalSpacing(4);
          linkImage.setAttribute("align","absmiddle");
        Link seeScores = new Link(linkImage,scoreWindow);
                seeScores.addParameter("member_id",collector.getMemberId());
                seeScores.addParameter("tournament_id",tournamentId_);
                seeScores.addParameter("tournament_group_id",collector.getTournamentGroupId());
        Link seeScoresMember = new Link(memberText,scoreWindow);
                seeScoresMember.addParameter("member_id",collector.getMemberId());
                seeScoresMember.addParameter("tournament_id",tournamentId_);
                seeScoresMember.addParameter("tournament_group_id",collector.getTournamentGroupId());

        Text positionText = new Text(Integer.toString(a+1));
          positionText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text clubText = new Text(collector.getAbbrevation());
          clubText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text handicapText = new Text(Integer.toString(handicap));
          handicapText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text holeText = new Text(hole);
          holeText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text firstNineText = new Text();
          firstNineText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text lastNineText = new Text();
          lastNineText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text totalRoundScore = new Text();
          totalRoundScore.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text diffText = new Text();
          diffText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        Text bruttoText = new Text();
          bruttoText.setFontSize(Text.FONT_SIZE_7_HTML_1);

        int totalScore = 0;
        int totalDifference = 0;
        int totalBrutto = 0;
        int roundNumber = 0;

        Vector roundScore = collector.getRoundScore();

        if ( hole.equalsIgnoreCase("f") ) {
          int lastNine = (int) collector.getRealLastNine();
          if ( roundScore != null ) {
            totalScore = ((Integer)roundScore.elementAt(roundScore.size()-1)).intValue();
            totalDifference = totalScore - collector.getFieldPar();
            totalBrutto = totalScore + handicap;
          }
          int firstNine = totalScore - lastNine;
          if ( this.tournamentType_ == ResultComparator.TOTALSTROKESWITHHANDICAP ) {
            firstNine = totalBrutto - lastNine;
          }

          lastNineText.setText(Integer.toString(lastNine));
          firstNineText.setText(Integer.toString(firstNine));
          totalRoundScore.setText(Integer.toString(totalScore));
          diffText.setText(Integer.toString(totalDifference));
          bruttoText.setText(Integer.toString(totalBrutto));
        }

        switch (tournamentType_) {
          case ResultComparator.TOTALSTROKES :
            int roundScoreColumn = 10;
            for ( int b = 1; b <= numberOfRounds; b++ ) {
              int roundScore2 = 0;
              int roundIncNumber = collector.getRound(b);
              int position = roundScoreColumn + roundIncNumber - 1;

              if ( collector.getRoundNumber(b) != 0 ) {
                roundScore2 = collector.getRoundScore(collector.getRoundNumber(b));
              }

              Text roundScoreText = new Text(Integer.toString(roundScore2));
                roundScoreText.setFontSize(Text.FONT_SIZE_7_HTML_1);

              if ( roundScore2 > 0 ) {
                myTable.add(roundScoreText,position,a+3);
              }
            }
            Text finalScoreText = new Text(Integer.toString(finalScore));
              finalScoreText.setFontSize(Text.FONT_SIZE_7_HTML_1);
              finalScoreText.setBold();
              finalScoreText.setFontFace("Verdana,Arial,sans-serif");
            Text finalDifferenceText = new Text(Integer.toString(difference));
              finalDifferenceText.setFontSize(Text.FONT_SIZE_7_HTML_1);
              finalDifferenceText.setBold();
              finalDifferenceText.setFontFace("Verdana,Arial,sans-serif");
            if ( finalScore > 0 ) {
              myTable.add(finalScoreText,numberOfColumns-1,a+3);
              myTable.add(finalDifferenceText,numberOfColumns,a+3);
            }
            myTable.add(totalRoundScore,8,a+3);
            myTable.add(diffText,9,a+3);
          break;

          case ResultComparator.TOTALSTROKESWITHHANDICAP :
            int roundScoreColumn2 = 10;
            int roundSize = 0;
            Vector roundSizeVector = collector.getRoundScore();
            if ( roundSizeVector != null ) {
              roundSize = roundSizeVector.size();
            }

            for ( int b = 1; b <= numberOfRounds; b++ ) {
              int position2 = roundScoreColumn2 + collector.getRound(b) - 1;
              int roundScore2 = collector.getRoundScore(b);
              int roundScoreBrutto = roundScore2 + handicap;
              Text roundScoreText = new Text(Integer.toString(roundScore2));
                roundScoreText.setFontSize(Text.FONT_SIZE_7_HTML_1);
              Text roundScoreBruttoText = new Text(Integer.toString(roundScoreBrutto));
                roundScoreBruttoText.setFontSize(Text.FONT_SIZE_7_HTML_1);
              if ( roundScore2 > 0 ) {
                myTable.add(roundScoreBruttoText,position2,a+3);
                myTable.add(roundScoreText,position2+1,a+3);
              }
            }
            Text finalBruttoText = new Text(Integer.toString(collector.getTotalStrokes()));
              finalBruttoText.setFontSize(Text.FONT_SIZE_7_HTML_1);
              finalBruttoText.setBold();
              finalBruttoText.setFontFace("Verdana,Arial,sans-serif");
            Text finalScoreText2 = new Text(Integer.toString(finalScore));
              finalScoreText2.setFontSize(Text.FONT_SIZE_7_HTML_1);
              finalScoreText2.setBold();
              finalScoreText2.setFontFace("Verdana,Arial,sans-serif");
            if ( finalScore > 0 ) {
              myTable.add(finalBruttoText,numberOfColumns-1,a+3);
              myTable.add(finalScoreText2,numberOfColumns,a+3);
            }
            myTable.add(bruttoText,8,a+3);
            myTable.add(totalRoundScore,9,a+3);
          break;

          case ResultComparator.TOTALPOINTS :
            int roundScoreColumn3 = 9;
            for ( int b = 1; b <= numberOfRounds; b++ ) {
              int position3 = roundScoreColumn3 + collector.getRound(b) - 1;
              int roundScore2 = collector.getRoundScore(b);
              Text roundScoreText = new Text(Integer.toString(roundScore2));
                roundScoreText.setFontSize(Text.FONT_SIZE_7_HTML_1);
              if ( roundScore2 > 0 ) {
                myTable.add(roundScoreText,position3,a+3);
              }
            }
            Text finalScoreText3 = new Text(Integer.toString(finalScore));
              finalScoreText3.setFontSize(Text.FONT_SIZE_7_HTML_1);
              finalScoreText3.setBold();
              finalScoreText3.setFontFace("Verdana,Arial,sans-serif");
            if ( finalScore > 0 )
              myTable.add(finalScoreText3,numberOfColumns,a+3);
            myTable.add(totalRoundScore,8,a+3);
          break;
        }

        myTable.add(positionText,1,a+3);
        myTable.add(seeScores,2,a+3);
        myTable.add(seeScoresMember,2,a+3);
        myTable.add(clubText,3,a+3);
        if ( finalScore > 0 ) {
          myTable.add(handicapText,4,a+3);
        }
        myTable.add(holeText,5,a+3);
        myTable.add(firstNineText,6,a+3);
        myTable.add(lastNineText,7,a+3);
        myTable.setHeight(a+3,"20");

      }
      for ( int c = 1; c <= numberOfColumns; c++ ) {
        if ( c != 2 ) {
          myTable.setColumnAlignment(c,"center");
        }
      }
      myTable.setAlignment(2,1,"center");
      myTable.setHorizontalZebraColored("#DCEFDE","#EAFAEC");
      myTable.setRowColor(1,"#2C4E3B");
      myTable.setRowColor(2,"#2C4E3B");
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void addHeaders(String header, int column, int row) {
    try {
      Text headerText = new Text(header);
        headerText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        headerText.setBold();
        headerText.setFontFace(Text.FONT_FACE_VERDANA);
        headerText.setFontColor("#FFFFFF");

      myTable.add(headerText,column,row);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getTotalHeaders() {
    try {
      String frontNine = iwrb.getLocalizedString("tournament.front_nine","F9");
      String backNine = iwrb.getLocalizedString("tournament.back_nine","B9");
      String total = iwrb.getLocalizedString("tournament.total","Total");
      String netto = iwrb.getLocalizedString("tournament.net","Net");
      String difference = iwrb.getLocalizedString("tournament.difference","Difference");

      int firstColumn = 6;
      int column = firstColumn;
      addHeaders(frontNine,column,2);
      addHeaders(backNine,column+1,2);

      String roundShort = iwrb.getLocalizedString("tournament.round_short","R");
      if ( tournament.getNumberOfRounds() > 4 ) {
        roundShort = iwrb.getLocalizedString("tournament.day_short","D");
      }

      String rounds = iwrb.getLocalizedString("tournament.rounds","Rounds");
      if ( tournament.getNumberOfRounds() > 4 ) {
        rounds = iwrb.getLocalizedString("tournament.days","Days");
      }

      switch (tournamentType_) {
        case ResultComparator.TOTALSTROKES :
          addHeaders(total,column+2,2);
          addHeaders(difference,column+3,2);
          column += 4;
          for ( int a = 0; a < numberOfRounds; a++ ) {
            addHeaders(roundShort+Integer.toString(a+1),column+a,2);
          }
          myTable.mergeCells(column,1,column+numberOfRounds-1,1);
          addHeaders(rounds,column,1);
          addHeaders(total,column+numberOfRounds,2);
          addHeaders(difference,column+numberOfRounds+1,2);
          myTable.mergeCells(column+numberOfRounds,1,column+numberOfRounds+1,1);
          addHeaders(iwrb.getLocalizedString("tournament.total","Total"),column+numberOfRounds,1);
        break;

        case ResultComparator.TOTALSTROKESWITHHANDICAP :
          addHeaders(total,column+2,2);
          addHeaders(netto,column+3,2);
          column += 4;
          int roundColumn = column;
          for ( int a = 0; a < numberOfRounds; a++ ) {
            myTable.mergeCells(roundColumn,1,roundColumn+1,1);
            addHeaders(total,roundColumn,2);
            addHeaders(netto,roundColumn+1,2);
            addHeaders(roundShort+Integer.toString(a+1),roundColumn,1);
            roundColumn += 2;
          }
          addHeaders(total,roundColumn,2);
          addHeaders(netto,roundColumn+1,2);
          myTable.mergeCells(roundColumn,1,roundColumn+1,1);
          addHeaders(iwrb.getLocalizedString("tournament.total","Total"),roundColumn,1);
        break;

        case ResultComparator.TOTALPOINTS :
          addHeaders(total,column+2,2);
          column += 3;
          for ( int a = 0; a < numberOfRounds; a++ ) {
            addHeaders(roundShort+Integer.toString(a+1),column+a,2);
          }
          myTable.mergeCells(column,1,column+numberOfRounds-1,1);
          addHeaders(rounds,column,1);
          myTable.mergeCells(column+numberOfRounds,1,column+numberOfRounds,2);
          addHeaders(iwrb.getLocalizedString("tournament.total","Total"),column+numberOfRounds,1);
        break;
      }

      myTable.mergeCells(firstColumn-1,1,column-1,1);
      addHeaders(iwrb.getLocalizedString("tournament.hole","Hole"),firstColumn-1,2);
      addHeaders(iwrb.getLocalizedString("tournament.last_round","Last round"),firstColumn-1,1);

      numberOfColumns = myTable.getColumns();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}