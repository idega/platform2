package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import java.util.*;
import java.sql.*;
import java.io.*;

import com.idega.presentation.*;
import com.idega.util.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class GroupMemberInsertWindow extends com.idega.presentation.ui.Window{

  private Member member = null;
  private Union union = null;
  private SelectionBox selectGroups;
  private String headerText = "Setja í flokk";

  private String[] selectGroupsValues = new String[0];

  public GroupMemberInsertWindow(Member mem, Union uni, boolean isAdmin)throws java.sql.SQLException {
      construct(mem, uni, isAdmin);
      setTitle("Setja í flokk");
  }

  public SelectionBox getSelectionGroups() {
      return this.selectGroups;
  }

  public void main(IWContext iwc) {
      this.empty();
      add(getInputTable(iwc));
  }

  public Form getInputTable(IWContext iwc){
      Form form = new Form();
      try {

          form.setAction(iwc.getRequest().getRequestURI()+"?cmd=save");
          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);
          Table table = new Table(2, 2);
          table.mergeCells(1, 1, 2, 1);
          table.add(new SubmitButton("Vista"), 2, 2);
          table.add(new CloseButton("Loka"), 1, 2);
          hTable.add(table);
          table.add(selectGroups, 1, 1);
          if(iwc.getRequest().getParameter("cmd") != null) {
              store(iwc);
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

  public void store(IWContext iwc)throws SQLException, IOException {
      selectGroupsValues = iwc.getRequest().getParameterValues(new Group().getEntityName());

      selectGroupsValues = removeFrom(selectGroupsValues);
      for(int i = 0; i < selectGroupsValues.length; i++) {
          member.addTo(new Group(Integer.parseInt(selectGroupsValues[i])));
      }
  }

  private void construct(Member mem, Union uni, boolean isAdmin) {
      try
      {
          member = mem;
          union = uni;
          Group group = new Group();
          Group[] groupArr = null;

          if(isAdmin)
              groupArr = (Group[]) group.findAll();
          else {
              //groupArr = (Group[]) group.findAll("select * from group_ where group_type = 'union_group' and group_.group_type not like 'accesscontrol'");
              //select * from group_, union_, union_group where union_.union_id = union_group.union_id and union_.union_id = 3 or union_.union_id = 81
              groupArr = getGroupArray(union.getUnionGroupsRecursive());
          }

          selectGroups = new SelectionBox(groupArr);
          selectGroups.setHeight(10);
          groupArr = member.getGroups();
          if(groupArr != null) {
            for(int i = 0; i < groupArr.length; i++) {
                selectGroups.setSelectedElement(String.valueOf(groupArr[i].getID()));
            }
          }
      }
      catch(Exception e ) {
          e.printStackTrace();
      }
  }

  public String[] removeFrom(String[] arr)throws SQLException, IOException {
      Vector vToArr = new Vector();
      Vector vCompare = new Vector();

      Group[] groupArr = member.getGroups();

      for (int i = 0; i < groupArr.length; i++) {
          vCompare.add(""+groupArr[i].getID());
      }

      for (int i = 0; i < arr.length; i++) {
          vToArr.add(arr[i]);
      }

      vToArr.removeAll(vCompare);

      return (String[]) vToArr.toArray(new String[0]);
  }

  public Group[] getGroupArray(List list) {
      Group[] groupArray = new Group[list.size()];
      for (int i = 0; i < groupArray.length; i++) {
          groupArray[i] = (Group) list.get(i);
      }
      return groupArray;
  }



}