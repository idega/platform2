package com.idega.jmodule.object.app;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleInfo;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.core.data.ICObject;

import java.util.List;
import java.util.Iterator;

public class IWControlCenter extends ModuleObjectContainer {

  int windowWidth=300;
  int windowHeight=200;
  int headerHeight=25;
  int border = 3;
  String windowBorder = "gray";
  String backgroundColor = "#D4D0C8";
  String headerColor = backgroundColor;
  String darkerColor = "gray";
  String bodyColor = "white";

  public IWControlCenter() {

  }

  public void main(ModuleInfo modinfo){

    Table outerWindow = new Table(1,2);
    outerWindow.setWidth(windowWidth);
    outerWindow.setHeight(windowHeight);
    outerWindow.setAlignment("center");
    outerWindow.setVerticalAlignment("middle");
    add(outerWindow);

    outerWindow.setCellspacing(border);
    outerWindow.setColor(windowBorder);
    outerWindow.setAlignment(1,2,"center");
    outerWindow.setVerticalAlignment(1,2,"middle");

    Table header = new Table();
    header.setWidth("100%");
    header.setHeight(headerHeight);

    outerWindow.add(header,1,1);
    header.setColor(headerColor);
    Text headerText = new Text("idegaWeb ApplicationSuite");
    headerText.setFontSize(1);
    headerText.setFontColor("black");
    header.add(headerText);

    Table body = new Table();
    outerWindow.add(body,1,2);
    body.setWidth("100%");
    body.setHeight(windowHeight-headerHeight);
    body.setColor(bodyColor);


    List icoList = IWApplication.getApplictionICObjects();
    if(icoList!=null){
      int x =1;
      int y =1;
      Iterator iter = icoList.iterator();
      while (iter.hasNext()) {
        ICObject item = (ICObject)iter.next();
        Class c = null;
        try{
          c = item.getObjectClass();
        }
        catch(Exception e){
        }

        ModuleObject icon = IWApplication.getIWApplicationIcon(c,modinfo);
        body.setAlignment(x,y,"center");
        body.setVerticalAlignment(x,y,"middle");
        body.add(icon,x,y);
        if(x==1){
          x=2;
        }
        else{
          x=1;
          y++;
        }
      }
    }

  }
}