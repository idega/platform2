// idega - Gimmi & Eiki
package com.idega.projects.rafteikning.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;

public abstract class MainTemplate extends JSPModule implements JspPage{

   public Table mainTable;
    public Table headerTable;
    public Table headerStripe;

private Link linkCommunications ;
private Link linkCalendar ;
private Link linkParticipants ;
private Link linkHandbook ;
private Link linkTimesheets ;
private Link linkNews;
private Link linkAccident;



	public void initializePage(){
          super.initializePage();
                //setPage(new Window());
		Page jmodule = getPage();
		jmodule.setMarginHeight(0);
		jmodule.setMarginWidth(0);
		jmodule.setLeftMargin(0);
		jmodule.setTopMargin(0);
		jmodule.setAlinkColor("black");
		jmodule.setVlinkColor("black");
		jmodule.setLinkColor("black");
		jmodule.setHoverColor("#4D6476");
                setPage(jmodule);
                init();
      jmodule.add(getHeaderTable());
      jmodule.add(getHeaderStripe());
      jmodule.add(getMainTable());
      this.locateObjects();

	}


    public Table getMainTable(){
      this.initMainTable();
      return mainTable;
    }

    public Table getHeaderTable(){
      this.initHeaderTable();
      return headerTable;
    }

    public Table getHeaderStripe(){
      this.initHeaderStripe();
      return headerStripe;
    }

    public void initMainTable(){
//      mainTable.setBorder(1);
      mainTable.setCellpadding(0);
      mainTable.setCellspacing(0);
      mainTable.setHeight(0);
      mainTable.setWidth("100%");
      mainTable.setWidth(1,1,"0");
      mainTable.setWidth(2,1,"100%");
      mainTable.setWidth(3,1,"177");

      mainTable.setAlignment(1,1,"left");
      mainTable.setAlignment(2,1,"left");
      mainTable.setAlignment(3,1,"right");
      mainTable.setVerticalAlignment(1,1,"top");
      mainTable.setVerticalAlignment(2,1,"top");
      mainTable.setVerticalAlignment(3,1,"top");
    }

    public void initHeaderTable(){
//      headerTable.setBorder(1);
      headerTable.setCellpadding(0);
      headerTable.setCellspacing(0);
      headerTable.setHeight(0);
      headerTable.setWidth("100%");
      headerTable.setWidth(1,1,"0");
      headerTable.setWidth(2,1,"100%");
      headerTable.setWidth(3,1,"0");
    }

    public void initHeaderStripe(){
//      headerStripe.setBorder(1);
      headerStripe.setCellpadding(0);
      headerStripe.setCellspacing(0);
      headerStripe.setHeight(0);
      headerStripe.setWidth("100%");
      headerStripe.setWidth(1,1,"0");
      headerStripe.setWidth(2,1,"100%");
      headerStripe.setWidth(3,1,"0");

      headerStripe.setAlignment("center");
    }


    public void init(){
      mainTable = new Table(3,1);
      headerTable = new Table(3,1);
      headerStripe = new Table(3,1);
    }



public void locateObjects() {



  String language = "IS";

    getModuleInfo().setSpokenLanguage( language );

    String commImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/samskipti.gif" : "/pics/Slysaskraning/enska/comm.gif";
    String calendarImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/dagatal.gif" : "/pics/Slysaskraning/enska/calendar.gif";
    String participantsImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/endur.gif" : "/pics/Slysaskraning/enska/participants.gif";
    String newsImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/forsida.gif" : "/pics/Slysaskraning/enska/home.gif";
    String handbookImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/handbok.gif" : "/pics/Slysaskraning/enska/handbook.gif";
    String timesheetsImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/verkbok.gif" : "/pics/Slysaskraning/enska/timesheets.gif";
    String accidentImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/slysavarnarad.gif" : "/pics/Slysaskraning/islenska/slysavarnarad.gif";

    String topImage = (language.equalsIgnoreCase("IS")) ? "/pics/Slysaskraning/islenska/topleft.gif" : "/pics/Slysaskraning/enska/topleft.gif";

        linkCommunications = new Link(new Image(commImage,"communications"),"/communications.jsp");
	linkCalendar = new Link(new Image(calendarImage,"calendar"),"/calendar.jsp");
	linkParticipants = new Link(new Image(participantsImage,"participants"),"/participants.jsp");
	linkHandbook = new Link(new Image(handbookImage,"handbook"),"/boxoffice/index.jsp");
        linkHandbook.addParameter("issue_id","1");
        linkTimesheets = new Link(new Image(timesheetsImage,"timesheets"),"/timesheets.jsp");
        linkAccident = new Link(new Image(accidentImage,"commity"),"/boxoffice/index2.jsp");
	linkAccident.addParameter("issue_id","2");
	linkNews = new Link(new Image(newsImage,"news"),"/index.jsp");

	Image top = new Image(topImage);
        //Image top = new Image("/pics/Slysaskraning/islenska/topmiddle.gif");
        Image topMiddle = new Image("/pics/Slysaskraning/islenska/topmiddle.gif");
        Image bottomMiddle = new Image("/pics/Slysaskraning/islenska/middlebottom.gif");
        Image topRight = new Image("/pics/Slysaskraning/islenska/topright.gif");
        Image bottomRight = new Image("/pics/Slysaskraning/islenska/kulurright.gif","kulur",177,18);

      headerTable.add(top,1,1);
      headerTable.setBackgroundImage(2,1,topMiddle);
      headerTable.add(topRight,3,1);
/*
      headerStripe.setBackgroundImage(1,1,bottomMiddle);
      headerStripe.setBackgroundImage(2,1,bottomMiddle);
      headerStripe.setBackgroundImage(3,1,bottomMiddle);
*/
        headerStripe.setColor("#394650");
//        headerStripe.setColor("#4D6476");
      Table linkTable = new Table(6,1);
      linkTable.setVerticalAlignment("middle");
      linkTable.setCellpadding(0);
      linkTable.setCellspacing(0);

      linkTable.add(linkCommunications,1,1);
      linkTable.add(linkCalendar,2,1);
      linkTable.add(linkParticipants,3,1);
      linkTable.add(linkHandbook,4,1);
      linkTable.add(linkTimesheets,5,1);
      linkTable.add(linkNews,6,1);

      headerStripe.add(linkTable,1,1);




 	}


	public void addToLink(String parameterName, String parameterValue) {

			linkCommunications.addParameter(parameterName,parameterValue);

			linkCalendar.addParameter(parameterName,parameterValue);

			linkParticipants.addParameter(parameterName,parameterValue);

			linkHandbook.addParameter(parameterName,parameterValue);

			linkTimesheets.addParameter(parameterName,parameterValue);

                        linkNews.addParameter(parameterName,parameterValue);
	}

        public boolean isAdmin() {
		if (getSession().getAttribute("member_access") != null) {
			if (getSession().getAttribute("member_access").equals("admin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}



	public void setLeftAlignment(String align){
		mainTable.setAlignment(1,1,align);
	}

        public void setCenterAlignment(String align){
          mainTable.setAlignment(2,1,align);
        }



    public void add(ModuleObject objectToAdd){
      mainTable.add(Text.getBreak(),2,1);
      mainTable.add(objectToAdd,2,1);
    }

    public void addLeft(ModuleObject objectToAdd){
      mainTable.add(objectToAdd,1,1);
    }

    public void addRight(ModuleObject objectToAdd){
      mainTable.add(objectToAdd,3,1);
    }


}
