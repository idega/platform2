// idega - Gimmi & Eiki
package com.idega.projects.jonni.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.data.*;
import com.idega.projects.jonni.templates.*;

public abstract class MainTemplate2 extends MainTemplate{

      public void template2() {

        tafla = new Table(3,1);
          tafla.setWidth("100%");
          tafla.setBorder(0);
          tafla.setHeight("100%");
          tafla.setVerticalAlignment(1,1,"top");
          tafla.setVerticalAlignment(3,1,"top");
          tafla.setAlignment(1,1,"center");
          tafla.setCellpadding(0);
          tafla.setCellspacing(0);
          tafla.setWidth(1,"132");
          tafla.setWidth(2,"3");
          tafla.setBackgroundImage(2,1,new Image("/pics/template/dots.gif",""));
          tafla.addBreak(1,1);

        myTable = new Table();
          myTable.setCellpadding(6);
          myTable.setCellspacing(6);

        tafla.add(myTable,3,1);

      }
}
