/*
 * Created on 1.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLElement;

/**
 * Title:		Font
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class Font extends XMLElement {
	
//	reportFont NMTOKEN #IMPLIED
//	fontName CDATA #IMPLIED
//	size NMTOKEN #IMPLIED
//	isBold (true | false) #IMPLIED
//	isItalic (true | false) #IMPLIED
	private static final String ATTRIBUTE_IS_UNDERLINE = "isUnderline"; // (true | false) #IMPLIED
//	isStrikeThrough (true | false) #IMPLIED
//	pdfFontName CDATA #IMPLIED
//	pdfEncoding CDATA #IMPLIED
//	isPdfEmbedded (true | false) #IMPLIED

	
	public Font() {
		super("font");
	}
	
	public void setIsUnderline(boolean value){
		this.setAttribute(ATTRIBUTE_IS_UNDERLINE, String.valueOf(value));
	}

}
