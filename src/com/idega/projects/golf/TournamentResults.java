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
import com.idega.util.text.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.business.*;
import com.idega.projects.golf.entity.*;

public class TournamentResults extends JModuleObject {

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

      String[] headers = { "Sæti","Kylfingur","Klúbbur","Fgj." };
      for ( int a = 0; a < headers.length; a++ ) {
        addHeaders(headers[a],a+1,1);
        myTable.mergeCells(a+1,1,a+1,2);
        //myTable.setVerticalAlignment(a+1,1,"bottom");
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

        Text positionText = new Text(Integer.toString(a+1));
          positionText.setFontSize(1);
        Text memberText = new Text(collector.getName());
          memberText.setFontSize(1);
        Text clubText = new Text(collector.getAbbrevation());
          clubText.setFontSize(1);
        Text handicapText = new Text(Integer.toString(handicap));
          handicapText.setFontSize(1);
        Text holeText = new Text(hole);
          holeText.setFontSize(1);
        Text firstNineText = new Text();
          firstNineText.setFontSize(1);
        Text lastNineText = new Text();
          lastNineText.setFontSize(1);
        Text totalRoundScore = new Text();
          totalRoundScore.setFontSize(1);
        Text diffText = new Text();
          diffText.setFontSize(1);
        Text bruttoText = new Text();
          bruttoText.setFontSize(1);

        int totalScore = 0;
        int totalDifference = 0;
        int totalBrutto = 0;

        if ( hole.equalsIgnoreCase("f") ) {
          int lastNine = (int) collector.getLastNine();
          Vector roundScore = collector.getRoundScore();
          if ( roundScore != null ) {
            totalScore = ((Integer)roundScore.elementAt(roundScore.size()-1)).intValue();
            totalDifference = totalScore - collector.getFieldPar();
            totalBrutto = totalScore + handicap;
          }
          int firstNine = totalScore - lastNine;

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
              int roundScore = collector.getRoundScore(b);
              Text roundScoreText = new Text(Integer.toString(roundScore));
                roundScoreText.setFontSize(1);
              if ( roundScore > 0 ) {
                myTable.add(roundScoreText,roundScoreColumn,a+3);
                roundScoreColumn++;
              }
            }
            Text finalScoreText = new Text(Integer.toString(finalScore));
              finalScoreText.setFontSize(1);
              finalScoreText.setBold();
              finalScoreText.setFontFace("Verdana,Arial,sans-serif");
            Text finalDifferenceText = new Text(Integer.toString(difference));
              finalDifferenceText.setFontSize(1);
              finalDifferenceText.setBold();
              finalDifferenceText.setFontFace("Verdana,Arial,sans-serif");
            myTable.add(finalScoreText,numberOfColumns-1,a+3);
            myTable.add(finalDifferenceText,numberOfColumns,a+3);
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
              int roundScore = collector.getRoundScore(b);
              int roundScoreBrutto = roundScore + handicap;
              Text roundScoreText = new Text(Integer.toString(roundScore));
                roundScoreText.setFontSize(1);
              Text roundScoreBruttoText = new Text(Integer.toString(roundScoreBrutto));
                roundScoreBruttoText.setFontSize(1);
              if ( roundScore > 0 ) {
                myTable.add(roundScoreBruttoText,roundScoreColumn2,a+3);
                myTable.add(roundScoreText,roundScoreColumn2+1,a+3);
                roundScoreColumn2 += 2;
              }
            }
            Text finalBruttoText = new Text(Integer.toString(collector.getTotalStrokes()));
              finalBruttoText.setFontSize(1);
              finalBruttoText.setBold();
              finalBruttoText.setFontFace("Verdana,Arial,sans-serif");
            Text finalScoreText2 = new Text(Integer.toString(finalScore));
              finalScoreText2.setFontSize(1);
              finalScoreText2.setBold();
              finalScoreText2.setFontFace("Verdana,Arial,sans-serif");
            myTable.add(finalBruttoText,numberOfColumns-1,a+3);
            myTable.add(finalScoreText2,numberOfColumns,a+3);
            myTable.add(bruttoText,8,a+3);
            myTable.add(totalRoundScore,9,a+3);
          break;

