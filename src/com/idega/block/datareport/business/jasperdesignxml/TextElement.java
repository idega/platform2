/*
 * Created on 31.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import com.idega.xml.XMLElement;

/**
 * Title:		TextElement
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class TextElement extends XMLElement {

	private static final String ATTRIBUTE_TEXT_ALIGNMENT = "textAlignment"; // (Left | Center | Right | Justified) "Left"
	private static final String VALUE_TEXT_ALIGNMENT_LEFT = "Left";
	private static final String VALUE_TEXT_ALIGNMENT_CENTER = "Center";
	private static final String VALUE_TEXT_ALIGNMENT_RIGHT = "Right";
	private static final String VALUE_TEXT_ALIGNMENT_JUSTIFIED = "Justified";
	
	private static final String ATTRIBUTE_VERTICAL_ALIGNMENT = "verticalAlignment"; // (Top | Middle | Bottom) "Top"
	private static final String VALUE_VERTICAL_ALIGNMENT_TOP ="Top";
	private static final String VALUE_VERTICAL_ALIGNMENT_MIDDLE = "Middle";
	private static final String VALUE_VERTICAL_ALIGNMENT_BOTTOM = "Bottom";
	
	private static final String ATTRIBUTE_LINE_SPACING = "lineSpacing"; // (Single | 1_1_2 | Double) "Single"
	private static final String VALUE_LINE_SPACING_SINGLE = "Single";
	private static final String VALUE_LINE_SPACING_ONE_AND_A_HALF = "1_1_2";
	private static final String VALUE_LINE_SPACING_DOUBLE = "Double";
	
	public TextElement() {
		super("textElement");
	}

	private void setTextAlignment(String align){
		this.setAttribute(ATTRIBUTE_TEXT_ALIGNMENT,align);
	}
	
	private void setVerticalAlignment(String vAlign){
		this.setAttribute(ATTRIBUTE_VERTICAL_ALIGNMENT,vAlign);
	}
	
	private void setLineSpacing(String spacing){
		this.setAttribute(ATTRIBUTE_LINE_SPACING,spacing);
	}
	
	public void setTextAlignmentAsLeft(){
		this.setTextAlignment(VALUE_TEXT_ALIGNMENT_LEFT);
	}
	
	public void setTextAlignmentAsCenter(){
		this.setTextAlignment(VALUE_TEXT_ALIGNMENT_CENTER);
	}
	
	public void setTextAlignmentAsRight(){
		this.setTextAlignment(VALUE_TEXT_ALIGNMENT_RIGHT);
	}
	
	public void setTextAlignmentAsJustified(){
		this.setTextAlignment(VALUE_TEXT_ALIGNMENT_JUSTIFIED);
	}
	
	public void setVerticalAlignmentAsTop(){
		this.setVerticalAlignment(VALUE_VERTICAL_ALIGNMENT_TOP);
	}
	
	public void setVerticalAlignmentAsMiddle(){
		this.setVerticalAlignment(VALUE_VERTICAL_ALIGNMENT_MIDDLE);
	}
	
	public void setVerticalAlignmentAsBottom(){
		this.setVerticalAlignment(VALUE_VERTICAL_ALIGNMENT_BOTTOM);
	}
	
	public void setLineSpacingAsSingle(){
		this.setLineSpacing(VALUE_LINE_SPACING_SINGLE);
	}
	
	public void setLineSpacingAsOneAndAHalf(){
		this.setLineSpacing(VALUE_LINE_SPACING_ONE_AND_A_HALF);
	}
	
	public void setLineSpacingAsDouble(){
		this.setLineSpacing(VALUE_LINE_SPACING_DOUBLE);
	}

}
