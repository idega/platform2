// idega - Gimmi & Eiki
package com.idega.projects.gonguhrolfur.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;
import java.util.*;

public abstract class MainTemplate extends JSPModule implements JspPage{

public Table content;
public Table leftTable;
public Table mainTable;
public String subPage;

      public void initializePage(){
          super.initializePage();
              Page jmodule = getPage();
              jmodule.setMarginHeight(0);
              jmodule.setMarginWidth(0);
              jmodule.setLeftMargin(0);
              jmodule.setTopMargin(0);
              jmodule.setAlinkColor("black");
              jmodule.setVlinkColor("black");
              jmodule.setLinkColor("black");
              jmodule.setHoverColor("#58AE47");
              jmodule.setTextDecoration("none");
              jmodule.setStyleSheetURL("/style/idega.css");
              jmodule.setBackgroundColor("#336699");
              setPage(jmodule);

              template();
              jmodule.add(content);
      }


      public void template() {

        ModuleInfo modinfo = getModuleInfo();

        String page = modinfo.getRequest().getRequestURI();
        subPage = page.substring(1,page.indexOf("."));

	content = new Table(1,3);
          content.setAlignment("center");
          content.setCellpadding(0);
          content.setCellspacing(0);
          content.setWidth("800");
          content.setHeight("100%");
          content.setBorder(0);
          content.setHeight(1,"112");
          content.setHeight(2,"22");
          content.setHeight(3,"100%");
          content.setWidth(1,"800");
          content.setColor("#336699");
          content.mergeCells(1,3,2,3);
          content.setVerticalAlignment(1,3,"top");

          content.add(new Image("/pics/template/header.jpg"),1,1);

        Table linksTable = new Table();
          linksTable.setCellpadding(0);
          linksTable.setCellspacing(0);

          Image newsImage = new Image("/pics/template/frettir.gif","Aðalsíða",60,22);
          Image chatImage = new Image("/pics/template/spjall.gif","Spjall",60,22);
          Image infoImage = new Image("/pics/template/umgongu.gif","Um Gönguhrólf",88,22);
          Image routeImage = new Image("/pics/template/gonguleidir.gif","Gönguleiðir",88,22);
          Image guideImage = new Image("/pics/template/leidsogu.gif","Leiðsögumenn",88,22);
          Image picsImage = new Image("/pics/template/ferdam.gif","Ferðamyndir",88,22);
          Image calendarImage = new Image("/pics/template/dagatal.gif","Dagatal",88,22);

          Link newsLink = new Link(newsImage,"/index.jsp");
          Link chatLink = new Link(chatImage,"/chat.jsp");
          Link infoLink = new Link(infoImage,"/info.jsp");
          Link routeLink = new Link(routeImage,"/routes.jsp");
          Link guideLink = new Link(guideImage,"/guides.jsp");
          Link picsLink = new Link(picsImage,"/pics.jsp");
          Link calendarLink = new Link(calendarImage,"/calendar.jsp");

          if ( subPage != null ) {
            if ( subPage.equalsIgnoreCase("chat") ) {
              chatImage.setSrc("/pics/template/spjall2.gif");
            }
            if ( subPage.equalsIgnoreCase("info") ) {
              infoImage.setSrc("/pics/template/umgongu2.gif");
            }
            if ( subPage.equalsIgnoreCase("routes") ) {
              routeImage.setSrc("/pics/template/gonguleidir2.gif");
            }
            if ( subPage.equalsIgnoreCase("guides") ) {
              guideImage.setSrc("/pics/template/leidsogu2.gif");
            }
            if ( subPage.equalsIgnoreCase("pics") ) {
              picsImage.setSrc("/pics/template/ferdam2.gif");
            }
            if ( subPage.equalsIgnoreCase("calendar") ) {
              calendarImage.setSrc("/pics/template/dagatal2.gif");
            }
          }

          linksTable.add(new Image("/pics/template/spot.gif","",41,22),1,1);
          linksTable.add(newsLink,1,1);
          linksTable.add(chatLink,1,1);
          linksTable.add(new Image("/pics/template/header2.gif","",155,22),1,1);
          linksTable.add(infoLink,1,1);
          linksTable.add(routeLink,1,1);
          linksTable.add(guideLink,1,1);
          linksTable.add(picsLink,1,1);
          linksTable.add(calendarLink,1,1);
          linksTable.add(new Image("/pics/template/header3.gif","",44,22),1,1);

          content.add(linksTable,1,2);
          content.add(Content(),1,3);
      }

