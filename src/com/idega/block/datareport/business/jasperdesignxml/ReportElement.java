/*
 * Created on 31.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLElement;

/**
 * Title:		ReportElement
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportElement extends XMLElement {
	
	
	//key NMTOKEN #IMPLIED
	private static final String ATTRIBUTE_POSITION_TYPE = "positionType"; // (Float | FixRelativeToTop | FixRelativeToBottom) "FixRelativeToTop"
	private static final String VALUE_POSITION_TYPE_FLOAT = "Float";
	private static final String VALUE_POSITION_TYPE_FIX_RELATIVE_TO_TOP = "FixRelativeToTop";
	private static final String VALUE_POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM = "FixRelativeToBottom";
	
	private static final String ATTRIBUTE_IS_PRINT_REPEATED_VALUES = "isPrintRepeatedValues"; // (true | false) "true"
	//mode (Opaque | Transparent) #IMPLIED
	private static final String ATTRIBUTE_X = "x"; // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_Y = "y"; // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_WIDTH = "width"; // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_HEIGHT = "height"; // NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_IS_REMOVE_LINE_WHEN_BLANK = "isRemoveLineWhenBlank"; // (true | false) "false"
	private static final String ATTRIBUTE_IS_PRINT_IN_FIRST_WHOLE_BAND = "isPrintInFirstWholeBand"; // (true | false) "false"
	private static final String ATTRIBUTE_IS_PRINT_WHEN_DETAIL_OVERFLOWS = "isPrintWhenDetailOverflows"; // (true | false) "false"
	//printWhenGroupChanges CDATA #IMPLIED
	//forecolor CDATA #IMPLIED
	//backcolor CDATA #IMPLIED

	/**
	 * @param name
	 */
	public ReportElement(int x, int y, int width, int height) {
		super("reportElement");
		setXCoordinate(x);
		setYCoordinate(y);
		setWidth(width);
		setHeight(height);
	}
	
	public void setXCoordinate(int x){
		this.setAttribute(ATTRIBUTE_X,Integer.toString(x));
	}
	
	public void setYCoordinate(int y){
		this.setAttribute(ATTRIBUTE_Y,Integer.toString(y));
	}
	
	public void setWidth(int width){
		this.setAttribute(ATTRIBUTE_WIDTH,Integer.toString(width));
	}
	
	public void setHeight(int height){
		this.setAttribute(ATTRIBUTE_HEIGHT,Integer.toString(height));
	}
	
	
	public void setPositionTypeAsFloat(){
		this.setAttribute(ATTRIBUTE_POSITION_TYPE, VALUE_POSITION_TYPE_FLOAT);
	}
	
	public void setPositionTypeAsFixRelativeToTop(){
		this.setAttribute(ATTRIBUTE_POSITION_TYPE, VALUE_POSITION_TYPE_FIX_RELATIVE_TO_TOP);
	}
	
	public void setPositionTypeAsFixRelativeToBottom(){
		this.setAttribute(ATTRIBUTE_POSITION_TYPE, VALUE_POSITION_TYPE_FIX_RELATIVE_TO_BOTTOM);
	}
	
	public void setIsPrintRepeatedValues(boolean value){
		this.setAttribute(ATTRIBUTE_IS_PRINT_REPEATED_VALUES,Boolean.toString(value));
	}
	
	public void setIsRemoveLineWhenBlank(boolean value){
		this.setAttribute(ATTRIBUTE_IS_REMOVE_LINE_WHEN_BLANK,Boolean.toString(value));
	}
	
	public void setIsPrintInFirstWholeBand(boolean value){
		this.setAttribute(ATTRIBUTE_IS_PRINT_IN_FIRST_WHOLE_BAND,Boolean.toString(value));
	}
	
	public void setIsPrintWhenDetailOverflows(boolean value){
		this.setAttribute(ATTRIBUTE_IS_PRINT_WHEN_DETAIL_OVERFLOWS,Boolean.toString(value));
	}
}