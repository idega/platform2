package com.idega.jmodule.object;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Image;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class FrameList extends JModuleObject {

private Table listTable;
private String style = "";

  public FrameList() {
  }

  public void main(ModuleInfo modinfo) {
    getParentPage().setAllMargins(0);
    initializeTable();

    add(listTable);
  }

  private void initializeTable() {
    listTable = new Table();
      listTable.setCellpadding(3);
      listTable.setCellspacing(0);
      listTable.setWidth("100%");
      listTable.setHeight("100%");
  }

  public void setLinkStyle(String style) {
    this.style = style;
  }

  public void setZebraColors(String color1, String color2) {
    listTable.setHorizontalZebraColored(color1,color2);
  }

  public void addToMenu(Class classToAdd, Image displayImage, String displayString, String target) {
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

  public void addToMenu(Class classToAdd, Image displayImage, String target) {
    addToMenu(classToAdd,displayImage,classToAdd.getName(),target);
  }

  public void addToMenu(Class classToAdd, Image displayImage) {
    addToMenu(classToAdd,displayImage,classToAdd.getName(),"_self");
  }

  public void addToMenu(Class classToAdd, String displayString, String target) {
    addToMenu(classToAdd,null,displayString,target);
  }

  public void addToMenu(Class classToAdd, String displayString) {
    addToMenu(classToAdd,displayString,"_self");
  }

  public void addToMenu(Class classToAdd) {
    addToMenu(classToAdd,classToAdd.getName().substring(classToAdd.getName().lastIndexOf(".")+1),"_self");
  }
}