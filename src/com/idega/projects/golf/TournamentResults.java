package com.idega.projects.golf;

/**
 * Title: TournamentResults
 * Description: Displayes the results of a tournament
 * Copyright:    Copyright (c) 2001
 * Company: idega co.
 * @author  Laddi
 * @version 1.3
 */

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.business.*;
import com.idega.data.*;
import com.idega.projects.golf.service.*;
import com.idega.projects.golf.entity.*;

public class TournamentResults extends JModuleObject {

private Tournament tournament;
private TournamentType type;
private TournamentRound[] tournamentRounds;
private int tournament_id = 0;
private String tournamentType;
private int numberOfGolfers = 10;
private Text tournamentName;
private Text typeName;
private Text positionText;
private Text memberText;
private Text unionText;
private Text strokesText;
private Text pointsText;
private Text bruttoText;
private Text nettoText;
private Text differenceText;
private String categoryText = "#000000";
private String headerText = "#000000";
private boolean showHeader = true;
private int tournament_group_id = 0;
private String groupSQLString = "";

  public TournamentResults(int tournament_id, int tournament_group_id) {
    this.tournament_id=tournament_id;
    this.tournament_group_id=tournament_group_id;
  }

  public TournamentResults(int tournament_id) {
    this.tournament_id=tournament_id;
  }

  public void main(ModuleInfo modinfo) throws SQLException {

    if ( tournament_group_id != 0 ) {
     groupSQLString = "and tournament_group_id = "+tournament_group_id+" ";
    }

    tournament = new Tournament(tournament_id);
    type = tournament.getTournamentType();
    tournamentRounds = (TournamentRound[]) (new TournamentRound()).findAllByColumnOrdered("tournament_id",tournament_id+"","round_number");
    tournamentType = type.getTournamentType();
    setTextValues();

    if ( tournamentType.equalsIgnoreCase("strokes") ) {
      getStrokesResult(modinfo);
    }

    if ( tournamentType.equalsIgnoreCase("points") ) {
      getPointsResult(modinfo);
    }

    if ( tournamentType.equalsIgnoreCase("holes") ) {
      getHolesResult(modinfo);
    }

    if ( tournamentType.equalsIgnoreCase("texas_scramble") ) {
      getTexasScrambleResult(modinfo);
    }

    if ( tournamentType.equalsIgnoreCase("greensome") ) {
      getGreensomeResult(modinfo);
    }

    if ( tournamentType.equalsIgnoreCase("groups") ) {
      getGroupsResult(modinfo);
    }

  }

  private void getStrokesResult(ModuleInfo modinfo) throws SQLException {

    boolean withHandicap = type.getWithHandicap();
    boolean withoutHandicap = type.getWithoutHandicap();

    Table strokesTable = new Table();
      strokesTable.setWidth("100%");
      strokesTable.setCellpadding(3);
      strokesTable.setCellspacing(1);

    Text withText = new Text("Með forgjöf");
      withText.setBold();
      withText.setFontColor(headerText);
    Text withoutText = new Text("Án forgjafar");
      withoutText.setBold();
      withoutText.setFontColor(headerText);

    if ( showHeader ) {
      strokesTable.add(tournamentName,1,1);
      strokesTable.add(typeName,1,1);
    }

    if ( withHandicap && !withoutHandicap ) {

      if ( showHeader ) {
        strokesTable.add(getWithHandicapTable(),1,3);
      }
      else {
        strokesTable.add(getWithHandicapTable(),1,1);
      }

    }

    if ( !withHandicap && withoutHandicap ) {

      if ( showHeader ) {
        strokesTable.add(getWithoutHandicapTable(),1,3);
      }
      else {
        strokesTable.add(getWithoutHandicapTable(),1,1);
      }

    }

    if ( withHandicap && withoutHandicap ) {

      if ( showHeader ) {
        strokesTable.add(withText,1,3);
        strokesTable.add(getWithHandicapTable(),1,4);
        strokesTable.add(withoutText,1,6);
        strokesTable.add(getWithoutHandicapTable(),1,7);
      }
      else {
        strokesTable.add(withText,1,1);
        strokesTable.add(getWithHandicapTable(),1,2);
        strokesTable.add(withoutText,1,4);
        strokesTable.add(getWithoutHandicapTable(),1,5);
      }

    }

    add(strokesTable);

  }

  private void getPointsResult(ModuleInfo modinfo) throws SQLException {

    Table strokesTable = new Table();
      strokesTable.setWidth("100%");
      strokesTable.setCellpadding(3);
      strokesTable.setCellspacing(1);

    Text withText = new Text("Með forgjöf");
      withText.setBold();
    Text withoutText = new Text("Án forgjafar");
      withoutText.setBold();

    if ( showHeader ) {
      strokesTable.add(tournamentName,1,1);
      strokesTable.add(typeName,1,1);
      strokesTable.add(getPointsTable(),1,3);
    }
    else {
      strokesTable.add(getPointsTable(),1,1);
    }

    add(strokesTable);
  }

