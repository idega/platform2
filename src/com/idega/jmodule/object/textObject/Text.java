//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.textObject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class Text extends ModuleObject{

private static Text emptyText;
private static Text HTMLbreak;

protected String text;
protected boolean attributeSet;
protected boolean teletype;
protected boolean bold;
protected boolean italic;
protected boolean underline;
public static String FONT_FACE_ARIAL = "Arial, Helvetica, Sans-serif";
public static String FONT_FACE_TIMES = "Times New Roman, Times, serif";
public static String FONT_FACE_COURIER = "Courier New, Courier, mono";
public static String FONT_FACE_GEORGIA = "Georgia, Times New Roman, Times, serif";
public static String FONT_FACE_VERDANA = "Verdana, Arial, Helvetica, sans-serif";
public static String FONT_FACE_GENEVA = "Geneva, Arial, Helvetica, san-serif";

public static String FONT_FACE_STYLE_NORMAL = "normal";
public static String FONT_FACE_STYLE_BOLD = "bold";
public static String FONT_FACE_STYLE_ITALIC = "italic";
public static String FONT_SIZE_7_HTML_1 = "1";
public static String FONT_SIZE_7_STYLE_TAG = "7pt";
public static String FONT_SIZE_10_HTML_2 = "2";
public static String FONT_SIZE_10_STYLE_TAG= "10pt";
public static String FONT_SIZE_12_HTML_3 = "3";
public static String FONT_SIZE_12_STYLE_TAG = "12pt";
public static String FONT_SIZE_14_HTML_4 = "4";
public static String FONT_SIZE_14_STYLE_TAG = "14pt";
public static String FONT_SIZE_16_STYLE_TAG = "16pt";
public static String FONT_SIZE_18_HTML_5 = "5";
public static String FONT_SIZE_18_STYLE_TAG = "18pt";
public static String FONT_SIZE_24_HTML_6 = "6";
public static String FONT_SIZE_24_STYLE_TAG = "24pt";
public static String FONT_SIZE_34_HTML_7 = "7";
public static String FONT_SIZE_34_STYLE_TAG = "34pt";




/**
**Constructor that creates the object with an empty string
**/

public Text(){
	this("");
}

public Text(String text){
	super();
	this.text=text;
	attributeSet=false;
	teletype=false;
	bold=false;
	italic=false;
	underline=false;
}

public Text(String text,boolean bold,boolean italic,boolean underline){
	this(text);
	if(bold){
		this.setBold();
	}
	if(italic){
		this.setItalic();
	}
	if(underline){
		this.setUnderline();
	}
}



public void setAttribute(String name,String value){
	attributeSet=true;
	super.setAttribute(name,value);
}

protected boolean isEnclosedByParagraph(){
	boolean returnBool=false;
	ModuleObject obj = getParentObject();
	while(obj != null){
		if(obj.getClassName().equals("com.idega.jmodule.object.textObject.Paragraph")){
			returnBool = true;
		}
		obj=obj.getParentObject();
	}
	return returnBool;
}

public void setFontSize(String s){
	setAttribute("size",s);
}

public void setFontSize(int i){
	setFontSize(Integer.toString(i));
}

public void setFontFace(String s){
	setAttribute("face",s);
}

public void setFontColor(String color){
	setAttribute("color",color);
}

public void setFontStyle(String style){
	setAttribute("style",style);
}

public void addToText(String s){
	text=text+s;
}

public void setText(String s){
	text=s;
}

public void addBreak(){
	addToText("<br/>");
}

public void setTeleType(){
	teletype=true;
}

public void setBold(){
	bold=true;
}

public void setItalic(){
	italic=true;
}

public void setUnderline(){
	underline=true;
}

public String getText(){
	return this.text;
}

public String toString(){
	return this.text;
}

/**
 * returns empty string with fontsize = 1
 */
public static Text emptyString(){
  if (emptyText == null){
    emptyText = new Text("");
    emptyText.setFontSize("1");
  }
  return emptyText;
}

public static Text getBreak(){
  if (HTMLbreak == null){
    HTMLbreak = new Text("<br>");
    HTMLbreak.setFontSize("1");
  }
  return HTMLbreak;
}


private void setDefaultAttributes(ModuleInfo modinfo){

	if ( ! isAttributeSet("size") ){
		setFontSize(modinfo.getDefaultFontSize());
	}
	if ( ! isAttributeSet("face") ){
		setFontFace(modinfo.getDefaultFontFace() );
	}
	if ( ! isAttributeSet("color") ){
		setFontColor(modinfo.getDefaultFontColor());
	}

	if ( ! isAttributeSet("style")){
		setFontStyle(modinfo.getDefaultFontStyle() );
	}

}

  public synchronized Object clone() {
    Text obj = null;
    try {
      obj = (Text)super.clone();

      obj.text = this.text;
      obj.attributeSet = this.attributeSet;
      obj.teletype = this.teletype;
      obj.bold = this.bold;
      obj.italic = this.italic;
      obj.underline = this.underline;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }

public void print(ModuleInfo modinfo)throws Exception{
	initVariables(modinfo);
	setDefaultAttributes(modinfo);
	//if ( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){
			//if (getInterfaceStyle().equals("something")){
			//}
			//else{
			if (bold)
				{print("<b>");}
			if (italic)
				{print("<i>");}
				if (attributeSet){

					print("<font "+getAttributeString()+" >");
					print(text);
					print("</font>");
				}
				else{
					print(text);
				}
			if (bold)
				{print("</b>");}
			if (italic)
				{print("</i>");}
			//}
		}
		else if (getLanguage().equals("WML")){
			/*if (isEnclosedByParagraph()){
				println(text);
			}
			else{
				Paragraph myParagraph = new Paragraph();
				myParagraph.setParentObject(this.getParentObject());
				myParagraph.add(this);
				myParagraph.print(modinfo);
			}*/
				print(text);
		}
	//}
}


}

