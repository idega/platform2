package is.idega.idegaweb.campus.presentation.handler;



import is.idega.idegaweb.campus.presentation.TitleIcons;
import is.idega.idegaweb.campus.presentation.CampusFactory;
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

public class CampusFactoryHandler implements PropertyHandler {

	public final static int TABBER = CampusFactory.TABBER;
	public final static int CONTENT = CampusFactory.CONTENT;
	public final static int MENU = CampusFactory.MENU;

  public CampusFactoryHandler() {
  }
  public List getDefaultHandlerTypes() {
    return null;
  }
  public PresentationObject getHandlerObject(String name,String value,IWContext iwc){
    DropdownMenu menu = new DropdownMenu(name);
    menu.addMenuElement("","Select:");
    menu.addMenuElement(String.valueOf(TABBER) ,"TABBER");
    menu.addMenuElement(String.valueOf(CONTENT),"CONTENT");
		menu.addMenuElement(String.valueOf(MENU),"MENU");
    menu.setSelectedElement(value);
    return menu;
  }

}