  private void getHolesResult(ModuleInfo modinfo) {
    add("holes");
  }

  private void getTexasScrambleResult(ModuleInfo modinfo) {
    add("texas_scramble");
  }

  private void getGreensomeResult(ModuleInfo modinfo) {
    add("greensome");
  }

  private void getGroupsResult(ModuleInfo modinfo) {
    add("groups");
  }

  private Table getWithHandicapTable() throws SQLException {

      Table myTable = new Table();
        myTable.setWidth("100%");
        myTable.setCellpadding(3);
        myTable.setCellspacing(1);

      DisplayScores[] strokesScores = TournamentController.getDisplayScores("tournament_id = "+tournament_id+" "+groupSQLString,"strokes_with_handicap");

      if ( numberOfGolfers == 0 || numberOfGolfers > strokesScores.length ) {
        numberOfGolfers = strokesScores.length;
      }

      for ( int a = 0; a < numberOfGolfers; a++ ) {

        Text memberName = new Text(strokesScores[a].getName());

        Window scoreWindow = new Window("Skoryfirlit",650,650,"/tournament/handicap_skor.jsp");

        Link seeScores = new Link(memberName,scoreWindow);
                seeScores.addParameter("member_id",strokesScores[a].getMemberID());
                seeScores.addParameter("tournament_id",tournament_id);
                seeScores.addParameter("tournament_group_id",strokesScores[a].getTournamentGroupID());

        if ( a == 0 ) {
          myTable.add((a+1)+"",1,a+2);
        }

        else {
          if ( strokesScores[a].getStrokesWithHandicap() != strokesScores[a-1].getStrokesWithHandicap() ) {
            myTable.add((a+1)+"",1,a+2);
          }
        }

        int difference = strokesScores[a].getDifference();
        String out_differ = "E";
          if ( difference == 0 ) { out_differ = "E"; }
          else if ( difference < 0 ) { out_differ = String.valueOf(difference); }
          else if ( difference > 0 ) { out_differ = "+"+String.valueOf(difference); }

        myTable.add(seeScores,2,a+2);
        myTable.add(strokesScores[a].getAbbrevation(),3,a+2);
        myTable.add(strokesScores[a].getStrokesWithoutHandicap()+"",4,a+2);
        myTable.add(strokesScores[a].getStrokesWithHandicap()+"",5,a+2);
      }

      myTable.add(positionText,1,1);
      myTable.add(memberText,2,1);
      myTable.add(unionText,3,1);
      myTable.add(bruttoText,4,1);
      myTable.add(nettoText,5,1);

      for ( int c = 1; c <= myTable.getColumns(); c++ ) {
        myTable.setColumnAlignment(c,"center");
      }

      myTable.setColumnAlignment(2,"left");

      return myTable;
  }

  private Table getWithoutHandicapTable() throws SQLException {

      Table myTable = new Table();
        myTable.setWidth("100%");
        myTable.setCellpadding(3);
        myTable.setCellspacing(1);

      DisplayScores[] strokesScores = TournamentController.getDisplayScores("tournament_id = "+tournament_id+" "+groupSQLString,"difference");

      if ( numberOfGolfers == 0 || numberOfGolfers > strokesScores.length ) {
        numberOfGolfers = strokesScores.length;
      }

      for ( int a = 0; a < numberOfGolfers; a++ ) {

        Text memberName = new Text(strokesScores[a].getName());

        Window scoreWindow = new Window("Skoryfirlit",650,650,"/tournament/handicap_skor.jsp");

        Link seeScores = new Link(memberName,scoreWindow);
                seeScores.addParameter("member_id",strokesScores[a].getMemberID());
                seeScores.addParameter("tournament_id",tournament_id);
                seeScores.addParameter("tournament_group_id",strokesScores[a].getTournamentGroupID());

        if ( a == 0 ) {
          myTable.add((a+1)+"",1,a+2);
        }

        else {
          if ( strokesScores[a].getStrokesWithoutHandicap() != strokesScores[a-1].getStrokesWithoutHandicap() ) {
            myTable.add((a+1)+"",1,a+2);
          }
        }

        int difference = strokesScores[a].getDifference();
        String out_differ = "E";
          if ( difference == 0 ) { out_differ = "E"; }
          else if ( difference < 0 ) { out_differ = String.valueOf(difference); }
          else if ( difference > 0 ) { out_differ = "+"+String.valueOf(difference); }

        myTable.add(seeScores,2,a+2);
        myTable.add(strokesScores[a].getAbbrevation(),3,a+2);
        myTable.add(strokesScores[a].getStrokesWithoutHandicap()+"",4,a+2);
        myTable.add(out_differ,5,a+2);
      }

      myTable.add(positionText,1,1);
      myTable.add(memberText,2,1);
      myTable.add(unionText,3,1);
      myTable.add(strokesText,4,1);
      myTable.add(differenceText,5,1);

      for ( int c = 1; c <= myTable.getColumns(); c++ ) {
        myTable.setColumnAlignment(c,"center");
      }

      myTable.setColumnAlignment(2,"left");

      return myTable;

  }

