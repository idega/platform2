package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.builder.handler.PropertyHandler;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SelectionBox;

/**
 * @author gimmi
 */
public class SchoolTypeHandler implements PropertyHandler{
	public SchoolTypeHandler() {
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
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		DropdownMenu obj = new DropdownMenu(name);
		obj.addMenuElement("SCHOOL","SCHOOL");
		obj.addMenuElement("CHILDCARE","CHILDCARE");
		obj.setSelectedElement(value);
		return obj;
	}

	/**
	 *
	 */
	public void onUpdate(String values[], IWContext iwc) {
	}

}
