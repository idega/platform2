//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.RadioButton;
import com.idega.data.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class RadioGroup extends InterfaceObjectContainer{

Vector buttons;
Vector texts;
Table frameTable;
int rowIndex = 1;
int columnIndex = 1;
int rows = 0;
int columns = 0;
boolean fillVertical = true;
String name;

private RadioGroup(){
  buttons = new Vector(0);
  texts = new Vector(0);
  frameTable = new Table();
  add(frameTable);
}

public RadioGroup(String name){
  this();
  this.name = name;
}

public RadioGroup(GenericEntity[] entity){
  this();
  if (entity != null){
    int length=entity.length;
    for (int i = 0; i < length;i++){
      buttons.add(new RadioButton(entity[i].getEntityName(),Integer.toString(entity[i].getID())));
      Text temp = new Text(entity[i].getName());
      temp.setFontSize(1);
      texts.add(temp);
    }
  }
}

public void keepStatusOnAction(){
  if (buttons != null){
    for(int i=0;i<buttons.size();i++){
      ((RadioButton)buttons.get(i)).keepStatusOnAction();
    }
  }
}

public void setVertical(boolean value){
  this.fillVertical = value;
}


public void addRadioButton(String value, Text DisplayString){
  buttons.add(new RadioButton(name,value));
  texts.add(DisplayString);
}

public void addRadioButton(int value,Text DisplayString){
  buttons.add(new RadioButton(name,Integer.toString(value)));
  texts.add(DisplayString);
}

public void addRadioButton(String value){
  buttons.add(new RadioButton(name,value));
  texts.add(new Text(value));
}

public void addRadioButton(RadioButton radioButton, Text DisplayText){
    buttons.add(radioButton);
    texts.add(DisplayText);
}

public void addRadioButton(RadioButton[] radioButtons, Text[] DisplayTexts){
  for (int i = 0; i < radioButtons.length; i++) {
    buttons.add(radioButtons[i]);
    texts.add(DisplayTexts[i]);
  }
}

private void heightenIndexes(){
  if(fillVertical){
    if(rows > 0){
      if (rowIndex%rows==0){
        rowIndex = 0;
        columnIndex++;
      }
    }
    rowIndex++;
  }else{
    if(columns > 0){
      if (columnIndex%columns==0){
        columnIndex = 0;
        rowIndex++;
      }
    }
    columnIndex++;
  }
}

public void setHeight(int height){
  this.rows = height;
}

public void setWidth(int width){
  this.columns = width;
}

public void main(ModuleInfo modinfo) throws Exception {
  if( columns > 0 && rows > 0){
    frameTable.resize(columns,rows);
    for (int i = 0; i < buttons.size(); i++) {
      if(!(rowIndex > rows) && !(columnIndex > columns)){
        frameTable.add((RadioButton)buttons.get(i),columnIndex*2-1,rowIndex);
        frameTable.add((Text)texts.get(i),columnIndex*2,rowIndex);
        heightenIndexes();
      } else {
        System.err.println("too many Radiobuttons for table cells");
        break;
      }
    }
  }else if(columns > 0){
    frameTable.resize(columns,(int)Math.ceil(buttons.size()/columns));
    for (int i = 0; i < buttons.size(); i++) {
        frameTable.add((RadioButton)buttons.get(i),columnIndex*2-1,rowIndex);
        frameTable.add((Text)texts.get(i),columnIndex*2,rowIndex);
        heightenIndexes();
    }
  }else if(rows > 0){
    frameTable.resize((int)Math.ceil(buttons.size()/rows),rows);
    for (int i = 0; i < buttons.size(); i++) {
        frameTable.add((RadioButton)buttons.get(i),columnIndex*2-1,rowIndex);
        frameTable.add((Text)texts.get(i),columnIndex*2,rowIndex);
        heightenIndexes();
    }
  }else{  // columnIndex == 0 && rowIndex == 0
    frameTable.resize(1,buttons.size());
    for (int i = 0; i < buttons.size(); i++) {
        frameTable.add((RadioButton)buttons.get(i),columnIndex*2-1,rowIndex);
        frameTable.add((Text)texts.get(i),columnIndex*2,rowIndex);
        heightenIndexes();
    }
  }

}


public void setBorder(int i){
  frameTable.setBorder(i);
}

} // end Class

