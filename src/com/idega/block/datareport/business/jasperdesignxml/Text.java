/*
 * Created on 1.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLCDATA;
import com.idega.xml.XMLElement;

/**
 * Title:		Text
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class Text extends XMLElement {

	/**
	 * @param name
	 */
	public Text(String text) {
		super("text");
		this.addContent(new XMLCDATA(text));
	}

}
