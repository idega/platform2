package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.moduleobject.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.jmodule.sidemenu.presentation.*;
import java.sql.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.templates.*;
import java.io.*;
import java.util.Vector;


public class GolfMainJSPModule1c extends GolfMainJSPModule{



  protected final int SIDEWIDTH = 864;//720
  protected final int LEFTWIDTH = 163;
  protected final int RIGHTWIDTH = 0;

      protected void User() throws SQLException,IOException{
          getPage().setTextDecoration("none");
          setTopMargin(5);
          add( "top", golfHeader());
          add("top", Top());
          add("bottom", golfFooter());
          add(Left(), Center());
//          add(Left(), Center(), Right());
	  setWidth(1, "" + LEFTWIDTH);
	  setContentWidth( "100%");
//	  setWidth(3, "" + RIGHTWIDTH);

      }


        protected Table Left() throws SQLException,IOException{
          Table leftTable = new Table(1,4);
//          leftTable.setBorder(1);
          leftTable.setVerticalAlignment("top");
          leftTable.setVerticalAlignment(1,1,"top");
          leftTable.setVerticalAlignment(1,2,"top");
          leftTable.setVerticalAlignment(1,3,"top");
          leftTable.setVerticalAlignment(1,4,"top");
          leftTable.setHeight("100%");
          leftTable.setHeight(2,"20");
          leftTable.setHeight(4,"100%");



          leftTable.setColumnAlignment(1, "left");

          leftTable.setWidth("" + LEFTWIDTH);

          leftTable.setCellpadding(0);
          leftTable.setCellspacing(0);


          leftTable.add(getSidemenu(),1,2);
          leftTable.add("<br>",1,3);
          leftTable.add( Sponsors(), 1,3);
//          leftTable.add(getLinks(),1,4);  // gimmi
         // leftTable.add( getPollVoter() ,1,6);



          return leftTable;
        }


      protected Table getSidemenu() {
          Table table = new Table();
            table.setBorder(0);
            table.setWidth(148);
//            table.setHeight(1,1,"22");
//            table.setColor("#99CC99");
            table.setCellpadding(2);
            table.setCellspacing(0);
//            table.setBackgroundImage(1,1,new Image("/pics/adalval.gif"));

            Image dotImage = new Image("/pics/klubbaicon/english.gif");
                    dotImage.setAttribute("hspace","6");
                    dotImage.setAttribute("vspace","2");
                    dotImage.setAttribute("align","absmiddle");

            Link staffLink = new Link("Stjórn og starfsmenn","/gsi/text.jsp");
                    staffLink.setFontSize(1);
                    staffLink.addParameter("text_id","24");
            Link committyLink = new Link("Nefndir","/gsi/text.jsp");
                    committyLink.setFontSize(1);
                    committyLink.addParameter("text_id","25");
            Link rulesLink = new Link("Reglugerðir","/gsi/boxoffice.jsp");
                    rulesLink.setFontSize(1);
                    rulesLink.addParameter("issue_id","5");
            Link listLink = new Link("Listar og útprentarnir","/list/list.jsp");
                    listLink.setFontSize(1);
                    listLink.setTarget("_new");

            table.add(dotImage,1,1);
            table.add(staffLink,1,1);
            table.add(dotImage,1,2);
            table.add(committyLink,1,2);
            table.add(dotImage,1,3);
            table.add(rulesLink,1,3);
            if ( isAdmin() ) {
              table.add(dotImage,1,4);
              table.add(listLink,1,4);
            }


          return table;


      }

}  // class GolfMainJSPModule1c
