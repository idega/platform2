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
import com.idega.jmodule.calendar.presentation.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.projects.gonguhrolfur.templates.*;

public abstract class MainTemplate3 extends MainTemplate {

public Table leftTable2;
public Table bottomTable;
public Table leftBottomTable;

      public Table Content() {

        Table myTable = new Table(3,1);
          myTable.setWidth(1,"150");
          myTable.setWidth(3,"150");
          myTable.setAlignment(1,1,"center");
          myTable.setVerticalAlignment(1,1,"top");
          myTable.setAlignment(3,1,"center");
          myTable.setVerticalAlignment(3,1,"top");
          myTable.setBorder(0);
          myTable.setWidth("100%");
          myTable.setHeight("100%");
          myTable.setCellpadding(6);
          myTable.setCellspacing(6);

          myTable.add(Left(),1,1);
          myTable.add(Middle(),2,1);
          myTable.add(Right(),3,1);

        return myTable;
    }

    public Table Left() {

        Table contentTable = new Table(1,5);

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
          //headerTable.setHeight("90");
          headerTable.setAlignment(2,1,"right");
          headerTable.setVerticalAlignment(2,1,"top");
          headerTable.setAlignment(1,2,"center");

        Table headerTable2 = new Table(2,2);
          headerTable2.setCellpadding(0);
          headerTable2.setCellspacing(0);
          headerTable2.setWidth(1,1,"23");
          headerTable2.setHeight(1,1,"23");
          headerTable2.setWidth(2,1,"100%");
          headerTable2.setColor("#FFFFFF");
          headerTable2.mergeCells(1,2,2,2);
          headerTable2.setBackgroundImage(2,1,new Image("/pics/template/tabletiler.gif"));
          headerTable2.add(new Image("/pics/template/tablehorn.gif"),1,1);
          headerTable2.setBorder(0);
          headerTable2.setWidth("140");
          //headerTable2.setHeight("90");
          headerTable2.setAlignment(2,1,"right");
          headerTable2.setVerticalAlignment(2,1,"top");
          headerTable2.setAlignment(1,2,"center");

        Table headerTable3 = new Table(2,2);
          headerTable3.setCellpadding(0);
          headerTable3.setCellspacing(0);
          headerTable3.setWidth(1,1,"23");
          headerTable3.setHeight(1,1,"23");
          headerTable3.setWidth(2,1,"100%");
          headerTable3.setColor("#FFFFFF");
          headerTable3.mergeCells(1,2,2,2);
          headerTable3.setBackgroundImage(2,1,new Image("/pics/template/tabletiler.gif"));
          headerTable3.add(new Image("/pics/template/tablehorn.gif"),1,1);
          headerTable3.setBorder(0);
          headerTable3.setWidth("140");
          //headerTable3.setHeight("90");
          headerTable3.setAlignment(2,1,"right");
          headerTable3.setVerticalAlignment(2,1,"top");
          headerTable3.setAlignment(1,2,"center");

          Text tableText = new Text("Innskráning&nbsp;");
            tableText.setFontSize(1);
            tableText.setFontColor("#FFFFFF");
            tableText.setBold();

          headerTable.add(tableText,2,1);

          Text tableText2 = new Text("Fróðleiksmolar&nbsp;");
            tableText2.setFontSize(1);
            tableText2.setFontColor("#FFFFFF");
            tableText2.setBold();

          headerTable2.add(tableText2,2,1);

          Text tableText3 = new Text("Ferðasögur/Umsagnir&nbsp;");
            tableText3.setFontSize(1);
            tableText3.setFontColor("#FFFFFF");
            tableText3.setBold();

          headerTable3.add(tableText3,2,1);

        leftTable = new Table(1,1);
          leftTable.setCellpadding(0);
          leftTable.setCellspacing(0);
          leftTable.setWidth("100%");
          leftTable.setHeight("100%");
          leftTable.setVerticalAlignment(1,1,"top");
          leftTable.setAlignment(1,1,"center");

          headerTable.add(leftTable,1,2);

        leftTable2 = new Table(1,1);
          leftTable2.setCellpadding(0);
          leftTable2.setCellspacing(0);
          leftTable2.setWidth("100%");
          leftTable2.setHeight("100%");
          leftTable2.setVerticalAlignment(1,1,"top");
          leftTable2.setAlignment(1,1,"center");

          headerTable2.add(leftTable2,1,2);

        leftBottomTable = new Table(1,1);
          leftBottomTable.setCellpadding(0);
          leftBottomTable.setCellspacing(0);
          leftBottomTable.setWidth("100%");
          leftBottomTable.setHeight("100%");
          leftBottomTable.setVerticalAlignment(1,1,"top");
          leftBottomTable.setAlignment(1,1,"center");

          headerTable3.add(leftBottomTable,1,2);

        contentTable.add(headerTable,1,1);
        contentTable.add(headerTable2,1,3);
        contentTable.add(headerTable3,1,5);

        return contentTable;
    }