  private Table getPointsTable() throws SQLException {

      Table myTable = new Table();
        myTable.setWidth("100%");
        myTable.setCellpadding(3);
        myTable.setCellspacing(1);

      DisplayScores[] strokesScores = TournamentController.getDisplayScores("tournament_id = "+tournament_id+" "+groupSQLString,"total_points");

      if ( numberOfGolfers == 0 || numberOfGolfers > strokesScores.length ) {
        numberOfGolfers = strokesScores.length;
      }

      for ( int a = 0; a < numberOfGolfers; a++ ) {

        Text memberName = new Text(strokesScores[a].getName());

        Window scoreWindow = new Window("Skoryfirlit",650,650,"/tournament/handicap_skor.jsp");

        Link seeScores = new Link(memberName,scoreWindow);
                seeScores.addParameter("member_id",strokesScores[a].getMemberID());
                seeScores.addParameter("tournament_id",tournament_id);
                seeScores.addParameter("tournament_group_id",strokesScores[a].getTournamentGroupID());

        if ( a == 0 ) {
          myTable.add((a+1)+"",1,a+2);
        }

        else {
          if ( strokesScores[a].getTotalPoints() != strokesScores[a-1].getTotalPoints() ) {
            myTable.add((a+1)+"",1,a+2);
          }
        }

        int difference = strokesScores[a].getDifference();
        String out_differ = "E";
          if ( difference == 0 ) { out_differ = "E"; }
          else if ( difference < 0 ) { out_differ = String.valueOf(difference); }
          else if ( difference > 0 ) { out_differ = "+"+String.valueOf(difference); }

        myTable.add(seeScores,2,a+2);
        myTable.add(strokesScores[a].getAbbrevation(),3,a+2);
        myTable.add(strokesScores[a].getTotalPoints()+"",4,a+2);
      }

      myTable.add(positionText,1,1);
      myTable.add(memberText,2,1);
      myTable.add(unionText,3,1);
      myTable.add(pointsText,4,1);

      for ( int c = 1; c <= myTable.getColumns(); c++ ) {
        myTable.setColumnAlignment(c,"center");
      }

      myTable.setColumnAlignment(2,"left");

      return myTable;

  }

  private void setTextValues() {

    tournamentName = new Text(tournament.getName());
      tournamentName.setFontSize(3);
      tournamentName.setBold();
      tournamentName.setFontColor(headerText);

    typeName = new Text(" - "+type.getName());
      typeName.setFontColor(headerText);

    positionText = new Text("Sæti");
      positionText.setFontSize(1);
      positionText.setBold();
      positionText.setFontColor(categoryText);

    memberText = new Text("Kylfingur");
      memberText.setFontSize(1);
      memberText.setBold();
      memberText.setFontColor(categoryText);

    unionText = new Text("Klúbbur");
      unionText.setFontSize(1);
      unionText.setBold();
      unionText.setFontColor(categoryText);

    strokesText = new Text("Högg");
      strokesText.setFontSize(1);
      strokesText.setBold();
      strokesText.setFontColor(categoryText);

    pointsText = new Text("Punktar");
      pointsText.setFontSize(1);
      pointsText.setBold();
      pointsText.setFontColor(categoryText);

    bruttoText = new Text("Brúttó");
      bruttoText.setFontSize(1);
      bruttoText.setBold();
      bruttoText.setFontColor(categoryText);

    nettoText = new Text("Nettó");
      nettoText.setFontSize(1);
      nettoText.setBold();
      nettoText.setFontColor(categoryText);

    differenceText = new Text("Par");
      differenceText.setFontSize(1);
      differenceText.setBold();
      differenceText.setFontColor(categoryText);

  }

  public void setNumberOfGolfers(int numberOfGolfers){
    this.numberOfGolfers=numberOfGolfers;
  }

  public void showAll(){
    this.numberOfGolfers=0;
  }

  public void setShowHeader(boolean showHeader){
    this.showHeader=showHeader;
  }

  public void setCategoryText(String categoryText){
    this.categoryText=categoryText;
  }

  public void setHeaderText(String headerText){
    this.headerText=headerText;
  }

}