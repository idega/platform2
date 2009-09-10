/*
 * $Id: SupplierManagerHandler.java,v 1.2 2005/08/24 13:01:21 gimmi Exp $
 * Created on 20.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.idega.block.trade.stockroom.business.SupplierManagerBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;


public class SupplierManagerHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		DropdownMenu menu = new DropdownMenu(name);
		
		try {
			// Add support for another datasource
			SupplierManagerBusiness bus = (SupplierManagerBusiness) IBOLookup.getServiceInstance(iwc, SupplierManagerBusiness.class);
			Collection coll = bus.findAllSupplierManagers();
			Iterator iter = coll.iterator();
			Group object;
			while (iter.hasNext()) {
				object = (Group) iter.next();
				menu.addMenuElement(object.getPrimaryKey().toString(), object.getName());
			}
			if (coll == null || coll.isEmpty()) {
				menu.addMenuElement("null", "No SupplierManagers Found");
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
	}}