    public Table Right() {

        Table contentTable = new Table(1,5);
          contentTable.setCellpadding(0);
          contentTable.setCellspacing(0);

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
          //headerTable.setHeight("90");
          headerTable.setAlignment(2,1,"right");
          headerTable.setVerticalAlignment(2,1,"top");
          headerTable.setAlignment(1,2,"center");

        Table headerTable2 = new Table(2,2);
          headerTable2.setCellpadding(0);
          headerTable2.setCellspacing(0);
          headerTable2.setWidth(1,1,"23");
          headerTable2.setHeight(1,1,"23");
          headerTable2.setWidth(2,1,"100%");
          headerTable2.setColor("#FFFFFF");
          headerTable2.mergeCells(1,2,2,2);
          headerTable2.setBackgroundImage(2,1,new Image("/pics/template/tabletiler.gif"));
          headerTable2.add(new Image("/pics/template/tablehorn.gif"),1,1);
          headerTable2.setBorder(0);
          headerTable2.setWidth("140");
          //headerTable2.setHeight("90");
          headerTable2.setAlignment(2,1,"right");
          headerTable2.setVerticalAlignment(2,1,"top");
          headerTable2.setAlignment(1,2,"center");

        Table headerTable3 = new Table(2,2);
          headerTable3.setCellpadding(0);
          headerTable3.setCellspacing(0);
          headerTable3.setWidth(1,1,"23");
          headerTable3.setHeight(1,1,"23");
          headerTable3.setWidth(2,1,"100%");
          headerTable3.setColor("#FFFFFF");
          headerTable3.mergeCells(1,2,2,2);
          headerTable3.setBackgroundImage(2,1,new Image("/pics/template/tabletiler.gif"));
          headerTable3.add(new Image("/pics/template/tablehorn.gif"),1,1);
          headerTable3.setBorder(0);
          headerTable3.setWidth("140");
          //headerTable3.setHeight("90");
          headerTable3.setAlignment(2,1,"right");
          headerTable3.setVerticalAlignment(2,1,"top");
          headerTable3.setAlignment(1,2,"center");

          Text tableText = new Text("Á næstunni&nbsp;");
            tableText.setFontSize(1);
            tableText.setFontColor("#FFFFFF");
            tableText.setBold();

          headerTable.add(tableText,2,1);

          Text tableText2 = new Text("Spurning dagsins&nbsp;");
            tableText2.setFontSize(1);
            tableText2.setFontColor("#FFFFFF");
            tableText2.setBold();

          headerTable2.add(tableText2,2,1);

          Text tableText3 = new Text("Hafðu samband&nbsp;");
            tableText3.setFontSize(1);
            tableText3.setFontColor("#FFFFFF");
            tableText3.setBold();

          headerTable3.add(tableText3,2,1);

        Table topTable = new Table(1,1);
          topTable.setCellpadding(0);
          topTable.setCellspacing(0);
          topTable.setWidth("100%");
          topTable.setHeight("100%");
          topTable.setVerticalAlignment(1,1,"top");
          topTable.setAlignment(1,1,"center");

          headerTable.add(topTable,1,2);

        Table middleTable = new Table(1,1);
          middleTable.setCellpadding(0);
          middleTable.setCellspacing(0);
          middleTable.setWidth("100%");
          middleTable.setHeight("100%");
          middleTable.setVerticalAlignment(1,1,"top");
          middleTable.setAlignment(1,1,"center");

          headerTable2.add(middleTable,1,2);

        bottomTable = new Table(1,1);
          bottomTable.setCellpadding(0);
          bottomTable.setCellspacing(0);
          bottomTable.setWidth("100%");
          bottomTable.setHeight("100%");
          bottomTable.setVerticalAlignment(1,1,"top");
          bottomTable.setAlignment(1,1,"center");

          headerTable3.add(bottomTable,1,2);

        Calendar calendar = new Calendar();
          calendar.viewSchedule();
          calendar.setHorizontalColors("#FFFFFF","#FFFFFF");
          calendar.setMaxShown(6);
          calendar.setAlignment("center");
          calendar.setWidth("140");

          topTable.add(calendar,1,1);

        BasicPollVoter poll = new BasicPollVoter("/poll/results.jsp");
          poll.setWidth(110);
          poll.setConnectionAttributes("hrolfur",1);

          middleTable.add(poll,1,1);

        contentTable.add(headerTable,1,1);
        contentTable.add(headerTable2,1,3);
        contentTable.add(headerTable3,1,5);

        return contentTable;

    }

      public void addLeft2(ModuleObject objectToAdd){
        leftTable2.add(objectToAdd,1,1);
      }

      public void addLeft3(ModuleObject objectToAdd){
        leftBottomTable.add(objectToAdd,1,1);
      }
      public void addRight(ModuleObject objectToAdd){
        bottomTable.add(objectToAdd,1,1);
      }

}
