//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

import java.util.*;
import java.io.*;
import com.idega.jmodule.object.textObject.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class Layer extends ModuleObjectContainer{

public static String RELATIVE = "relative";
public static String ABSOLUTE = "absolute";

String absoluteOrRelative;

public Layer(){
  super();
}


public String getAttributeString(){
  String returnString="";
  for (Enumeration e = getAttributes().keys(); e.hasMoreElements();){

          Object Attribute=e.nextElement();
          String AttributeString = (String) Attribute;
          returnString = returnString + " " + AttributeString + ": "+(String) attributes.get(Attribute)+";";
  }

  return returnString;
}



public void setLeftPosition(int xpos){
  setAttribute("left",Integer.toString(xpos)+"px");
  if(absoluteOrRelative==null) setAttribute("position",ABSOLUTE);
}


public void setTopPosition(int ypos){
  setAttribute("top",Integer.toString(ypos)+"px");
  if(absoluteOrRelative==null) setAttribute("position",ABSOLUTE);
}


public void setVisibility(String visibilityType){
  setAttribute("visibility",visibilityType);
}

public void setWidth(int width){
  setAttribute("width",Integer.toString(width)+"px");
}

public void setHeight(int height){
  setAttribute("height",Integer.toString(height)+"px");
}

public void setOverflow(String overflowType){
  setAttribute("overflow",overflowType);
}

public void setZIndex(int index){
  setAttribute("z-index",Integer.toString(index));
}


public void setBackgroundColor(String backgroundColor){
  setAttribute("background-color",backgroundColor);
  setAttribute("layer-background-color",backgroundColor);
}

/*
public void setBorder(int borderWidth,String borderColor){

}

public void setBorder(int borderWidth){

}


public void setBorderColor(String color){

}
*/

public void setPositionType(String absoluteOrRelative){
  this.absoluteOrRelative = absoluteOrRelative;
  setAttribute("position",absoluteOrRelative);
}

public void setBackgroundImage(String url){
  setAttribute("background-image","url("+url+")");
  setAttribute("layer-background-image","url("+url+")");
}

public void setBackgroundImage(Image image){
  setBackgroundImage(image.getURL());
}


public void print(ModuleInfo modinfo) throws Exception{
  initVariables(modinfo);
  if( doPrint(modinfo)){
          if (getLanguage().equals("HTML")){

                  //if (getInterfaceStyle().equals("something")){
                  //}
                  //else{
                          println("<div id=\""+getID()+"\" style=\""+getAttributeString()+"\" >");

                          super.print(modinfo);

                          println("\n</div>");

          }//end if (getLanguage(...
  }
}


}
