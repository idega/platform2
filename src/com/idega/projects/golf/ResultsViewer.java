package com.idega.projects.golf;

/**
 * Title: ResultsViewer
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
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class ResultsViewer extends JModuleObject {

private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
protected IWResourceBundle iwrb;
protected IWBundle iwb;

private int tournamentID = 0;
private int tournamentGroupID = -1;
private int[] tournamentRounds = null;
private String gender = null;
private int orderBy = -1;
private int sortBy = -1;
private boolean showAllGroups = false;
private boolean showAllGenders = false;
private boolean championship = false;

private Tournament tournament;
private Form myForm;
private Table outerTable;
private Table formTable;
private Table resultTable;

  public ResultsViewer(int tournamentId_) {
    tournamentID=tournamentId_;
    myForm = new Form();
  }

  public void main(ModuleInfo modinfo) {
    try {
      iwrb = getResourceBundle(modinfo);
      tournament = new Tournament(tournamentID);
      add(getResult(modinfo));
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private Form getResult(ModuleInfo modinfo) {
    try {
      getFormValues(modinfo);
      getOuterTable();

      myForm.add(outerTable);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return myForm;
  }

  private void getFormValues(ModuleInfo modinfo) {
    String tournamentGroupId_ = modinfo.getParameter("tournament_group_id");
      if ( tournamentGroupId_ != null ) {
        if ( tournamentGroupId_.length() > 0 ) {
          int tournamentGroupId = Integer.parseInt(tournamentGroupId_);
          if ( tournamentGroupId > 0 ) {
            tournamentGroupID = Integer.parseInt(tournamentGroupId_);
          }
          else {
            tournamentGroupID = -1;
            showAllGroups = true;
          }
        }
      }
      else {
        showAllGroups = true;
      }

    String tournamentRounds_ = modinfo.getParameter("tournament_round_id");
      if ( tournamentRounds_ != null ) {
        if ( tournamentRounds_.length() > 0 ) {
          if ( tournamentRounds_ != "0" ) {
            String[] query = null;

            try {
              TournamentRound round = new TournamentRound(Integer.parseInt(tournamentRounds_));
              String queryString = "select tournament_round_id from tournament_round where tournament_id = "+Integer.toString(tournamentID)+" and round_number <= "+Integer.toString(round.getRoundNumber())+" order by round_number";
              query = SimpleQuerier.executeStringQuery(queryString);
            }
            catch (Exception e) {
              e.printStackTrace(System.err);
            }

            if ( query != null ) {
              tournamentRounds = new int[query.length];
              for ( int a = 0; a < query.length; a++ ) {
                tournamentRounds[a] = Integer.parseInt(query[a]);
              }
            }
          }
        }
      }

    String gender_ = modinfo.getParameter("gender");
      if ( gender_ != null ) {
        if ( gender_.equalsIgnoreCase("f") || gender_.equalsIgnoreCase("m") ) {
          gender = gender_.toUpperCase();
        }
        if ( gender_.equalsIgnoreCase("b") && !showAllGroups ) {
          showAllGenders = true;
        }
      }

    String sort_ = modinfo.getParameter("sort");
      if ( sort_ != null ) {
        if ( sort_.length() > 0 ) {
          sortBy = Integer.parseInt(sort_);
        }
        else {
          sortBy = this.getTournamentType();
        }
      }
      else {
        sortBy = this.getTournamentType();
      }

    String order_ = modinfo.getParameter("order");
      if ( order_ != null ) {
        if ( order_.length() > 0 ) {
          if ( order_.equalsIgnoreCase("0") ) {
            orderBy = sortBy;
          }
          else {
            orderBy = Integer.parseInt(order_);
          }
        }
        else {
          orderBy = sortBy;
        }
      }
      else {
        orderBy = sortBy;
      }
  }

  private void getOuterTable() {
    outerTable = new Table(1,2);
      outerTable.setWidth("100%");
      outerTable.setCellpadding(3);
      outerTable.setCellspacing(3);
      outerTable.setHeight("100%");
      outerTable.setHeight(2,"100%");
      outerTable.setVerticalAlignment(1,2,"top");

    getFormTable();
    getResultsTable();

    outerTable.add(formTable,1,1);
    outerTable.add(resultTable,1,2);
  }

  private void getFormTable() {
    formTable = new Table(6,1);
      formTable.setAlignment("right");
      formTable.setCellpadding(0);
      formTable.setCellspacing(1);

    DropdownMenu genderMenu = new DropdownMenu("gender");
      genderMenu.setAttribute("style",getStyle());
      genderMenu.addMenuElement("","- "+iwrb.getLocalizedString("tournament.genders","Genders")+" -");
      genderMenu.addMenuElement("M",iwrb.getLocalizedString("tournament.males","Male"));
      genderMenu.addMenuElement("F",iwrb.getLocalizedString("tournament.females","Female"));
      genderMenu.addMenuElement("B",iwrb.getLocalizedString("tournament.both","Both"));
      genderMenu.keepStatusOnAction();

    if ( tournament.getNumberOfRounds() > 4 ) {
      championship = true;
    }

    String all = "";
    String allGroups = "0";

    if ( championship ) {
      all = "0";
      allGroups = "";
    }

    DropdownMenu groupsMenu = new DropdownMenu("tournament_group_id");
      groupsMenu.setAttribute("style",getStyle());
      groupsMenu.addMenuElement(all,"- "+iwrb.getLocalizedString("tournament.groups","Groups")+" -");

      TournamentGroup[] groups = null;
      try {
        groups = tournament.getTournamentGroups();
        for ( int a = 0; a < groups.length; a++ ) {
          groupsMenu.addMenuElement(groups[a].getID(),groups[a].getName());
        }
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      groupsMenu.addMenuElement(allGroups,iwrb.getLocalizedString("tournament.all","All"));
      groupsMenu.keepStatusOnAction();

    String roundShort = iwrb.getLocalizedString("tournament.round","Round");
    if ( tournament.getNumberOfRounds() > 4 ) {
      roundShort = iwrb.getLocalizedString("tournament.day","Day");
    }

    String round = iwrb.getLocalizedString("tournament.rounds","Rounds");
    if ( tournament.getNumberOfRounds() > 4 ) {
      round = iwrb.getLocalizedString("tournament.days","Days");
    }

    DropdownMenu roundsMenu = new DropdownMenu("tournament_round_id");
      roundsMenu.setAttribute("style",getStyle());
      roundsMenu.addMenuElement("","- "+round+" -");

      TournamentRound[] rounds = null;
      try {
        rounds = tournament.getTournamentRounds();
        for ( int a = 0; a < rounds.length; a++ ) {
          roundsMenu.addMenuElement(rounds[a].getID(),Integer.toString(a+1)+". "+roundShort);
        }
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      roundsMenu.addMenuElement("0",iwrb.getLocalizedString("tournament.all","All"));
      roundsMenu.keepStatusOnAction();

    DropdownMenu scoreMenu = new DropdownMenu("sort");
      scoreMenu.setAttribute("style",getStyle());
      scoreMenu.addMenuElement("","- "+iwrb.getLocalizedString("tournament.score","Score")+" -");
      scoreMenu.addMenuElement(ResultComparator.TOTALSTROKES,iwrb.getLocalizedString("tournament.strokes_without_handicap","Strokes"));
      scoreMenu.addMenuElement(ResultComparator.TOTALSTROKESWITHHANDICAP,iwrb.getLocalizedString("tournament.strokes_with_handicap","Strokes w/handicap"));
      scoreMenu.addMenuElement(ResultComparator.TOTALPOINTS,iwrb.getLocalizedString("tournament.points","Points"));
      scoreMenu.keepStatusOnAction();

    DropdownMenu orderMenu = new DropdownMenu("order");
      orderMenu.setAttribute("style",getStyle());
      orderMenu.addMenuElement("","- "+iwrb.getLocalizedString("tournament.order","Order")+" -");
      orderMenu.addMenuElement(0,iwrb.getLocalizedString("tournament.by_score","By score"));
      orderMenu.addMenuElement(ResultComparator.NAME,iwrb.getLocalizedString("tournament.by_name","By name"));
      orderMenu.addMenuElement(ResultComparator.ABBREVATION,iwrb.getLocalizedString("tournament.by_club","By club"));
      orderMenu.keepStatusOnAction();

    SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/get.gif","",76,19));

    formTable.add(genderMenu,1,1);
    formTable.add(groupsMenu,2,1);
    formTable.add(roundsMenu,3,1);
    formTable.add(scoreMenu,4,1);
    formTable.add(orderMenu,5,1);
    formTable.add(submit,6,1);

  }

  private String getStyle() {
    String style = "font-family: Verdana; font-size: 8pt; border: 1 solid #000000";
    return style;
  }

  private void getResultsTable() {
    try {
      resultTable = new Table(1,1);
        resultTable.setBorder(0);
        resultTable.setCellpadding(0);
        resultTable.setCellspacing(0);
        resultTable.setWidth("100%");
        resultTable.setHeight("100%");
        resultTable.setVerticalAlignment(1,1,"top");

      if ( showAllGroups || showAllGenders ) {
        if ( showAllGroups ) {
          String genderString = "";
          if ( gender != null ) {
            genderString = "and gender = '"+gender.toUpperCase()+"'";
          }

          TournamentGroup[] groups = (TournamentGroup[]) TournamentGroup.getStaticInstance("com.idega.projects.golf.entity.TournamentGroup").findAll("select tournament_group.* from tournament_group tg, tournament_tournament_group ttg where tg.tournament_group_id = ttg.tournament_group_id "+genderString+" and tournament_id = "+Integer.toString(tournamentID)+" order by handicap_max");

          Table groupResultsTable = new Table();
            groupResultsTable.setWidth("100%");

          int row = 1;

          for ( int a = 0; a < groups.length; a++ ) {
            Text groupName = new Text(groups[a].getName());
              groupName.setFontSize(3);
              groupName.setBold();

            TournamentResults tournResults = new TournamentResults(tournamentID,sortBy,groups[a].getID(),tournamentRounds,gender);
                tournResults.sortBy(orderBy);

            groupResultsTable.add(groupName,1,row);
            groupResultsTable.add(tournResults,1,row+1);
            row += 3;
          }
          resultTable.add(groupResultsTable,1,1);
        }

        if ( showAllGenders ) {
          String[] genders = getGenderInTournament();

          Table groupResultsTable = new Table();
            groupResultsTable.setWidth("100%");

          int row = 1;

          for ( int a = 0; a < genders.length; a++ ) {
            Text genderName = new Text();
              genderName.setFontSize(3);
              genderName.setBold();

            if ( genders[a].equalsIgnoreCase("m") )
              genderName.setText(iwrb.getLocalizedString("tournament.men","Men"));
            else genderName.setText(iwrb.getLocalizedString("tournament.women","Women"));

            TournamentResults tournResults = new TournamentResults(tournamentID,sortBy,tournamentGroupID,tournamentRounds,genders[a]);
                tournResults.sortBy(orderBy);

            groupResultsTable.add(genderName,1,row);
            groupResultsTable.add(tournResults,1,row+1);
            row += 3;
          }
          resultTable.add(groupResultsTable,1,1);
       }
      }
      else {
        TournamentResults tournResults = new TournamentResults(tournamentID,sortBy,tournamentGroupID,tournamentRounds,gender);
            tournResults.sortBy(orderBy);

        resultTable.add(tournResults,1,1);
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private String[] getGenderInTournament() {
    String[] query = null;
    try {
      String queryString = "select distinct gender from tournament t,tournament_group tg,tournament_tournament_group ttg where t.tournament_id = ttg.tournament_id and ttg.tournament_group_id = tg.tournament_group_id and tournament_id = "+Integer.toString(tournamentID);
      query = SimpleQuerier.executeStringQuery(queryString);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return query;
  }

  private int getTournamentType() {
    int tournamentType = ResultComparator.TOTALSTROKES;
    try {
      TournamentType type = tournament.getTournamentType();
      String typeName = type.getTournamentType();

      if ( typeName.equalsIgnoreCase("points") )
        tournamentType = ResultComparator.TOTALPOINTS;
      if ( orderBy == ResultComparator.TOTALPOINTS )
        tournamentType = ResultComparator.TOTALPOINTS;
      if ( orderBy == ResultComparator.TOTALSTROKES )
        tournamentType = ResultComparator.TOTALSTROKES;
      if ( orderBy == ResultComparator.TOTALSTROKESWITHHANDICAP )
        tournamentType = ResultComparator.TOTALSTROKESWITHHANDICAP;
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return tournamentType;
  }

  public void addHiddenInput(String name, String value) {
    myForm.add(new HiddenInput(name,value));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}