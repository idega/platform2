// idega - Gimmi & Eiki
package com.idega.projects.lv.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.data.*;

public abstract class PSMainTemplate extends JSPModule implements JspPage{

public Table tafla;
public Table frame;

private Link linkCommunications ;
private Link linkCalendar ;
private Link linkParticipants ;
private Link linkHandbook ;
private Link linkTimesheets ;
private Link linkNews;
private Link linkAccident;

public String language = "IS";


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
                setPage(jmodule);

		jmodule.add(template());
                String title = "Verkefnahandbók";

                jmodule.setTitle(title);

	}


	public Table template() {
          frame = new Table(1,1);
          frame.setCellpadding(0);
          frame.setCellspacing(0);
          frame.setWidth("100%");
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
	tafla.setAlignment(1,1,"center");
        tafla.setAlignment(2,1,"left");
        tafla.setAlignment(3,1,"center");
        tafla.setWidth(1,1,"160");
        tafla.setWidth(3,1,"177");

     tafla.setAlignment("top");
     tafla.setVerticalAlignment("top");
     frame.setRowVerticalAlignment(1,"top");

        frame.setVerticalAlignment(1,1,"top");
	tafla.setVerticalAlignment(1,1,"top");
        tafla.setVerticalAlignment(2,1,"top");
        tafla.setVerticalAlignment(3,1,"top");


    String language2 = getIWContext().getRequest().getParameter("language");
    if (language2==null) language2 = ( String ) getIWContext().getSession().getAttribute("language");
    if ( language2 != null) language = language2;

    getIWContext().setSpokenLanguage( language );

    getIWContext().getSession().setAttribute("language",language);


    String commImage = "/pics/Projectscene/"+language+"/comm.gif";
    String calendarImage = "/pics/Projectscene/"+language+"/calendar.gif";
    String participantsImage = "/pics/Projectscene/"+language+"/participants.gif";
    String newsImage = "/pics/Projectscene/"+language+"/home.gif";
    String handbookImage = "/pics/Projectscene/"+language+"/handbook.gif";
    String timesheetsImage = "/pics/Projectscene/"+language+"/timesheets.gif";
    String accidentImage = "/pics/Projectscene/"+language+"/committee.gif";
    String topImage = "/pics/Projectscene/"+language+"/topleft.gif";

        linkCommunications = new Link(new Image(commImage,"communications"),"/project/communications.jsp");
	linkCalendar = new Link(new Image(calendarImage,"calendar"),"/project/calendar.jsp");
	linkParticipants = new Link(new Image(participantsImage,"participants"),"/project/participants.jsp");
	linkHandbook = new Link(new Image(handbookImage,"handbook"),"/project/boxoffice/index.jsp");
        linkHandbook.addParameter("issue_id","4");
        linkTimesheets = new Link(new Image(timesheetsImage,"timesheets"),"/project/timesheets.jsp");
	linkNews = new Link(new Image(newsImage,"news"),"/project/index.jsp");

	Image top = new Image(topImage);
        Image topMiddle = new Image("/pics/Projectscene/"+language+"/topmiddle.gif");
        Image bottomMiddle = new Image("/pics/Projectscene/"+language+"/middlebottom.gif");
        Image topRight = new Image("/pics/Projectscene/"+language+"/topright.gif");
        Image bottomRight = new Image("/pics/Projectscene/"+language+"/kulurright.gif","kulur",177,18);

                header.setWidth(1,1,"354");
                header.setWidth(3,1,"177");
                header.setBackgroundImage(1,1,topMiddle);
                header.setBackgroundImage(2,1,topMiddle);
                header.setBackgroundImage(3,1,topRight);
                header.setBackgroundImage(1,2,bottomMiddle);
                header.setBackgroundImage(3,2,bottomMiddle);
                header.add(new Image("/pics/spacer.gif","",518,50),1,1);
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
		tafla.add(Text.getBreak(),3,1);
	  tafla.add(objectToAdd,3,1);
	}



}
