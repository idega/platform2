package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.handicap.presentation.HandicapOverview;

import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

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
//    dummyTable.addBreak(1,1);
    dummyTable1.add(hOverview,1,1);
    add(dummyTable1);
  }

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
    setHomeResultsView();
  }
}
