package com.idega.projects.golf.member;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;


public class Input{

  private int bodyFontSize = 1;
  private int fontSize = 2;
 // private String  DarkColor = "#336660",LightColor = "#CEDFD0", MiddleColor = "#ADCAB1";
  private String bodyFontColor =  "#336660";
  private String backGroundColor = "#CEDFD0";
  private String styleAttribute = "font-size: 8pt";
  private boolean bodyFontBold = true;

  public void setBodyFontColor(String color){
    this.bodyFontColor = color;
  }
  public void setBodyFontSize(int size){
    this.fontSize = size;
  }
  public void setBodyFontBold(boolean bold){
    this.bodyFontBold = bold;
  }
  public void setStyleAttribute(String style){
    this.styleAttribute = style;
  }
  public Text bodyText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      T.setFontColor(this.bodyFontColor);
      T.setFontSize(this.bodyFontSize);
      if(this.bodyFontBold)
        T.setBold();
    }
    return T;
  }
  public Text bodyText(int i){
    return bodyText(String.valueOf(i));
  }
   protected void setStyle(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute);
  }
}