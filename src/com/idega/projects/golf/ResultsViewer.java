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
import com.idega.data.*;
import com.idega.projects.golf.service.*;
import com.idega.projects.golf.entity.*;

public class ResultsViewer extends JModuleObject {

private int tournament_id = 0;
private int numberOfGolfers = 10;
private boolean showHeader = true;
private boolean groups = false;
private boolean gender = false;


  public ResultsViewer(int tournament_id,boolean groups) {
    this.tournament_id=tournament_id;
    this.groups=groups;
  }

  public ResultsViewer(int tournament_id,boolean groups,boolean gender) {
    this.tournament_id=tournament_id;
    this.groups=groups;
    this.gender=gender;
    if ( gender ) groups = false;
    if ( groups ) gender = false;
  }

  public ResultsViewer(int tournament_id) {
    this.tournament_id=tournament_id;
  }

  public void main(ModuleInfo modinfo) throws Exception {
    add(getResultsTable(modinfo));
  }

  private Table getResultsTable(ModuleInfo modinfo) throws Exception {

   Tournament tournament = new Tournament(tournament_id);
   TournamentGroup[] tournamentGroup = tournament.getTournamentGroups();
   String[] genderString = getGenderInTournament();

   Table myTable = new Table();
    myTable.setWidth("100%");

   Text tournamentName = new Text(tournament.getName());
    tournamentName.setBold();
    tournamentName.setFontSize(4);

   int a = 1;

   if ( showHeader ) {
      myTable.add(tournamentName,1,a);
      a++;
   }

   if ( groups ) {
    for ( int b = 0; b < tournamentGroup.length; b++ ) {
     Text groupName = new Text(tournamentGroup[b].getName());
      groupName.setFontSize(3);
      groupName.setBold();

     Table groupTable = new Table(1,2);
      groupTable.setWidth("100%");

     TournamentResults results = new TournamentResults(tournament_id,tournamentGroup[b].getID());
      results.setShowHeader(false);
      results.setNumberOfGolfers(numberOfGolfers);

     groupTable.add(groupName,1,1);
     groupTable.add(results,1,2);
     myTable.add(groupTable,1,a);
     a++;
    }
   }

   else if ( gender ) {
    for ( int b = 0; b < genderString.length; b++ ) {
      Text genderText = new Text();
        if ( genderString[b].equalsIgnoreCase("m") ) {
          genderText.setText("Karlar");
        }
        else if ( genderString[b].equalsIgnoreCase("f") ) {
          genderText.setText("Konur");
        }
        genderText.setFontSize(3);
        genderText.setBold();

       Table genderTable = new Table(1,2);
        genderTable.setWidth("100%");

       TournamentResults results = new TournamentResults(tournament_id,genderString[b]);
        results.setShowHeader(false);
        results.setNumberOfGolfers(numberOfGolfers);

       genderTable.add(genderText,1,1);
       genderTable.add(results,1,2);
       myTable.add(genderTable,1,a);
       a++;
    }
   }

   else {
     TournamentResults results = new TournamentResults(tournament_id);
      results.setShowHeader(false);
      results.setNumberOfGolfers(numberOfGolfers);

     myTable.add(results,1,a);
   }

   return myTable;

  }

  public void setNumberOfGolfers(int numberOfGolfers){
    this.numberOfGolfers=numberOfGolfers;
  }

  public void setShowHeader(boolean showHeader){
    this.showHeader=showHeader;
  }

  public void showAll(){
    this.numberOfGolfers=0;
  }

  public String[] getGenderInTournament() throws Exception {

    String queryString = "select distinct gender from tournament t,tournament_group tg,tournament_tournament_group ttg where t.tournament_id = ttg.tournament_id and ttg.tournament_group_id = tg.tournament_group_id and tournament_id = "+tournament_id;
    String[] query = SimpleQuerier.executeStringQuery(queryString);

    return query;

  }

}