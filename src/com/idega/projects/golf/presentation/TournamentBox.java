package com.idega.projects.golf.presentation;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.business.*;

public class TournamentBox extends ModuleObjectContainer{

private String header;
private HeaderTable mainTable;
private Table innerTable;
private String headerColor;
private String mainColor;


	public TournamentBox(){
		this("Mót");
	}

	public TournamentBox(String header){
              initialize(header);
	}


	public TournamentBox(Image headerImage){
		initialize(headerImage);


	}

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
                mainTable.setHeaderText("Mót");
                super.add(mainTable);

                Tournament[] recent = null;
                Tournament[] coming = null;

                try {
                  recent = TournamentController.getLastTournaments(3);
                  coming = TournamentController.getNextTournaments(3);
                }
                catch (Exception e) {
                }

                Text nextones=new Text("Næstu mót:");
                  nextones.setFontSize(1);
                  nextones.setBold();
                  nextones.setFontFace("Verdana,Arial,sans-serif");
                innerTable.add(nextones,1,1);

                if(coming!= null){
                  for (int i = 0; i < coming.length; i++) {
                    Link link = new Link(coming[i].getName(),"/tournament/tournamentinfo.jsp");
                      link.setFontSize(1);
                      link.addParameter("tournament_id",coming[i].getID());
                    innerTable.add(link,1,i+2);
                  }

                }
                Text lastones=new Text("Síðustu mót:");
                  lastones.setFontSize(1);
                  lastones.setBold();
                  lastones.setFontFace("Verdana,Arial,sans-serif");
                innerTable.add(lastones,1,6);
                innerTable.addText("",1,5);
                innerTable.setHeight(5,"10");
                if(recent!= null){
                  for (int i = 0; i < recent.length; i++) {
                    Link link = new Link(recent[i].getName(),"/tournament/tournamentinfo.jsp");
                      link.setFontSize(1);
                      link.addParameter("tournament_id",recent[i].getID());
                    innerTable.add(link,1,i+7);
                  }
                  if ( recent.length == 0 ) {
                     Text noLast = new Text("Engin mót búin");
                      noLast.setFontSize(1);
                      innerTable.add(noLast,1,7);
                  }
                }
                else {
                 Text noLast = new Text("Engin mót búin");
                  noLast.setFontSize(1);
                  innerTable.add(noLast,1,7);
                }

        }

	public void setHeader(String header){
		this.header=header;
	}

	public String getHeader(){
		return header;
	}

	public void add(ModuleObject objectToAdd){
		mainTable.add(objectToAdd);
	}
}
