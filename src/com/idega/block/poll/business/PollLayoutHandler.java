package com.idega.block.poll.business;

import java.util.List;
import com.idega.block.poll.presentation.Poll;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.text.Text;
import com.idega.builder.handler.PropertyHandler;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class PollLayoutHandler implements PropertyHandler {

  public PollLayoutHandler() {
  }
  public List getDefaultHandlerTypes() {
    return null;
  }
  public PresentationObject getHandlerObject(String name,String value,IWContext iwc){
    DropdownMenu menu = new DropdownMenu(name);
    menu.addMenuElement("","Select:");
    menu.addMenuElement(Poll.RADIO_BUTTON_VIEW,"Button view");
    menu.addMenuElement(Poll.LINK_VIEW,"Link view");
    menu.setSelectedElement(value);
    return menu;
  }

}