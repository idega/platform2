
package com.idega.projects.golf.service;

import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import java.util.*;
import java.sql.*;
import java.io.*;

import com.idega.jmodule.object.*;
import com.idega.util.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  Ægir
 * @version 1.0
 */


public class GroupInsertWindow extends com.idega.jmodule.object.interfaceobject.Window{

  private Union union;
  private Group group;
  private TextInput inputGroup;
  private String headerText = "Setja í flokk";
  private final String inputGroupName = "insert_group";

  public GroupInsertWindow(Union uni)throws java.sql.SQLException {
      union = uni;
      group = new Group();
      inputGroup = new TextInput(inputGroupName);
      setTitle("Nýskrá flokk");
      //inputGroup.setAsNotEmpty("Nafn á flokk vantar");
  }

  public TextInput getInputGroup() {
      return this.inputGroup;
  }

  public void main(ModuleInfo modinfo)throws Exception  {
      this.empty();
      add(getInputTable(modinfo));
  }

  public Form getInputTable(ModuleInfo modinfo)throws Exception {
      Form form = new Form();
      try {

          //form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=save");
          form.add(new Parameter("cmd","save"));
          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);
          Table table = new Table(2, 2);
          table.mergeCells(1, 1, 2, 1);
          table.add(new SubmitButton("Vista"), 2, 2);
          table.add(new CloseButton("Loka"), 1, 2);
          hTable.add(table);
          table.add(getInputGroup(), 1, 1);
          if(modinfo.getRequest().getParameter("cmd") != null) {
              String name = modinfo.getRequest().getParameter(inputGroupName);
              if(name == null || name.equals("")) {
                  modinfo.getWriter().print("Nafn á flokk vantar");
                  //form.add(new BackButton("Til baka"));
              }
              else {
                  modinfo.getWriter().print("Takk");
                  store(modinfo);
                  setParentToReload();
                  close();
              }
          }
          form.add(hTable);
      }
      catch(Exception e) {
          e.printStackTrace(modinfo.getWriter());

      }
      return form;
  }

  public void store(ModuleInfo modinfo)throws SQLException, IOException {
      String name = modinfo.getParameter(inputGroupName);
      if((name != null) && (! name.equals("")) && (union.getID() != -1)) {
          group.setName(name);
          group.setGroupType("union_group");
          group.insert();
          group.addTo(union);
      }
  }



}