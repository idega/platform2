package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;
import java.lang.String;
import java.sql.SQLException;
import com.idega.presentation.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.text.presentation.TextReader;
import is.idega.idegaweb.golf.entity.GolferPageData;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class AbroadResultsView extends GolferBlock {

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

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
    setAbroadResultsView();
  }
}
