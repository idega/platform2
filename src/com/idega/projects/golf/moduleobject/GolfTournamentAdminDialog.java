package com.idega.projects.golf.moduleobject;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;


public class GolfTournamentAdminDialog extends ModuleObjectContainer{

private String header;
private Table myTable;

private String headerColor;
private String mainColor;

private String width = "100%";


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
            if (com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo) || com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo)) {
                String view = modinfo.getParameter("tournament_admin_view");

                String URI = modinfo.getRequestURI();

                Image mynd4 = new Image("/pics/jmodules/poll/leftcorner.gif");
                Image iCreateTournament = new Image("/pics/flipar_takkar/stofnamot1.gif");
                Image iScorecard = new Image("/pics/flipar_takkar/skraskorkort1.gif");
                Image iFinishTournament = new Image("/pics/flipar_takkar/gerauppmot1.gif");
                Image iSetupStartingtime = new Image("/pics/flipar_takkar/stillaupp1.gif");
                Image iRegisterMember = new Image("/pics/flipar_takkar/skrakylfing1.gif");
                Image iModifyTournament = new Image("/pics/flipar_takkar/breytamoti1.gif");
                Image iPrintouts = new Image("/pics/flipar_takkar/utprentanir1.gif");

                if (view == null) {
    //                    iCreateTournamente.setSrc("/pics/flipar_takkar/ollmot.gif");
                      view = "";
                }
                else if ( view.equals("finishTournament") ) {
                    iFinishTournament.setSrc("/pics/flipar_takkar/gerauppmot.gif");
                }
                else if ( view.equals("createTournament") ) {
                    iCreateTournament.setSrc("/pics/flipar_takkar/stofnamot.gif");
                }
                else if ( view.equals("tournamentScore") ) {
                    iScorecard.setSrc("/pics/flipar_takkar/skraskorkort.gif");
                }
                else if ( view.equals("femaleTournaments") ) {
                    iSetupStartingtime.setSrc("/pics/flipar_takkar/stillaupp.gif");
                }
                else if ( view.equals("registerMembers") ) {
                    iRegisterMember.setSrc("/pics/flipar_takkar/skrakylfing.gif");
                }
                else if ( view.equals("modifyTournament") ) {
                    iModifyTournament.setSrc("/pics/flipar_takkar/breytamoti.gif");
                }
                else if ( view.equals("outPrints") ) {
                    iPrintouts.setSrc("/pics/flipar_takkar/utprentanir.gif");
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

}
