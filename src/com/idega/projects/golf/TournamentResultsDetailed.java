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

public class TournamentResultsDetailed extends JModuleObject {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private int tournamentId_ = -1;
  private int tournamentGroupId_ = -1;
  private int tournamentRound_ = -1;

  private Tournament tournament;
  private Vector result = null;
  private Table myTable;
  private Text blackText;
  private Text whiteText;

  private int[] pastTournamentRounds_ = null;
  private int[] allTournamentRounds_;


  private int totalPar = 0;
  private int outValue = 0;
  private int inValue = 0;

  public TournamentResultsDetailed(int tournamentId, int tournamentGroupId, int tournamentRound) {
    tournamentId_ = tournamentId;
    tournamentGroupId_ = tournamentGroupId;
    tournamentRound_ = tournamentRound;
  }

  public void main(ModuleInfo modinfo) throws SQLException {
    try {
      iwrb = getResourceBundle(modinfo);
      iwb = getBundle(modinfo);
      tournament = new Tournament(tournamentId_);
      getMemberVector();
      setMemberVectorPastRounds();
      setMemberVectorAllRounds();
      if ( result != null ) {
        if ( result.size() > 1 ) {
          sortMemberVector();
        }
      }

      setValues();
      getFieldInfo();
      getResults();
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  private void getResults() {
    try {
      int size = result.size();
      int row = 5;

      int[] tournamentRounds_ = new int[1];
        tournamentRounds_[0] = tournamentRound_;

      ResultDataHandler handler;

      for ( int a = 0; a < size; a++ ) {
        ResultsCollector r = (ResultsCollector) result.elementAt(a);

        handler = new ResultDataHandler(tournamentId_,ResultComparator.TOTALSTROKES,tournamentGroupId_,tournamentRounds_,r.getMemberId());
        Vector v = handler.getTournamentMembers();
        if (v != null) {
          if (v.size() > 0) {
            r = (ResultsCollector) v.elementAt(0);
          }
        }

        if ( size <= 1 ) {
          r.calculateCompareInfo();
        }

        if ( r.getDismissal() == 0 || r.getDismissal() == 15 ) {
          myTable.mergeCells(1,row+2,22,row+2);
          myTable.addText("",1,row+2);
          myTable.setHeight(1,row+2,"5");
          getMemberScore(r,row);
          row += 3;
        }
      }

      for ( int a = 2; a <= myTable.getColumns(); a++ ) {
        myTable.setColumnAlignment(a,"center");
      }
      myTable.setColumnAlignment(1,"right");
      myTable.setAlignment(1,1,"center");
      myTable.setRowColor(1,"#2C4E3B");
      myTable.setRowColor(2,"#2C4E3B");
      myTable.setRowColor(3,"#DCEFDE");
      myTable.mergeCells(1,4,22,4);
      myTable.addText("",1,4);
      //myTable.setColor(1,4,"#2C4E3B");
      myTable.setHeight(1,4,"5");

      add(myTable);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getMemberScore(ResultsCollector r, int row) {
    try {
      int column = 2;
      int strokeValue = 0;
      int parValue = 0;
      int outScore = 0;
      int inScore = 0;
      int totalStrokes = 0;
      int difference = 0;

      if ( this.pastTournamentRounds_ != null ) {
        ResultDataHandler handler = new ResultDataHandler(tournamentId_,ResultComparator.TOTALSTROKES,tournamentGroupId_,pastTournamentRounds_,r.getMemberId());
        Vector v = handler.getTournamentMembers();
        if (v != null) {
          if (v.size() > 0) {
            ResultsCollector rip = (ResultsCollector) v.get(0);
            if (rip != null) {
                rip.calculateCompareInfo();
                difference = rip.getDifference();
            }
            Text pastDifference = getDifference(difference);
              myTable.add(pastDifference,1,row+1);
          }
        }
      }


      myTable.setHeight(row,"20");
      myTable.setHeight(row+1,"20");
      myTable.setRowColor(row,"#EAFAEC");
      myTable.setRowColor(row+1,"#DCEFDE");

      Text member = (Text) blackText.clone();
        member.setText("&nbsp;"+r.getName()+"&nbsp;");
        myTable.add(member,1,row);

      Vector strokes = r.getStrokes();
      Vector pars = r.getPar();

      if ( strokes != null ) {
        int strokeSize = strokes.size();
        for ( int b = 0; b < strokeSize; b++ ) {
          strokeValue = ((Double)strokes.elementAt(b)).intValue();

          if (strokeValue > 0) {

              parValue = ((Integer)pars.elementAt(b)).intValue();

              difference += strokeValue - parValue;
              totalStrokes += strokeValue;

              Text stroke = (Text) blackText.clone();
                stroke.setText(Integer.toString(strokeValue));
              Text differ = getDifference(difference);

              String color = this.getBackgroundColor(strokeValue,parValue);
              if ( color != null ) {
                myTable.setColor(column,row,color);
                stroke.setFontColor("#FFFFFF");
              }

              myTable.add(stroke,column,row);
              myTable.add(differ,column,row+1);
              column++;

              if ( b+1 == 9 ) {
                outScore = totalStrokes;
                Text outValueText = (Text) blackText.clone();
                  outValueText.setText(Integer.toString(outScore));
                  outValueText.setFontColor(getColor(outScore,outValue));
                myTable.add(outValueText,column,row);
                myTable.add(differ,column,row+1);
                column++;
              }

              if ( b+1 == 18 ) {
                inScore = totalStrokes - outScore;
                Text inValueText = (Text) blackText.clone();
                  inValueText.setText(Integer.toString(inScore));
                  inValueText.setFontColor(getColor(inScore,inValue));
                myTable.add(inValueText,column,row);
                myTable.add(differ,column,row+1);
                column++;
              }

          }

        }

        Text totalText = (Text) blackText.clone();
          totalText.setText(Integer.toString(totalStrokes));
          totalText.setFontColor(getColor(totalStrokes,totalPar));
        myTable.add(totalText,22,row);
        myTable.add(getDifference(difference),22,row+1);
      }

    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private Text getDifference(int difference) {
    String differ = "";
    Text differenceText = (Text) blackText.clone();

    if ( difference > 0 ) {
      differ = "+" + Integer.toString(difference);
      differenceText.setText(differ);
      differenceText.setFontColor("#0000FF");
    }
    else if ( difference == 0 ) {
      differ = "E";
      differenceText.setText(differ);
    }
    else {
      differ = Integer.toString(difference);
      differenceText.setText(differ);
      differenceText.setFontColor("#FF0000");
    }

    return differenceText;
  }

  private void setValues() {
    try {
      getParentPage().setAllMargins(0);
      getParentPage().setTitle(tournament.getName());

      myTable = new Table();
        myTable.setAlignment("center");
        myTable.setCellpadding(1);
        myTable.setCellspacing(1);
        myTable.setWidth("780");

      blackText = new Text();
        blackText.setFontSize(Text.FONT_SIZE_7_HTML_1);

      whiteText = new Text();
        whiteText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        whiteText.setFontColor("#FFFFFF");
        whiteText.setBold();
        whiteText.setFontFace(Text.FONT_FACE_VERDANA);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getFieldInfo() {
    try {
      int parValue = 0;
      int column = 2;
      Tee[] tee = (Tee[]) Tee.getStaticInstance("com.idega.projects.golf.entity.Tee").findAll("select distinct hole_number,par from tee where field_id = "+tournament.getFieldId()+" order by hole_number");

      Text holeText = (Text) whiteText.clone();
        holeText.setText(iwrb.getLocalizedString("tournament.hole","Hole")+"&nbsp;");
      Text parText = (Text) blackText.clone();
        parText.setText(iwrb.getLocalizedString("tournament.par","Par")+"&nbsp;");
        parText.setBold();
        parText.setFontFace(Text.FONT_FACE_VERDANA);
      Text outText = (Text) whiteText.clone();
        outText.setText(iwrb.getLocalizedString("tournament.out","Out"));
        outText.setBold();
        outText.setFontFace(Text.FONT_FACE_VERDANA);
      Text inText = (Text) whiteText.clone();
        inText.setText(iwrb.getLocalizedString("tournament.in","In"));
        inText.setBold();
        inText.setFontFace(Text.FONT_FACE_VERDANA);
      Text totalText = (Text) whiteText.clone();
        totalText.setText(iwrb.getLocalizedString("tournament.total","Total"));
        totalText.setBold();
        totalText.setFontFace(Text.FONT_FACE_VERDANA);

      for ( int b = 0; b < tee.length; b++ ) {
        parValue = tee[b].getPar();
        totalPar += parValue;
        Text par = (Text) blackText.clone();
          par.setText(Integer.toString(parValue));
          par.setBold();
          par.setFontFace(Text.FONT_FACE_VERDANA);
        Text hole = (Text) whiteText.clone();
          hole.setText(Integer.toString(b+1));

        myTable.add(hole,column,2);
        myTable.add(par,column,3);
        myTable.setWidth(column,2,"25");
        column++;

        if ( b+1 == 9 ) {
          outValue = totalPar;
          Text outValueText = (Text) blackText.clone();
            outValueText.setText(Integer.toString(outValue));
            outValueText.setBold();
            outValueText.setFontFace(Text.FONT_FACE_VERDANA);
          myTable.add(outText,column,2);
          myTable.setWidth(column,2,"30");
          myTable.add(outValueText,column,3);
          column++;
        }

        if ( b+1 == 18 ) {
          inValue = totalPar - outValue;
          Text inValueText = (Text) blackText.clone();
            inValueText.setText(Integer.toString(inValue));
            inValueText.setBold();
            inValueText.setFontFace(Text.FONT_FACE_VERDANA);
          myTable.add(inText,column,2);
          myTable.setWidth(column,2,"30");
          myTable.add(inValueText,column,3);
          column++;
        }

      }

      Text totalParText = (Text) blackText.clone();
        totalParText.setText(Integer.toString(totalPar));
        totalParText.setBold();
        totalParText.setFontFace(Text.FONT_FACE_VERDANA);
      myTable.setWidth(column,2,"40");
      myTable.add(totalText,column,2);
      myTable.add(totalParText,column,3);
      myTable.add(holeText,1,2);
      myTable.add(parText,1,3);

      myTable.setHeight(2,"20");
      myTable.setHeight(3,"20");

      myTable.mergeCells(1,1,myTable.getColumns(),1);

      Text tournamentText = new Text("&nbsp;&nbsp;"+tournament.getName());
        tournamentText.setBold();
        tournamentText.setFontSize(Text.FONT_SIZE_10_HTML_2);
        tournamentText.setFontFace(Text.FONT_FACE_VERDANA);
        tournamentText.setFontColor("FFFFFF");

      Text groupAndRound = new Text(new TournamentGroup(this.tournamentGroupId_).getName()+" - "+iwrb.getLocalizedString("tournament.round","Round")+" "+new TournamentRound(this.tournamentRound_).getRoundNumber()+"&nbsp;&nbsp;");
        groupAndRound.setBold();
        groupAndRound.setFontSize(Text.FONT_SIZE_10_HTML_2);
        groupAndRound.setFontFace(Text.FONT_FACE_VERDANA);
        groupAndRound.setFontColor("FFFFFF");

      Table headerTable = new Table(2,1);
        headerTable.setWidth("100%");
        headerTable.setCellpadding(0);
        headerTable.setCellspacing(0);
        headerTable.setAlignment(2,1,"right");

        headerTable.add(tournamentText,1,1);
        headerTable.add(groupAndRound,2,1);

      myTable.add(headerTable,1,1);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void setMemberVectorPastRounds() throws SQLException{
      TournamentRound[] rounds = tournament.getTournamentRounds();
      TournamentRound tRound = new TournamentRound(tournamentRound_);
      Vector ids = new Vector();
      for (int i = 0; i < rounds.length; i++) {
        if (rounds[i].getRoundNumber() < tRound.getRoundNumber()) {
          ids.add(new Integer(rounds[i].getID()));
        }
      }

      if (ids.size() > 0) {
        pastTournamentRounds_ = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            pastTournamentRounds_[i] = ((Integer) ids.get(i)).intValue();
        }
      }
  }

  private void setMemberVectorAllRounds() throws SQLException{
      TournamentRound[] allRounds = tournament.getTournamentRounds();
      allTournamentRounds_ = new int[allRounds.length];
      for (int i = 0; i < allRounds.length; i++) {
          allTournamentRounds_[i] = allRounds[i].getID();
      }

  }

  private void getMemberVector() {
    try {
          int[] tournamentRounds_ = new int[1];
            tournamentRounds_[0] = tournamentRound_;


      ResultDataHandler handler = new ResultDataHandler(tournamentId_,ResultComparator.TOTALSTROKES,tournamentGroupId_,this.allTournamentRounds_ ,null);
      result = handler.getTournamentMembers();

    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private String getColor(int score, int par) {
    if ( score < par ) {
      return "FF0000";
    }
    else if ( score == par ) {
      return "000000";
    }
    else {
      return "0000FF";
    }
  }

  private String getBackgroundColor(int score, int par) {
    String color = null;
    int birdie = score - par;

    if ( birdie >= 2 ) {
      color = "#777D1A";
    }
    else if ( birdie == 1 ) {
      color = "#04463C";
    }
    else if ( birdie == -1 ) {
      color = "#BB2322";
    }
    else if ( birdie == -2 ) {
      color = "#2050A8";
    }

    return color;
  }

  private void sortMemberVector() {
    ResultComparator comparator = new ResultComparator(ResultComparator.TOTALSTROKES);
    Collections.sort(result,comparator);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}