/*

 * $Id: CampusFactoryHandler.java,v 1.3 2002/04/06 19:11:15 tryggvil Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.presentation.handler;



import is.idega.idegaweb.campus.presentation.TitleIcons;

import is.idega.idegaweb.campus.presentation.CampusFactory;

import com.idega.builder.handler.PropertyHandler;

import com.idega.presentation.PresentationObject;

import com.idega.presentation.IWContext;

import com.idega.presentation.ui.DropdownMenu;

import com.idega.presentation.text.Text;

import java.util.List;



/**

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */

public class CampusFactoryHandler implements PropertyHandler {

  public final static int TABBER = CampusFactory.TABBER;

  public final static int CONTENT = CampusFactory.CONTENT;

  public final static int MENU = CampusFactory.MENU;



  /**

   *

   */

  public CampusFactoryHandler() {

  }



  /**

   *

   */

  public List getDefaultHandlerTypes() {

    return(null);

  }



  /**

   *

   */

  public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {

    DropdownMenu menu = new DropdownMenu(name);

    menu.addMenuElement("","Select:");

    menu.addMenuElement(String.valueOf(TABBER) ,"TABBER");

    menu.addMenuElement(String.valueOf(CONTENT),"CONTENT");

    menu.addMenuElement(String.valueOf(MENU),"MENU");

    menu.setSelectedElement(value);

    return(menu);

  }



  /**

   *

   */

  public void onUpdate(String values[], IWContext iwc) {

  }

}
