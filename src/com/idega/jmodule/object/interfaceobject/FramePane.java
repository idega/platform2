//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/

public class FramePane extends InterfaceObjectContainer{

private Table table;
private static String imagePrefix = "/common/pics/interfaceobject/FramePane/";

public FramePane(){
  this("");
}

public FramePane(String headerText){
  table = new Table(3,3);
  //table.setWidth("200");
  //table.setHeight("200");
  table.setCellpadding(0);
  table.setCellspacing(0);
  table.setAlignment(2,2,"center");
  table.setVerticalAlignment(2,2,"middle");
  super.add(table);
  table.setHeight(1,"24");
  table.setHeight(3,"10");

  table.setWidth(1,"10");
  table.setWidth(3,"10");

  Image topRight=new Image(imagePrefix+"topright.gif");
  table.add(topRight,3,1);
  //table.setBackgroundImage(3,1,topRight);
  Image topLeft=new Image(imagePrefix+"topleft.gif");
  table.add(topLeft,1,1);
  //table.setBackgroundImage(1,1,topLeft);
  Image bottomLeft=new Image(imagePrefix+"bottomleft.gif");
  //table.add(bottomLeft,1,3);
  table.add(Text.emptyString(),1,3);
  table.setBackgroundImage(1,3,bottomLeft);
  Image bottomRight=new Image(imagePrefix+"bottomright.gif");
  //table.add(bottomRight,3,3);
  table.setBackgroundImage(3,3,bottomRight);
  table.add(Text.emptyString(),3,3);

  Image Right=new Image(imagePrefix+"righttiler.gif");
  table.setBackgroundImage(3,2,Right);
  table.add(Text.emptyString(),3,2);
  Image Left=new Image(imagePrefix+"lefttiler.gif");
  table.setBackgroundImage(1,2,Left);
  table.add(Text.emptyString(),1,2);
  Image Bottom=new Image(imagePrefix+"bottomtiler.gif");
  table.setBackgroundImage(2,3,Bottom);
  table.add(Text.emptyString(),2,3);

  Table InnerTable = new Table(2,1);
  InnerTable.setHeight("24");
  InnerTable.setWidth(2,1,"100%");
  InnerTable.setCellpadding(0);
  InnerTable.setCellspacing(0);
  InnerTable.add(headerText,1,1);
  Image Top=new Image(imagePrefix+"toptiler.gif");
  InnerTable.setBackgroundImage(2,1,Top);

  table.add(InnerTable,2,1);


}


public void add(ModuleObject obj){
  table.add(obj,2,2);
}


public void setWidth(int width){
  table.setWidth(width);
}

public void setHeight(int height){
  table.setHeight(height);
}

}