package com.idega.projects.golf.moduleobject;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public class GolfTournamentAdminDialog extends ModuleObjectContainer{

private String header;
private Table myTable;

private String headerColor;
private String mainColor;

private String width = "100%";

  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";


      public GolfTournamentAdminDialog(){
            myTable = new Table(2,4);

            super.addBreak();
            super.add(myTable);
                myTable.setBorder(0);
                myTable.setCellpadding(0);
                myTable.setCellspacing(0);
                myTable.setWidth("90%");
                myTable.setHeight("90%");
                myTable.setColor("#99CC99");
                myTable.setColor(1,1,"#FFFFFF");
                myTable.setColor(2,1,"#FFFFFF");
                myTable.setColor(1,2,"#CEDFD0");
                myTable.setColor(1,3,"#CEDFD0");
                myTable.setColor(1,4,"#CEDFD0");
                myTable.setAlignment("center");
                myTable.setAlignment(1,1,"center");
                myTable.setAlignment(2,1,"right");
                myTable.setAlignment(1,3,"center");
                myTable.setVerticalAlignment(2,1,"bottom");
                myTable.setVerticalAlignment(1,3,"top");
                myTable.mergeCells(1,2,2,2);
                myTable.mergeCells(1,3,2,3);
                myTable.mergeCells(1,4,2,4);
                myTable.setHeight(3,"100%");
                myTable.setHeight(2,"17");

        }



        public void main(ModuleInfo modinfo) throws Exception{

            iwrb = getResourceBundle(modinfo);
            iwb = getBundle(modinfo);


            if (com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo) || com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo)) {
                String view = modinfo.getParameter("tournament_admin_view");

                String URI = modinfo.getRequestURI();

                Image mynd4 = iwrb.getImage("leftcorner.gif");
                Image iCreateTournament = iwrb.getImage("tabs/newtournament1.gif");
                Image iScorecard = iwrb.getImage("tabs/registerscorecard1.gif");
                Image iFinishTournament = iwrb.getImage("tabs/handicapupdate1.gif");
                Image iSetupStartingtime = iwrb.getImage("tabs/lineupteetimes1.gif");
                Image iRegisterMember = iwrb.getImage("tabs/registergolfer1.gif");
                Image iModifyTournament = iwrb.getImage("tabs/edittournament1.gif");
                Image iPrintouts = iwrb.getImage("tabs/printouts1.gif");

                if (view == null) {
    //                    iCreateTournamente.setSrc("tabs/ollmot.gif");
                      view = "";
                }
                else if ( view.equals("finishTournament") ) {
                    iFinishTournament = iwrb.getImage("tabs/handicapupdate.gif");
                }
                else if ( view.equals("createTournament") ) {
                    iCreateTournament = iwrb.getImage("tabs/newtournament.gif");
                }
                else if ( view.equals("tournamentScore") ) {
                    iScorecard = iwrb.getImage("tabs/registerscorecard.gif");
                }
                else if ( view.equals("setupStartingtime") ) {
                    iSetupStartingtime = iwrb.getImage("tabs/lineupteetimes.gif");
                }
                else if ( view.equals("registerMembers") ) {
                    iRegisterMember = iwrb.getImage("tabs/registergolfer.gif");
                }
                else if ( view.equals("modifyTournament") ) {
                    iModifyTournament = iwrb.getImage("tabs/edittournament.gif");
                }
                else if ( view.equals("outPrints") ) {
                    iPrintouts = iwrb.getImage("tabs/printouts.gif");
                }


                Link lPrintOuts = new Link(iPrintouts,"printing.jsp");
                  lPrintOuts.addParameter("tournament_admin_view","outPrints");

                Link lCreateTournament = new Link(iCreateTournament,"createtournament.jsp");
                  lCreateTournament.addParameter("tournament_admin_view","createTournament");
                  lCreateTournament.addParameter("tournament_control_mode","create");

                Link lFinish = new Link(iFinishTournament,"close_tournament.jsp");
                  lFinish.addParameter("tournament_admin_view","finishTournament");
                Link lScore = new Link(iScorecard,"scorecard_select.jsp");
                  lScore.addParameter("tournament_admin_view","tournamentScore");

                Link lSetupStartingtime = new Link(iSetupStartingtime,"setupstartingtime.jsp");
                  lSetupStartingtime.addParameter("tournament_admin_view","setupStartingtime");
                Link lRegisterMember = new Link(iRegisterMember,"registermember.jsp");
                  lRegisterMember.addParameter("tournament_admin_view","registerMembers");
                Link lModifyTournament = new Link(iModifyTournament,"modifytournament.jsp");
                  lModifyTournament.addParameter("tournament_admin_view","modifyTournament");
                  lCreateTournament.addParameter("tournament_control_mode","edit");


//                myTable.add(opinmot,2,1);


                myTable.add(lPrintOuts,2,1);
                myTable.add(lFinish,2,1);
                myTable.add(lScore,2,1);
                myTable.add(lSetupStartingtime,2,1);
                myTable.add(lRegisterMember,2,1);
                myTable.add(lModifyTournament,2,1);
                myTable.add(lCreateTournament,2,1);
                //myTable.add("Velkomin/n í mótastjórann",1,3);
            }

        }


	private void setDefaultValues(){
		//mainColor="#99CC99";
		//headerColor="#336666";

                //mainTable.setColumnColor(1,mainColor);
		//mainTable.setRowColor(1,headerColor);
	}

	public void setHeader(String header){
		this.header=header;
	}

	public String getHeader(){
		return header;
	}

	public void add(ModuleObject objectToAdd){
		myTable.add(objectToAdd,1,3);
	}

        public void addMessage(String message){
          add(message);
        }


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
