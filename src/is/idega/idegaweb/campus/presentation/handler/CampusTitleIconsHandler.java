package is.idega.idegaweb.campus.presentation.handler;



import is.idega.idegaweb.campus.presentation.TitleIcons;
import com.idega.builder.handler.PropertyHandler;
import java.util.List;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.text.Text;


/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class CampusTitleIconsHandler implements PropertyHandler {

	public final static String MENU = TitleIcons.MAINMENU;
	public final static String LOGIN = TitleIcons.LOGIN;

  public CampusTitleIconsHandler() {
  }
  public List getDefaultHandlerTypes() {
    return null;
  }
  public PresentationObject getHandlerObject(String name,String value,IWContext iwc){
    DropdownMenu menu = new DropdownMenu(name);
    menu.addMenuElement("","Select:");
    menu.addMenuElement(MENU ,"MENU");
    menu.addMenuElement(LOGIN,"LOGIN");
    menu.setSelectedElement(value);
    return menu;
  }

}
