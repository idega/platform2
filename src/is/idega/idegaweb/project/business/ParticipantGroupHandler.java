package is.idega.idegaweb.project.business;

import java.util.Iterator;
import java.util.List;

import com.idega.builder.dynamicpagetrigger.business.DPTTriggerBusinessBean;
import com.idega.builder.dynamicpagetrigger.data.PageTriggerInfo;
import com.idega.builder.handler.PropertyHandler;
import com.idega.core.data.GenericGroup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;


/**
 * @author <a href="gummi@idega.is">Gudmundur Agust Saemundsson</a>
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
      List list = DPTTriggerBusinessBean.getInstance(iwc).getDPTPermissionGroups(((com.idega.builder.dynamicpagetrigger.data.PageTriggerInfoHome)com.idega.data.IDOLookup.getHomeLegacy(PageTriggerInfo.class)).findByPrimaryKeyLegacy(ProjectBusiness.tmpHardcodedPageTriggerInfoId));
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
