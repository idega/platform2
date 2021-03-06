package is.idega.idegaweb.golf.presentation;

import java.sql.SQLException;

import com.idega.block.text.presentation.TextReader;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class StatisticsView extends GolferBlock {

  public StatisticsView() {
  }

  private void setStatisticsView(){
    Table dummyTable = new Table(2,1);
    dummyTable.setWidth("100%");
    dummyTable.setCellpadding(24);
    //Image iStatisticsLogo = iwrb.getImage("/golferpage/tolfraedi.gif");
    //this.addLeftLogo(iStatisticsLogo);
    TextReader statisticText = new TextReader(golferPageData.getStatisticsID());
    statisticText.setWidth("100%");
    statisticText.setTableTextSize(1);
    statisticText.setTextSize(1);
    statisticText.setTextStyle(Text.FONT_FACE_ARIAL);
    statisticText.setHeadlineSize(1);
    dummyTable.add(statisticText,1,1);
    Image statisticsImage;
    statisticsImage = iwb.getImage("/shared/tolfraediMynd.gif");
    dummyTable.add(statisticsImage,2,1);
    dummyTable.setVerticalAlignment(2,1,"top");
    dummyTable.setAlignment(2,1,"right");
    add(dummyTable);
  }

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
  }

}
