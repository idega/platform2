package is.idega.idegaweb.golf.templates.page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import is.idega.idegaweb.golf.*;
import is.idega.idegaweb.golf.moduleobject.Login;
import com.idega.jmodule.*;
import com.idega.jmodule.banner.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.jmodule.boxoffice.presentation.*;
import com.idega.jmodule.sidemenu.presentation.*;
import java.sql.*;
import is.idega.idegaweb.golf.entity.*;
import is.idega.idegaweb.golf.templates.*;
import java.io.*;
import java.util.Vector;


public class GolfMainJSPModulePage1c extends GolfMainJSPModulePage{

  protected final int SIDEWIDTH = 864;//720
  protected final int LEFTWIDTH = 163;
  protected final int RIGHTWIDTH = 0;

      protected void User(IWContext iwc) throws SQLException,IOException{
          this.setTextDecoration("none");
          setTopMargin(5);
          add( "top", golfHeader());
          add("top", Top(iwc));
          add("bottom", golfFooter());
          add(Left(iwc), Center());
//          add(Left(), Center(), Right());
	  setWidth(1, "" + LEFTWIDTH);
	  setContentWidth( "100%");
//	  setWidth(3, "" + RIGHTWIDTH);

      }


        protected Table Left(IWContext iwc) throws SQLException,IOException{
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


          leftTable.add(getSidemenu(iwc),1,2);
          leftTable.add(Text.getBreak(),1,3);
          leftTable.add( Sponsors(), 1,3);
//          leftTable.add(getLinks(),1,4);  // gimmi
         // leftTable.add( getPollVoter() ,1,6);



          return leftTable;
        }


      protected Table getSidemenu(IWContext iwc) {
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

            Text staffText = new Text(iwrb.getLocalizedString("union_board_and_employees","Board and employees"));
              staffText.setFontSize(1);
            Link staffLink = new Link(staffText,"/gsi/text.jsp");
                    staffLink.addParameter("text_id","24");

            Text committyText = new Text(iwrb.getLocalizedString("committees","committees"));
              committyText.setFontSize(1);
            Link committyLink = new Link(committyText,"/gsi/text.jsp");
                    committyLink.addParameter("text_id","25");

            Text rulesText = new Text(iwrb.getLocalizedString("regulations","Regulations"));
              rulesText.setFontSize(1);
            Link rulesLink = new Link(rulesText,"/gsi/boxoffice.jsp");
                    rulesLink.addParameter("issue_id","5");

            Text listText = new Text(iwrb.getLocalizedString("lists_and_printouts","Lists and printouts"));
              listText.setFontSize(1);
            Link listLink = new Link(listText,"/reports/index.jsp");
                    listLink.setTarget("_new");

            table.add(dotImage,1,1);
            table.add(staffLink,1,1);
            table.add(dotImage,1,2);
            table.add(committyLink,1,2);
            table.add(dotImage,1,3);
            table.add(rulesLink,1,3);
            if ( isAdmin ) {
              table.add(dotImage,1,4);
              table.add(listLink,1,4);
            }


          return table;


      }

}  // class GolfMainJSPModule1c
