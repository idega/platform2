// idega - Gimmi & Eiki
package com.idega.projects.development.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.data.*;

public abstract class MainTemplate extends JSPModule implements JspPage{

public Table tafla;
public Table frame;

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
		jmodule.setHoverColor("#f29b00");
/*
                Script script = new Script("JavaScript");
                    script.setScriptSource("/js/gimmi.js");
                    jmodule.setAssociatedScript(script);
                setPage(jmodule);
*/
		jmodule.add(template());

	}


public Table template() {
         frame = new Table(1,1);
          frame.setWidth("100%");
          frame.setCellpadding(0);
          frame.setCellspacing(0);
          tafla = new Table(3,1);
          Table header = new Table(3,2);
          header.mergeCells(1,2,2,2);

	  header.setHeight(1,"50");
          header.setWidth(1,1,"523");
	  header.setHeight(2,"18");
          header.setCellpadding(0);
	  header.setCellspacing(0);
          header.setWidth("100%");

        tafla.setWidth("100%");
        tafla.setHeight("100%");
	tafla.setHeight(1,1,"68");
	tafla.setCellpadding(0);
	tafla.setCellspacing(0);
	tafla.setAlignment(1,1,"left");
        tafla.setAlignment(2,1,"left");
        tafla.setAlignment(3,1,"center");
        tafla.setWidth(1,1,"170");
        tafla.setWidth(3,1,"170");

     tafla.setAlignment("top");
     tafla.setVerticalAlignment("top");
     frame.setRowVerticalAlignment(1,"top");

        frame.setVerticalAlignment(1,1,"top");
	tafla.setVerticalAlignment(1,1,"top");
        tafla.setVerticalAlignment(2,1,"top");
        tafla.setVerticalAlignment(3,1,"top");



  String language = "IS";

    getIWContext().setSpokenLanguage( language );

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

                header.setWidth(1,1,"354");
                header.setWidth(3,1,"177");
                header.setBackgroundImage(1,1,top);
                header.setBackgroundImage(2,1,topMiddle);
                header.setBackgroundImage(3,1,topRight);
                header.setBackgroundImage(1,2,bottomMiddle);
               // header.setBackgroundImage(2,2,bottomMiddle);
                header.setBackgroundImage(3,2,bottomMiddle);
                header.add(new Image("/pics/spacer.gif","",523,50),1,1);
                header.add(bottomRight,3,2);
                header.setAlignment(3,2,"right");
				header.add(linkCommunications,1,2);
				header.add(linkCalendar,1,2);
				header.add(linkParticipants,1,2);
				header.add(linkHandbook,1,2);
				header.add(linkTimesheets,1,2);
                header.add(linkNews,1,2);


                header.setVerticalAlignment(1,2,"bottom");
                header.setVerticalAlignment(2,2,"bottom");

          frame.add(header,1,1);
          frame.add(tafla, 1,1);

	return frame;
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

	public void add(PresentationObject objectToAdd){
		tafla.add(Text.getBreak(),2,1);
	  tafla.add(objectToAdd,2,1);
	}

        public void addLeft(PresentationObject objectToAdd){

	  tafla.add(objectToAdd,1,1);
	}

        public void addRight(PresentationObject objectToAdd){
	  tafla.add(objectToAdd,3,1);
	}

	public void setLeftAlignment(String align){
		tafla.setAlignment(1,1,align);
	}

}
