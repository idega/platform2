package com.idega.block.school.presentation;

import com.idega.presentation.PresentationObject;

/**
 * @author gimmi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolContentItemName extends SchoolContentItem {

	/**
	 * @see com.idega.block.school.presentation.SchoolItem#getObject()
	 */
	protected PresentationObject getObject(){
		String name = super._school.getName();
		return getText(name);
	}

}
