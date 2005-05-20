/*
 * $Id: SupplierBrowserPluginHandler.java,v 1.1 2005/05/20 18:17:50 gimmi Exp $
 * Created on 20.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.core.component.data.ICObject;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;


public class SupplierBrowserPluginHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		DropdownMenu menu = new DropdownMenu(name);
		
		IWBundle bundle = iwc.getIWMainApplication().getBundle(TravelBlock.IW_BUNDLE_IDENTIFIER);
		try {
			Collection ICObjectList = bundle.getICObjectsList(SupplierBrowserPlugin.OBJECT_NAME);
			Iterator iter = ICObjectList.iterator();
			ICObject object;
			while (iter.hasNext()) {
				object = (ICObject) iter.next();
				menu.addMenuElement(object.getClassName(), object.getName());
			}
			if (ICObjectList == null || ICObjectList.isEmpty()) {
				menu.addMenuElement("null", "No Plugins Found");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (stringValue != null) {
			menu.setSelectedElement(stringValue);
		}
		return menu;
	}

	public void onUpdate(String[] values, IWContext iwc) {
	}
}
