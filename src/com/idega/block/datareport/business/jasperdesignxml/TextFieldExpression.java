/*
 * Created on 31.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLCDATA;
import com.idega.xml.XMLElement;

/**
 * Title:		TextFieldExpression
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class TextFieldExpression extends XMLElement {
	
	private static final String ATTRIBUTE_CLASS = "class"; // (java.lang.Boolean | java.lang.Byte | java.util.Date | java.sql.Timestamp | java.sql.Time | java.lang.Double | java.lang.Float | java.lang.Integer | java.lang.Long | java.lang.Short | java.math.BigDecimal | java.lang.String) "java.lang.String"

	

	public TextFieldExpression() {
		super("textFieldExpression");
	}
	
	public void setClassType(Class c){
		setAttribute(ATTRIBUTE_CLASS,c.getName());
	}
	
	public void setClassType(String c){
		setAttribute(ATTRIBUTE_CLASS,c);
	}
	
	public void addParameter(String prmName){
		XMLCDATA cData = new XMLCDATA("$P{"+prmName+"}");
		this.addContent(cData);
	}
	
	public void addField(String fieldName){
		XMLCDATA cData = new XMLCDATA("$F{"+fieldName+"}");
		this.addContent(cData);
	}
	
	public void addVariable(String var){
		XMLCDATA cData = new XMLCDATA("$V{"+var+"}");
		this.addContent(cData);
	}
	
}
