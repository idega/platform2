// idega - Gimmi & Eiki
package com.idega.projects.projectscene.templates;

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

private Link linkGeneral ;
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
		jmodule.setAllMargins(0);
		jmodule.setAlinkColor("black");
		jmodule.setVlinkColor("black");
		jmodule.setLinkColor("black");
                setPage(jmodule);

		jmodule.add(template());
                String title = (language.equalsIgnoreCase("IS")) ? "Slysaskrá Íslands" : "Accident Register";

                jmodule.setTitle(title);

	}


public Table template() {

  frame = new Table(1,1);
  frame.setWidth("100%");
  tafla = new Table(3,1);

  tafla.setWidth("100%");
  tafla.setHeight("100%");
  tafla.setHeight(1,1,"68");
  tafla.setCellpadding(0);
  tafla.setCellspacing(0);
  tafla.setAlignment(1,1,"left");
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

  String generalImage = "/pics/Slysaskraning/"+language+"/general.gif";
  String commImage = "/pics/Slysaskraning/"+language+"/comm.gif";
  String calendarImage = "/pics/Slysaskraning/"+language+"/calendar.gif";
  String participantsImage = "/pics/Slysaskraning/"+language+"/participants.gif";
  String newsImage = "/pics/Slysaskraning/"+language+"/home.gif";
  String handbookImage = "/pics/Slysaskraning/"+language+"/handbook.gif";
  String timesheetsImage = "/pics/Slysaskraning/"+language+"/timesheets.gif";
  String accidentImage = "/pics/Slysaskraning/"+language+"/committee.gif";
  String topImage = "/pics/Slysaskraning/"+language+"/topleft.gif";

  linkGeneral = new Link(new Image(generalImage,"general info"),"/general.jsp");
  linkCommunications = new Link(new Image(commImage,"communications"),"/communications.jsp");
  linkCalendar = new Link(new Image(calendarImage,"calendar"),"/calendar.jsp");
  linkParticipants = new Link(new Image(participantsImage,"participants"),"/participants.jsp");
  linkHandbook = new Link(new Image(handbookImage,"handbook"),"/boxoffice/index.jsp");
  linkHandbook.addParameter("issue_id","1");
  linkTimesheets = new Link(new Image(timesheetsImage,"timesheets"),"/timesheets.jsp");
  linkAccident = new Link(new Image(accidentImage,"committee"),"/boxoffice/index2.jsp");
  linkAccident.addParameter("issue_id","2");
  linkNews = new Link(new Image(newsImage,"news"),"/index.jsp");

  addToLink("language",language);


  Image top = new Image(topImage);
  Image topMiddle = new Image("/pics/Slysaskraning/"+language+"/topmiddle.gif");
  Image bottomMiddle = new Image("/pics/Slysaskraning/"+language+"/middlebottom.gif");
  Image topRight = new Image("/pics/Slysaskraning/"+language+"/topright.gif");
  Image bottomRight = new Image("/pics/Slysaskraning/"+language+"/kulurright.gif","kulur",177,18);

  Table header = new Table(3,2);
  int length = 543;
  header.setWidth("100%");
  header.mergeCells(1,2,2,2);

  header.setHeight(1,"50");
  header.setHeight(2,"18");
  header.setCellpadding(0);
  header.setCellspacing(0);

  header.setWidth(1,1,Integer.toString(length));
  header.setWidth(3,1,"177");
  header.add(top,1,1);
  header.setBackgroundImage(2,1,topMiddle);
    header.setBackgroundImage(1,1,topMiddle);
  header.setBackgroundImage(3,1,topRight);
  header.setBackgroundImage(1,2,bottomMiddle);
 // header.setBackgroundImage(2,2,bottomMiddle);
  header.setBackgroundImage(3,2,bottomMiddle);
 // header.add(new Image("/pics/spacer.gif","",length,50),1,1);
  header.add(bottomRight,3,2);
  header.setAlignment(3,2,"right");

  header.add(linkGeneral,1,2);
  header.add(linkCommunications,1,2);
  header.add(linkCalendar,1,2);
  header.add(linkParticipants,1,2);
  header.add(linkHandbook,1,2);
  header.add(linkTimesheets,1,2);
  header.add(linkAccident,1,2);
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

                linkAccident.addParameter(parameterName,parameterValue);

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



}