          case ResultComparator.TOTALPOINTS :
            int roundScoreColumn3 = 9;
            for ( int b = 1; b <= numberOfRounds; b++ ) {
              int roundScore = collector.getRoundScore(b);
              Text roundScoreText = new Text(Integer.toString(roundScore));
                roundScoreText.setFontSize(1);
              if ( roundScore > 0 ) {
                myTable.add(roundScoreText,roundScoreColumn3,a+3);
                roundScoreColumn3++;
              }
            }
            Text finalScoreText3 = new Text(Integer.toString(finalScore));
              finalScoreText3.setFontSize(1);
              finalScoreText3.setBold();
              finalScoreText3.setFontFace("Verdana,Arial,sans-serif");
            myTable.add(finalScoreText3,numberOfColumns,a+3);
            myTable.add(totalRoundScore,8,a+3);
          break;
        }

        myTable.add(positionText,1,a+3);
        myTable.add(memberText,2,a+3);
        myTable.add(clubText,3,a+3);
        myTable.add(handicapText,4,a+3);
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
      myTable.setRowColor(1,"#336661");
      myTable.setRowColor(2,"#336661");
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void addHeaders(String header, int column, int row) {
    try {
      Text headerText = new Text(header);
        headerText.setFontSize(1);
        headerText.setBold();
        headerText.setFontFace("Verdana,Arial,sans-serif");
        headerText.setFontColor("#FFFFFF");

      myTable.add(headerText,column,row);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getTotalHeaders() {
    try {
      String frontNine = "F9";
      String backNine = "S9";
      String total = "Sam";
      String netto = "Net";
      String difference = "Staða";

      int firstColumn = 6;
      int column = firstColumn;
      addHeaders(frontNine,column,2);
      addHeaders(backNine,column+1,2);

      switch (tournamentType_) {
        case ResultComparator.TOTALSTROKES :
          addHeaders(total,column+2,2);
          addHeaders(difference,column+3,2);
          column += 4;
          for ( int a = 0; a < numberOfRounds; a++ ) {
            addHeaders("H"+Integer.toString(a+1),column+a,2);
          }
          myTable.mergeCells(column,1,column+numberOfRounds-1,1);
          addHeaders("Hringir",column,1);
          addHeaders(total,column+numberOfRounds,2);
          addHeaders(difference,column+numberOfRounds+1,2);
          myTable.mergeCells(column+numberOfRounds,1,column+numberOfRounds+1,1);
          addHeaders("Samtals",column+numberOfRounds,1);
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
            addHeaders("H"+Integer.toString(a+1),roundColumn,1);
            roundColumn += 2;
          }
          addHeaders(total,roundColumn,2);
          addHeaders(netto,roundColumn+1,2);
          myTable.mergeCells(roundColumn,1,roundColumn+1,1);
          addHeaders("Samtals",roundColumn,1);
        break;

        case ResultComparator.TOTALPOINTS :
          addHeaders(total,column+2,2);
          column += 3;
          for ( int a = 0; a < numberOfRounds; a++ ) {
            addHeaders("H"+Integer.toString(a+1),column+a,2);
          }
          myTable.mergeCells(column,1,column+numberOfRounds-1,1);
          addHeaders("Hringir",column,1);
          myTable.mergeCells(column+numberOfRounds,1,column+numberOfRounds,2);
          addHeaders("Samtals",column+numberOfRounds,1);
        break;
      }

      myTable.mergeCells(firstColumn-1,1,column-1,1);
      addHeaders("Hola",firstColumn-1,2);
      addHeaders("Í dag",firstColumn-1,1);

      numberOfColumns = myTable.getColumns();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

}