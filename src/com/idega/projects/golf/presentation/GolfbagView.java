package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import com.idega.projects.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolfbagView extends GolferJModuleObject {

  public GolfbagView() {
  }

    private void setGolfbagView(){

    Table dummyTable = new Table(2,1);
    dummyTable.setCellpadding(24);
    TextReader golfbagText = new TextReader(this.golferPageData.getGolfbagID());
    golfbagText.setWidth("100%");
    golfbagText.setTableTextSize(1);
    golfbagText.setTextSize(1);
    golfbagText.setTextStyle(Text.FONT_FACE_ARIAL);
    golfbagText.setHeadlineSize(1);
    dummyTable.add(golfbagText,1,1);
    Image sideImage;
    sideImage = iwb.getImage("/shared/pingi3.jpg");
    dummyTable.add(sideImage,2,1);
    dummyTable.setVerticalAlignment(2,1,"top");
    dummyTable.setAlignment(2,1,"right");
    add(dummyTable);
  }

  public void main(ModuleInfo modinfo) throws SQLException{
    super.main(modinfo);
    setGolfbagView();
  }

}