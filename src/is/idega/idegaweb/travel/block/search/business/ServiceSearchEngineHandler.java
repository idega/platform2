package is.idega.idegaweb.travel.block.search.business;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.core.builder.data.ICPropertyHandler;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author gimmi
 */
public class ServiceSearchEngineHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name,	String stringValue,	IWContext iwc) {
		DropdownMenu menu = new DropdownMenu(name);
		try {
			Collection engines = ((ServiceSearchEngineHome) IDOLookup.getHome(ServiceSearchEngine.class)).findAll();
			if (engines != null) {
				Iterator iter = engines.iterator();
				ServiceSearchEngine engine;
				while (iter.hasNext()) {
					engine = (ServiceSearchEngine) iter.next();
					menu.addMenuElement(engine.getPrimaryKey().toString(), engine.getName());
				}
			}
			if (stringValue != null) {
				menu.setSelectedElement(stringValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return menu;
	}

	public void onUpdate(String[] values, IWContext iwc) {
	}

}
