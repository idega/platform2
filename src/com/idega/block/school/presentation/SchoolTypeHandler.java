package com.idega.block.school.presentation;

import java.util.List;

import com.idega.core.builder.data.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author gimmi
 */
public class SchoolTypeHandler implements ICPropertyHandler{
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
