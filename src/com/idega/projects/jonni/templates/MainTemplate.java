// idega - Gimmi & Eiki
package com.idega.projects.jonni.templates;

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

public Table tafla;
public Table frame;
public Table myTable;

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
              jmodule.setHoverColor("#FF9310");
              jmodule.setTextDecoration("none");
              jmodule.setStyleSheetURL("/style/idega.css");
              setPage(jmodule);

              template();
              template2();
              jmodule.add(frame);
              jmodule.add(tafla);
      }


      public void template() {

        ModuleInfo modinfo = getModuleInfo();

        String page = modinfo.getRequest().getRequestURI();
        String subPage = page.substring(1,page.indexOf("."));

        frame = new Table(2,3);
          frame.setCellpadding(0);
          frame.setCellspacing(0);
          frame.setBorder(0);
          frame.setWidth("100%");
          frame.setWidth(1,"134");
          frame.setHeight(1,"54");
          frame.setHeight(2,"79");
          frame.setHeight(3,"141");
          frame.setColor(2,1,"#0e2934");
          frame.setColor(2,3,"#b8c1c8");
          frame.setVerticalAlignment(2,1,"bottom");
          frame.setVerticalAlignment(2,2,"bottom");

          Image headerImage = new Image("/pics/template/welcome.gif","",217,37);

          if ( subPage != null ) {
            if ( subPage.equalsIgnoreCase("index") ) {
              headerImage.setSrc("/pics/template/welcome.gif");
            }
            if ( subPage.equalsIgnoreCase("info") ) {
              headerImage.setSrc("/pics/template/info1.gif");
            }
            if ( subPage.equalsIgnoreCase("work") ) {
              headerImage.setSrc("/pics/template/work1.gif");
            }
            if ( subPage.equalsIgnoreCase("pictures") ) {
              headerImage.setSrc("/pics/template/pictures1.gif");
            }
            if ( subPage.equalsIgnoreCase("links") ) {
              headerImage.setSrc("/pics/template/links1.gif");
            }
          }

          frame.add(headerImage,2,2);

          Random number = new Random();
          int random = number.nextInt(23) + 1;

          Image randomImage = new Image("/pics/template/random"+random+".jpg","",134,141);
          frame.add(randomImage,1,3);

          frame.add(new Image("/pics/template/vibrant.gif","",134,54),1,1);
          frame.add(new Image("/pics/template/blueblock.gif","",134,79),1,2);
          frame.add(new Image("/pics/template/middle.gif","",650,141),2,3);
          frame.setBackgroundImage(2,3,new Image("/pics/template/middletiler.gif",""));

          Link infoLink = new Link(new Image("/pics/template/info.gif","Info",145,19),"/info.jsp");
          Link workLink = new Link(new Image("/pics/template/work.gif","Works",72,19),"/work.jsp");
          Link picturesLink = new Link(new Image("/pics/template/pictures.gif","Pictures",90,19),"/pictures.jsp");
          Link linksLink = new Link(new Image("/pics/template/links.gif","Links",89,19),"/links.jsp");
          Link homeLink = new Link(new Image("/pics/template/home.gif","Home",79,19),"/index.jsp");

          frame.add(infoLink,2,1);
          frame.add(workLink,2,1);
          frame.add(picturesLink,2,1);
          frame.add(linksLink,2,1);
          frame.add(homeLink,2,1);

      }

      public void template2() {

        tafla = new Table(5,1);
          tafla.setWidth("100%");
          tafla.setBorder(0);
          tafla.setHeight("100%");
          tafla.setVerticalAlignment(1,1,"top");
          tafla.setVerticalAlignment(3,1,"top");
          tafla.setVerticalAlignment(5,1,"top");
          tafla.setAlignment(1,1,"center");
          tafla.setCellpadding(0);
          tafla.setCellspacing(0);
          tafla.setWidth(1,"132");
          tafla.setWidth(2,"3");
          tafla.setWidth(3,"210");
          tafla.setWidth(4,"3");
          tafla.setBackgroundImage(2,1,new Image("/pics/template/dots.gif",""));
          tafla.setBackgroundImage(4,1,new Image("/pics/template/dots.gif",""));
          tafla.addBreak(1,1);

        myTable = new Table();
          myTable.setCellpadding(6);
          myTable.setCellspacing(6);

        tafla.add(myTable,3,1);

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
          myTable.add(objectToAdd,1,1);
      }

      public void addLeft(ModuleObject objectToAdd){
          tafla.add(objectToAdd,1,1);
      }

      public void addRight(ModuleObject objectToAdd){
          tafla.add(objectToAdd,5,1);
      }

}
