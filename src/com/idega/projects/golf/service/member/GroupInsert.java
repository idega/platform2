//package insert;
package com.idega.projects.golf.service.member;
import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.util.*;
import com.idega.util.text.*;
import java.util.*;
import java.sql.Date;
import java.sql.*;
import java.io.*;

import com.idega.jmodule.object.*;

import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.*;
import com.idega.util.*;
import com.idega.data.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class GroupInsert extends EntityInsert {


  private final String selectGroupsName = "group_";
  private Member member = null;
  private SelectionBox selectGroups;
  private String headerText = "Flokkar";

  private String[] selectGroupsValues = new String[0];

  public GroupInsert(ModuleInfo modinfo, boolean isAdmin) {
      super(modinfo);
      isUpdate = false;
      try {
          Group group = new Group();
          Group[] groupArr = null;

          if(isAdmin)
              groupArr = (Group[]) group.findAll();
          else
              groupArr = (Group[]) group.findAll("select * from group_ where group_type = 'union_group'");
          selectGroups = new SelectionBox(groupArr);
          selectGroups.setHeight(10);
      }
      catch(SQLException e) {
          System.out.println("Villa i GroupInsert(ModuleInfo modinfo) "+e.getMessage());
          e.printStackTrace();
      }
  }

  public GroupInsert(ModuleInfo modinfo, Member mem, boolean isAdmin)throws java.sql.SQLException {
      super(modinfo, mem.getID());
      isUpdate = true;
      member = mem;
      Group group = new Group();
      Group[] groupArr = null;

      if(isAdmin)
          groupArr = (Group[]) group.findAll();
      else
          groupArr = (Group[]) group.findAll("select * from group_ where group_type = 'union_group'");
      selectGroups = new SelectionBox(groupArr);
      selectGroups.setHeight(10);
      groupArr = member.getGroups();
      if(groupArr != null) {
        for(int i = 0; i < groupArr.length; i++) {
            selectGroups.setSelectedElement(String.valueOf(groupArr[i].getID()));
        }
      }
  }

  public boolean areNeetedFieldsEmpty() {
      return false;
  }

  public Vector getNeetedEmptyFields() {
      return this.getEmptyFields();
  }

  public SelectionBox getSelectionGroups() {
      return this.selectGroups;
  }

  public boolean areAllFieldsEmpty() {
      return false;
  }

  public boolean areSomeFieldsEmpty() {
      return false;
  }

  public Vector getEmptyFields() {
      return new Vector();
  }

    public HeaderTable getInputTable(boolean submitButton) {

      Table table = null;
      HeaderTable hTable = new HeaderTable();
      hTable.setHeaderText(headerText);
      if(submitButton) {
          table = new Table(1, 2);
          table.add(new SubmitButton("Vista"), 1, 2);
      }
      else
          table = new Table(1, 1);
      hTable.add(table);
      table.add(selectGroups, 1, 1);



      return hTable;
  }

  public void store(Member mem)throws SQLException, IOException {
      setVariables();
      member = mem;
      if(selectGroupsValues == null)
          return;
      for(int i = 0; i < selectGroupsValues.length; i++) {
          member.addTo(new Group(Integer.parseInt(selectGroupsValues[i])));
      }


  }

  public void store()throws SQLException, IOException {
      setVariables();
      if(selectGroupsValues == null)
          return;

      selectGroupsValues = removeGroupsMemberHas(selectGroupsValues);
      for(int i = 0; i < selectGroupsValues.length; i++) {
          modinfo.getResponse().getWriter().print("<br>"+Integer.parseInt(selectGroupsValues[i]));
          member.addTo(new Group(Integer.parseInt(selectGroupsValues[i])));
      }
  }

  public void setVariables() {
      selectGroupsValues = modinfo.getRequest().getParameterValues(selectGroupsName);
  }

  private String[] removeGroupsMemberHas(String[] values)throws IOException {


      if(member.getID() == -1)
          return values;

      Vector v = new Vector();
      Vector toReturn = new Vector();
      Group[] groupArr = null;

      try {
          groupArr = member.getGroups();
      }
      catch(Exception e) {
          System.out.print("Villa i GroupInsert.removeGroupsMemberHas(String values)"+e.getMessage());
          e.printStackTrace();
      }
      for (int i = 0; i < groupArr.length; i++)
          v.add(String.valueOf(groupArr[i].getID()));

      for (int i = 0; i < values.length; i++)
      {
          if(! v.contains(values[i])) {
              toReturn.add(values[i]);
          }
      }

      return (String[]) toReturn.toArray(new String[0]);
  }


}