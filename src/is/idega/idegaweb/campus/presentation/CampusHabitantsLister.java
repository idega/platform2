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



public class CampusHabitantsLister extends PresentationObjectContainer {



  public CampusHabitantsLister() {

  }



  public void main(IWContext iwc){

    Reporter R = new Reporter();

    R.setReportCategoryId(1);

    //R.setMainCategoryAttribute("tenants");

    R.setSQLEdit(true);

    add(R);

  }

}
