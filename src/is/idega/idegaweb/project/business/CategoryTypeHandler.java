package is.idega.idegaweb.project.business;

import com.idega.builder.handler.PropertyHandler;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.data.IPCategoryType;

import java.util.Iterator;
import java.util.List;
import java.sql.SQLException;


/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class CategoryTypeHandler implements PropertyHandler {

  public CategoryTypeHandler() {
  }
  public List getDefaultHandlerTypes() {
    return null;
  }

  public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
    DropdownMenu menu = new DropdownMenu(name);

      //menu.addMenuElement("","Select");
      try {
        List list = ProjectBusiness.getInstance().getCategoryTypes();
        if(list != null){
          Iterator iter = list.iterator();
          while (iter.hasNext()) {
            IPCategoryType item = (IPCategoryType)iter.next();
            menu.addMenuElement(item.getID(),item.getName());
          }
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      menu.setSelectedElement(stringValue);
    return menu;
  }
}