package is.idega.idegaweb.golf.templates.page;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

//gsi

public class GolfMainJSPModulePage1c extends GolfMainJSPModulePage{

  protected final int SIDEWIDTH = 864;//720
  protected final int LEFTWIDTH = 163;
  protected final int RIGHTWIDTH = 0;

      protected void User(IWContext modinfo) throws SQLException,IOException{
          this.setTextDecoration("none");
          setTopMargin(5);
          add( "top", golfHeader());
          add("top", Top(modinfo));
          add("bottom", golfFooter());

            add(Left(modinfo), Center());

//          add(Left(), Center(), Right());
	  setWidth(1, "" + LEFTWIDTH);
	  setContentWidth( "100%");
//	  setWidth(3, "" + RIGHTWIDTH);

      }


        protected Table Left(IWContext modinfo) throws SQLException,IOException{
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


          leftTable.add(getSidemenu(modinfo),1,2);
          leftTable.add(Text.getBreak(),1,3);
          leftTable.add( sponsors(), 1,3);
//          leftTable.add(getLinks(),1,4);  // gimmi
         // leftTable.add( getPollVoter() ,1,6);



          return leftTable;
        }


      protected Table getSidemenu(IWContext modinfo) {
          Table table = new Table();
            table.setBorder(0);
            table.setWidth(148);
//            table.setHeight(1,1,"22");
//            table.setColor("#99CC99");
            table.setCellpadding(2);
            table.setCellspacing(0);
//            table.setBackgroundImage(1,1,new Image("/pics/adalval.gif"));


            Image dotImage = new Image("/pics/klubbaicon/english.gif");
                    dotImage.setHorizontalSpacing(6);
                    dotImage.setVerticalSpacing(2);
                    dotImage.setAlignment("absmiddle");

            Link staffLink = new Link(iwrb.getLocalizedString("union_board_and_employees","Board and employees"),"/gsi/text.jsp");
                    staffLink.setFontSize(1);
                    staffLink.addParameter("text_id","24");
            Link committyLink = new Link(iwrb.getLocalizedString("committees","Committees"),"/gsi/text.jsp");
                    committyLink.setFontSize(1);
                    committyLink.addParameter("text_id","25");
            Link rulesLink = new Link(iwrb.getLocalizedString("regulations","Regulations"),"/gsi/boxoffice.jsp");
                    rulesLink.setFontSize(1);
                    rulesLink.addParameter("issue_id","5");
            Link listLink = new Link(iwrb.getLocalizedString("lists_and_printouts","Lists and printouts"),"/reports/index.jsp");
                    listLink.setFontSize(1);
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
