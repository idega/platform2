package is.idega.idegaweb.project.business;

import com.idega.builder.handler.PropertyHandler;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.builder.dynamicpagetrigger.business.DPTTriggerBusiness;
import com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo;
import is.idega.idegaweb.project.data.IPCategoryType;
import java.util.Iterator;
import java.util.List;
import java.sql.SQLException;
import com.idega.core.data.GenericGroup;


/**
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ParticipantGroupHandler implements PropertyHandler {
  /**
   *
   */
  public ParticipantGroupHandler() {
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
  public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
    DropdownMenu menu = new DropdownMenu(name);
    menu.addMenuElement(-1,"Select");

    try {
      /**
       * @todo link to project app (hardcoded dpt_id 1)
       */
      List list = DPTTriggerBusiness.getInstance().getDPTPermissionGroups(new PageTriggerInfo(ProjectBusiness.tmpHardcodedPageTriggerInfoId));
      if (list != null) {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
          GenericGroup item = (GenericGroup)iter.next();
          menu.addMenuElement(item.getID(),item.getName());
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    menu.setSelectedElement(stringValue);

    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}