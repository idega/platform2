package com.idega.projects.golf.presentation;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.idegaweb.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.business.*;

public class TournamentBox extends Block{

private String header;
private String headerT;
private HeaderTable mainTable;
private Table innerTable;
private String headerColor;
private String mainColor;
private Image headerImage;
private boolean goneThrough_main=false;

private final String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
private IWBundle iwb;
private IWResourceBundle iwrb;

        public String getBundleIdentifier(){
            return IW_BUNDLE_IDENTIFIER;
        }

	public TournamentBox(){
		this("Mót");
	}

	public TournamentBox(String header){
              this.headerT = header;
              //initialize(header);
	}


	public TournamentBox(Image headerImage){
                this.headerImage=headerImage;
		//initialize(headerImage);
	}


        /**
         * Workaround so that this object doesn't call the main() function of HeaderTable
         */
        public void _main(IWContext iwc) throws Exception {
          iwb = getBundle(iwc);
          iwrb = iwb.getResourceBundle(iwc.getCurrentLocale());




          if (this.headerImage != null) {
              initialize(headerImage);
          }
          else {
              initialize(headerT);
          }

          if(!goneThrough_main){
              goneThrough_main=true;
              super._main(iwc);
          }

        }

        /*public void main(IWContext iwc){
          empty();
          if(headerImage==null){
            initialize(header);
          }
          else{
            initialize(headerImage);
          }
        }*/

        public void initialize(Object obj){
              mainTable = new HeaderTable();
              innerTable = new Table();
              add(innerTable);
              if(obj instanceof Image){

              }
              else if(obj instanceof String){
                mainTable.setHeaderText(obj.toString());
              }
                mainTable.setWidth(148);
		mainTable.setColor("#FFFFFF");
		mainTable.setBorderColor("#8ab490");
		mainTable.setRightHeader(false);
                mainTable.setHeadlineAlign("left");
		mainTable.setHeadlineSize(1);
                mainTable.setHeaderText(iwrb.getLocalizedString("tournament.tournaments","Tournaments") );
                super.add(mainTable);

                Tournament[] recent = null;
                Tournament[] coming = null;
                int row = 1;

                try {
                    recent = TournamentController.getLastClosedTournaments(5);
//                  recent = TournamentController.getLastTournaments(3);
                  coming = TournamentController.getTournamentToday();
//                  coming = TournamentController.getNextTournaments(3);
                }
                catch (Exception e) {
                }

                Text nextones=new Text(iwrb.getLocalizedString("tournament.tournaments_today","Tournaments today")+":");
                  nextones.setFontSize(1);
                  nextones.setBold();
                  nextones.setFontFace("Verdana,Arial,sans-serif");
                innerTable.add(nextones,1,row);

                if(coming!= null){
                  for (int i = 0; i < coming.length; i++) {
                    ++row;
                    Link link = new Link(coming[i].getName(),"/tournament/tournamentinfo.jsp");
                      link.setFontSize(1);
                      link.addParameter("tournament_id",coming[i].getID());
                    innerTable.add(link,1,row);
                  }
                  if ( coming.length == 0 ) {
                      ++row;
                     Text noCom = new Text(iwrb.getLocalizedString("tournament.no_tournaments_today","No tournaments today"));
                      noCom.setFontSize(1);
                      innerTable.add(noCom,1,row);
                  }

                }else{
                    ++row;
                   Text noCom = new Text(iwrb.getLocalizedString("tournament.no_tournaments_today","No tournaments today"));
                    noCom.setFontSize(1);
                    innerTable.add(noCom,1,row);
                }


                ++row;
                innerTable.addText("",1,row);
                innerTable.setHeight(row,"10");

                ++row;
                Text lastones=new Text(iwrb.getLocalizedString("tournament.latest_results","Latest results")+":");
                  lastones.setFontSize(1);
                  lastones.setBold();
                  lastones.setFontFace("Verdana,Arial,sans-serif");
                innerTable.add(lastones,1,row);
                if(recent!= null){
                  for (int i = 0; i < recent.length; i++) {
                    ++row;
                    Link link = new Link(recent[i].getName(),"/tournament/tournamentinfo.jsp");
                      link.setFontSize(1);
                      link.addParameter("tournament_id",recent[i].getID());
                      link.addParameter("action","viewFinalScore");
                    innerTable.add(link,1,row);
                  }
                  if ( recent.length == 0 ) {
                      ++row;
                     Text noLast = new Text(iwrb.getLocalizedString("tournament.no_tournaments_done","No tournaments done"));
                      noLast.setFontSize(1);
                      innerTable.add(noLast,1,row);
                  }
                }
                else {
                ++row;
                 Text noLast = new Text(iwrb.getLocalizedString("tournament.no_tournaments_done","No tournaments done"));
                  noLast.setFontSize(1);
                  innerTable.add(noLast,1,row);
                }

        }

	public void setHeader(String header){
		this.header=header;
	}

	public String getHeader(){
		return header;
	}

	public void add(PresentationObject objectToAdd){
		mainTable.add(objectToAdd);
	}
}
