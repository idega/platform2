package is.idegaweb.campus.service;

import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.IWContext;
import is.idegaweb.campus.application.CampusApplicationForm;
import com.idega.presentation.Table;
import com.idega.presentation.Image;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusApply extends PresentationObjectContainer {

  public CampusApply() {
  }

  public void main(IWContext iwc){

   Table T = new Table(2,1);
      T.setWidth("100%");
      //T.setBorder(1);
      T.setWidth(1,"500");
      T.setAlignment(2,1,"center");
      T.setVerticalAlignment(1,1,"top");
      T.setVerticalAlignment(2,1,"top");

    Image textImage = new Image("/pics/text_pictures/apply.jpg");
      textImage.setVerticalSpacing(12);

    T.add(new CampusApplicationForm(),1,1);
    if ( iwc.getParameter("status") == null || iwc.getParameter("status").equalsIgnoreCase("2") ) {
      T.add(textImage,2,1);
    }
    else {
      T.mergeCells(1,1,2,1);
      T.setWidth(1,"100%");
    }
    add(T);
  }
}