      public Table Content() {

        Table myTable = new Table(2,1);
          myTable.setWidth(1,"150");
          myTable.setAlignment(1,1,"center");
          myTable.setVerticalAlignment(1,1,"top");
          myTable.setBorder(0);
          myTable.setWidth("100%");
          myTable.setHeight("100%");
          myTable.setCellpadding(6);
          myTable.setCellspacing(6);

          myTable.add(Left(),1,1);
          myTable.add(Middle(),2,1);

        return myTable;
      }

      public Table Left() {

        Table headerTable = new Table(2,2);
          headerTable.setCellpadding(0);
          headerTable.setCellspacing(0);
          headerTable.setWidth(1,1,"23");
          headerTable.setHeight(1,1,"23");
          headerTable.setWidth(2,1,"100%");
          headerTable.setColor("#FFFFFF");
          headerTable.mergeCells(1,2,2,2);
          headerTable.setBackgroundImage(2,1,new Image("/pics/template/tabletiler.gif"));
          headerTable.add(new Image("/pics/template/tablehorn.gif"),1,1);
          headerTable.setBorder(0);
          headerTable.setWidth("140");
          headerTable.setHeight("90");
          headerTable.setAlignment(2,1,"right");
          headerTable.setVerticalAlignment(2,1,"top");
          headerTable.setAlignment(1,2,"center");

          Text tableText = new Text("Innskráning&nbsp;");

          if ( subPage != null ) {
            if ( subPage.equalsIgnoreCase("info") ) {
              tableText = new Text("Um Gönguhrólf&nbsp;");
            }
            if ( subPage.equalsIgnoreCase("routes") ) {
              tableText = new Text("Gönguleiðir&nbsp;");
            }
            if ( subPage.equalsIgnoreCase("guides") ) {
              tableText = new Text("Leiðsögumenn&nbsp;");
            }
            if ( subPage.equalsIgnoreCase("pics") ) {
              tableText = new Text("Ferðamyndir&nbsp;");
            }
          }

            tableText.setFontSize(1);
            tableText.setFontColor("#FFFFFF");
            tableText.setBold();

          headerTable.add(tableText,2,1);

        leftTable = new Table(1,1);
          leftTable.setCellpadding(3);
          leftTable.setCellspacing(3);
          leftTable.setWidth("100%");
          leftTable.setHeight("100%");
          leftTable.setVerticalAlignment(1,1,"top");
          leftTable.setAlignment(1,1,"center");

          headerTable.add(leftTable,1,2);

        return headerTable;
      }

      public Table Middle() {

        Table headerTable2 = new Table(2,2);
          headerTable2.setCellpadding(0);
          headerTable2.setCellspacing(0);
          headerTable2.setWidth(1,1,"23");
          headerTable2.setWidth(2,1,"100%");
          headerTable2.setHeight(1,1,"23");
          headerTable2.setColor("#FFFFFF");
          headerTable2.mergeCells(1,2,2,2);
          headerTable2.setBackgroundImage(2,1,new Image("/pics/template/tabletiler.gif"));
          headerTable2.add(new Image("/pics/template/tablehorn.gif"),1,1);
          headerTable2.setBorder(0);
          headerTable2.setWidth("100%");
          headerTable2.setHeight("100%");
          headerTable2.setAlignment(2,1,"right");
          headerTable2.setVerticalAlignment(2,1,"top");

        mainTable = new Table(1,1);
          mainTable.setCellpadding(3);
          mainTable.setCellspacing(3);
          mainTable.setWidth("100%");
          mainTable.setHeight("100%");
          mainTable.setVerticalAlignment(1,1,"top");

          headerTable2.add(mainTable,1,2);

        return headerTable2;

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

      public void add(ModuleObject objectToAdd){
        mainTable.add(objectToAdd,1,1);
      }

      public void addLeft(ModuleObject objectToAdd){
        leftTable.add(objectToAdd,1,1);
      }

}
