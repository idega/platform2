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


  public ResultsViewer(int tournament_id) {
    this.tournament_id=tournament_id;
  }

  public void main(ModuleInfo modinfo) throws Exception {
    add(getResultsTable(modinfo));
  }

  private Table getResultsTable(ModuleInfo modinfo) throws Exception {

   String[] gender = getGenderInTournament();
   Tournament tournament = new Tournament(tournament_id);

   Table myTable = new Table();
    myTable.setWidth("100%");

   Text tournamentName = new Text(tournament.getName());
    tournamentName.setBold();
    tournamentName.setFontSize(4);

    if ( showHeader ) {
      myTable.add(tournamentName,1,1);
    }

   for ( int a = 0; a < gender.length; a++ ) {

    Table genderTable = new Table(1,2);
      genderTable.setWidth("100%");

    Text genderText = new Text();
      if ( gender[a].equalsIgnoreCase("m") ) {
        genderText.setText("Karlar:");
      }
      else if ( gender[a].equalsIgnoreCase("f") ) {
        genderText.setText("Konur:");
      }

      if ( gender.length == 1 ) {
        genderText.setText("Opin flokkur");
      }

      genderText.setBold();
      genderText.setFontSize(3);

    TournamentResults results = new TournamentResults(tournament_id,gender[a]);
      results.setShowHeader(false);
      results.setNumberOfGolfers(numberOfGolfers);

    genderTable.add(genderText,1,1);
    genderTable.add(results,1,2);

    myTable.add(genderTable,1,a+2);

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