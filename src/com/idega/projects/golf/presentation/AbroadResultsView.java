package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;
import java.lang.String;
import java.sql.SQLException;
import com.idega.jmodule.object.textObject.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.projects.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class AbroadResultsView extends GolferJModuleObject {

  public AbroadResultsView() {
  }
    //Árangur Erlendis

  private void setAbroadResultsView(){
    Table dummyTable = new Table(2,1);
    dummyTable.setCellpadding(10);
    dummyTable.setWidth("100%");
    dummyTable.setAlignment(2,1,"center");
    dummyTable.setAlignment(1,1,"center");
    dummyTable.setVerticalAlignment(1,1,"top");
    dummyTable.setVerticalAlignment(2,1,"top");
    TextReader abroadResultsTextReader = new TextReader(this.golferPageData.getResultsAbroadID());
    Image resultsImage;
    resultsImage = iwb.getImage("/shared/arangurMynd.jpg");
    dummyTable.add(resultsImage,2,1);
    dummyTable.add(abroadResultsTextReader, 1, 1);
    add(dummyTable);
  }

  public void main(ModuleInfo modinfo) throws SQLException{
    super.main(modinfo);
    setAbroadResultsView();
  }
}