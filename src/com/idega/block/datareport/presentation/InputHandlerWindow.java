package com.idega.block.datareport.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.presentation.StyledIWAdminWindow;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Dec 1, 2003
 */
public class InputHandlerWindow extends StyledIWAdminWindow {
	
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
  
  public InputHandlerWindow() {
    setResizable(true);
    setWidth(900);
    setHeight(500);
    setScrollbar(true);
  }

  public void main(IWContext iwc) throws Exception {  
  	PresentationObject fieldHandler = new InputHandlerChooser();
  	add(fieldHandler);
   }
    
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  } 
}
