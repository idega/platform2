package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.news.presentation.NewsReader;
import java.lang.String;
import java.sql.SQLException;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.projects.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class StatisticsView extends GolferJModuleObject {

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

  public void main(ModuleInfo modinfo) throws SQLException{
    super.main(modinfo);
  }

}