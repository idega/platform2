package is.idega.idegaweb.campus.presentation;


import com.idega.presentation.PresentationObjectContainer;
import com.idega.block.reports.presentation.*;
import com.idega.presentation.IWContext;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusHabitants extends PresentationObjectContainer {

  public CampusHabitants() {
  }

  public void main(IWContext iwc){
    Reporter R = new Reporter();
    //R.setMainCategoryAttribute("tenants");
    R.setSQLEdit(true);
    add(R);
  }
}
