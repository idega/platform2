/*
 * Created on 10.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;


import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import com.idega.xml.XMLDocument;
import com.idega.xml.XMLException;
import com.idega.xml.XMLParser;

/**
 * Title:		MethodInvocationParser
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class MethodInvocationParser extends XMLParser {

	/**
	 * 
	 */
	public MethodInvocationParser() {
		super();
	}

	/**
	 * @param verify
	 */
	public MethodInvocationParser(boolean verify) {
		super(verify);
	}
	
	
	/**
	 *
	 */
	public XMLDocument parse(String URI) throws XMLException {
		MethodInvocationDocument xdoc = new MethodInvocationDocument(super.parse(URI));

		return(xdoc);
	}

	/**
	 *
	 */
	public XMLDocument parse(InputStream stream) throws XMLException {
		MethodInvocationDocument xdoc = new MethodInvocationDocument(super.parse(stream));

		return(xdoc);
	}

	/**
	 *
	 */
	public XMLDocument parse(StringReader reader) throws XMLException {
		MethodInvocationDocument xdoc = new MethodInvocationDocument(super.parse(reader));

		return(xdoc);
	}

	/**
	 *
	 */
	public XMLDocument parse(File file) throws XMLException {
		MethodInvocationDocument xdoc = new MethodInvocationDocument(super.parse(file));

		return(xdoc);
	}

}
