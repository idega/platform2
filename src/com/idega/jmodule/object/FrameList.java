package com.idega.jmodule.object;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Text;

/**
 * Title: FrameList
 * Description: An addable list that extends JModuleObject, can be inserted into pages or frames.
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

public class FrameList extends JModuleObject {

private Table listTable;
private String style = "";

  public FrameList() {
  }

  public void _main(ModuleInfo modinfo) {
    getParentPage().setAllMargins(0);
    initializeTable();

    add(listTable);
  }

  public void main(ModuleInfo modinfo) {
  }

  private void initializeTable() {
    listTable = new Table();
      listTable.setCellpadding(3);
      listTable.setCellspacing(0);
      listTable.setWidth("100%");
      listTable.setHeight("100%");
  }

  public void addToList(ModuleObject obj, Image displayImage) {
    int rows = listTable.getRows();
      if ( !listTable.isEmpty(1,rows) ) {
        rows++;
      }

    if ( displayImage != null ) {
      listTable.add(displayImage,1,rows);
      listTable.add(obj,2,rows);
    }
    else {
      listTable.add(obj,1,rows);
    }
  }

  public void addToList(ModuleObject obj) {
    addToList(obj,null);
  }

  public void addToList(String displayString) {
    addToList(new Text(displayString),null);
  }

  public void addToList(Class classToAdd, Image displayImage, String displayString, String target) {
    Link link = new Link(displayString,classToAdd);
      link.setTarget(target);
      link.setFontStyle(style);

    int rows = listTable.getRows();
      if ( !listTable.isEmpty(1,rows) ) {
        rows++;
      }

    if ( displayImage != null ) {
      listTable.add(displayImage,1,rows);
      listTable.add(link,2,rows);
    }
    else {
      listTable.add(link,1,rows);
    }
  }

  public void addToList(Class classToAdd, Image displayImage, String target) {
    addToList(classToAdd,displayImage,classToAdd.getName(),target);
  }

  public void addToList(Class classToAdd, Image displayImage) {
    addToList(classToAdd,displayImage,classToAdd.getName(),"_self");
  }

  public void addToList(Class classToAdd, String displayString, String target) {
    addToList(classToAdd,null,displayString,target);
  }

  public void addToList(Class classToAdd, String displayString) {
    addToList(classToAdd,displayString,"_self");
  }

  public void addToList(Class classToAdd) {
    addToList(classToAdd,classToAdd.getName().substring(classToAdd.getName().lastIndexOf(".")+1),"_self");
  }

  public void setLinkStyle(String style) {
    this.style = style;
  }

  public void setListWidth(String width) {
    listTable.setWidth(width);
  }

  public void setListWidth(int width) {
    setListWidth(Integer.toString(width));
  }

  public void setListHeight(String height) {
    listTable.setHeight(height);
  }

  public void setListHeight(int height) {
    setListHeight(Integer.toString(height));
  }

  public void setZebraColors(String color1, String color2) {
    listTable.setHorizontalZebraColored(color1,color2);
  }

  public void setListpadding(int padding) {
    listTable.setCellpadding(padding);
  }

  public void setListSpacing(int spacing) {
    listTable.setCellspacing(spacing);
  }
}