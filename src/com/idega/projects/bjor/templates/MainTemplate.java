// idega - Gimmi & Eiki
package com.idega.projects.bjor.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;

public abstract class MainTemplate extends JSPModule implements JspPage{

public Table frame;

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
              jmodule.add(frame);
      }


      public void template() {

        frame = new Table(1,1);
          frame.setCellpadding(0);
          frame.setCellspacing(0);
          frame.setBorder(0);
          frame.setWidth("100%");
          frame.setHeight("100%");
          frame.setVerticalAlignment(1,1,"middle");

      }

      public void add(ModuleObject objectToAdd){
          frame.add(objectToAdd,1,1);
      }

}
