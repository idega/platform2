package com.idega.projects.rafteikning.templates;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.idegaweb.template.TemplatePage;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia - iceland
 * @author
 * @version 1.0
 */

public class RafteikingHomePageTemplate extends TemplatePage {

  Table FrameTable;
  Table addInTable;

  public RafteikingHomePageTemplate() {
    FrameTable = new Table(1,2);
    FrameTable.setWidth("100%");
    addInTable = new Table(3,1);
    addInTable.setWidth("100%");
    FrameTable.add(addInTable,1,2);
    FrameTable.setBorder(1);
    addInTable.setBorder(1);
    super.add(FrameTable);
    initTemplate();
  }

  public void initTemplate(){
    this.setMarginHeight(0);
    this.setMarginWidth(0);
    this.setTopMargin(0);
    this.setLeftMargin(0);
    this.setTitle("Rafteikning");


    FrameTable.add(getHeader(),1,1);

  }

  public ModuleObject getHeader(){
    Table HeaderTable = new Table(3,1);
    HeaderTable.setWidth("100%");
    Image header1 = new Image("/pics/template/raflogo.gif", "Rafteikning");

    HeaderTable.add(header1,1,1);
    HeaderTable.setBorder(1);


    return HeaderTable;
  }




  public void add(ModuleObject obj){
    addInTable.add(obj,2,1);
  }

  public void add1(ModuleObject obj){
    addInTable.add(obj,1,1);
  }

  public boolean isAdministrator(ModuleInfo modinfo)throws Exception{
    return true;
  }


}