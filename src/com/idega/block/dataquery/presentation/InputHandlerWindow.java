package com.idega.block.dataquery.presentation;

import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description:
 * This class and the class InputHandlerChooser are not in use at the moment.
 * <br>
 * Both classes are used for choosing (or changing) an InputHandler that is linked to a certain field in a query,
 * that is when defining a condition for that  field the specified InputHandler is used.
 * This feature is disabled in the QueryBuilder at the moment and should work only in the expert mode.
 * </p>
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
