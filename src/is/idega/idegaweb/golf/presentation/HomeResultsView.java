package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.Block;
import is.idega.idegaweb.golf.HandicapOverview;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;
import com.idega.jmodule.news.presentation.NewsReader;
import java.lang.String;
import java.sql.SQLException;
import com.idega.presentation.text.*;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class HomeResultsView extends GolferBlock implements LinkParameters{

  public HomeResultsView() {
  }

  //Árangur Heima
  private void setHomeResultsView(){
   // addFakeResultsSidemenu();
    Table dummyTable1 = new Table(1,1);
    dummyTable1.setWidth("100%");
    dummyTable1.setCellpadding(10);
  /*  dummyTable.setAlignment(1,1,"center");
    dummyTable.setVerticalAlignment(1,2,"top");*/
/*    Text handicapText = new Text("Forgjafar Yfirlit Björgvins");
    handicapText.setFontSize(3);
    handicapText.setBold();
    dummyTable.add(handicapText);*/

    HandicapOverview hOverview = new HandicapOverview(member_id);
      hOverview.noIcons();
      hOverview.setTilPicture("/golferpage/til.gif");
      hOverview.setFraPicture("/golferpage/fra.gif");
      hOverview.setGetOverviewButton("/golferpage/saekja.gif", sTopMenuParameterName, homeResultsParameterValue);
      hOverview.setViewScoreIconUrlInBundle("/shared/iconSkoda.gif");
      Text headerText = new Text();
      headerText.setFontColor("#FF6000");
      headerText.setFontSize(2);
      headerText.setBold();
      headerText.setFontStyle(Text.FONT_FACE_VERDANA);
      hOverview.setHeaderTextProperties(headerText);
      Text tableText = new Text();
      tableText.setBold();
      tableText.setFontColor("000000");
      tableText.setFontSize(1);
      tableText.setFontStyle(Text.FONT_FACE_VERDANA);
      hOverview.setTableTextProperties(tableText);
      Link textLink = new Link();
      textLink.setBold();
      textLink.setFontSize(1);
      textLink.setFontStyle(Text.FONT_FACE_VERDANA);
      hOverview.setTextLinkProperties(textLink);
      hOverview.setHeaderColor("#FFFFFF");
      hOverview.setTeeTextColor("#000000");
//    dummyTable.addBreak(1,1);
    dummyTable1.add(hOverview,1,1);
    add(dummyTable1);
  }

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
    setHomeResultsView();
  }
}
