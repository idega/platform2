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
 * @author        Ægir
 * @version 1.0
 */



public class GroupMemberRemoveWindow extends com.idega.jmodule.object.interfaceobject.Window{

  private Member member = null;
  private Union union;
  private SelectionBox selectGroups;
  private String headerText = "Færa úr flokki";
  private boolean isAdministrator = false;

  private String[] selectGroupsValues = new String[0];

  public GroupMemberRemoveWindow(Member mem, Union uni, boolean isAdmin)throws java.sql.SQLException {
      construct(mem, uni, isAdmin);
      setTitle("Færa úr flokki");
      setWidth(170);
      this.setHeight(255);
      setScrollbar(false);
  }

  public SelectionBox getSelectionGroups() {
      return this.selectGroups;
  }

  public void main(ModuleInfo modinfo) {
      this.empty();
      add(getInputTable(modinfo));
  }

  public Form getInputTable(ModuleInfo modinfo){
      Form form = new Form();
      try {

          //form.setAction(modinfo.getRequest().getRequestURI()+"?cmd=remove");
          form.add(new Parameter("cmd","remove"));
          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);
          Table table = new Table(2, 2);
          table.mergeCells(1, 1, 2, 1);
          table.add(new CloseButton("Loka"), 1, 2);
          hTable.add(table);
          if(member.getGroups().length > 0) {
              table.add(selectGroups, 1, 1);
              table.add(new SubmitButton("Slíta frá"), 2, 2);
          }
          else {
              table.add("Enginn flokkur skráður", 1, 1);
          }

          if(modinfo.getRequest().getParameter("cmd") != null) {
              remove(modinfo);
              setParentToReload();
              close();
          }
          form.add(hTable);
          }
      catch(Exception e) {
          e.printStackTrace();
      }
      return form;
  }

  public void remove(ModuleInfo modinfo)throws SQLException, IOException {
      selectGroupsValues = modinfo.getRequest().getParameterValues(new Group().getEntityName());

      for(int i = 0; i < selectGroupsValues.length; i++) {
          Group gr = new Group(Integer.parseInt(selectGroupsValues[i]));
          gr.removeFrom(member);
      }
  }

  private void construct(Member mem, Union uni, boolean isAmin) {
      try
      {
          member = mem;
          union = uni;
          isAdministrator = isAmin;
          Group group = new Group();
          Group[] groupArr = null;
          Group[] grArr = null;

          if(isAdministrator) {
              grArr = (Group[]) group.findAll("select group_.* from group_, union_group, group_member where member_id = '"+member.getID()+"' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = 3");
          }
          else {
              grArr = (Group[]) group.findAll("select group_.* from group_, union_group, group_member where member_id = '"+member.getID()+"' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = 3 and group_.group_type not like 'accesscontrol'");
          }

          groupArr = (Group[]) group.findAll("select group_.* from group_, union_group, group_member where member_id = '"+member.getID()+"' and group_.group_id = group_member.group_id and union_group.group_id = group_member.group_id and union_group.union_id = "+union.getID());
          groupArr = joinArrays(grArr, groupArr);

          selectGroups = new SelectionBox(groupArr);
          selectGroups.setHeight(10);
          groupArr = member.getGroups();
      }
      catch(Exception e ) {
          e.printStackTrace();
      }
  }

  public Group[] joinArrays(Group[] p1, Group[] p2) {
      Vector v = new Vector();

      for(int i = 0; i < p1.length; i++) {
          v.add(p1[i]);
      }
      for(int i = 0; i < p2.length; i++) {
          v.add(p2[i]);
      }

      Group[] returnEntity = new Group[v.size()];

      for(int i = 0; i < v.size(); i++) {
          returnEntity[i] = (Group) v.get(i);
      }

      return  returnEntity;
  }
}