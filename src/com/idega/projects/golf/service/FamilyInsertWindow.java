package com.idega.projects.golf.service;

import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.util.*;
import com.idega.util.text.Name;
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
 * Company:      idega
 * @author       Ægir
 * @version 1.0
 */

public class FamilyInsertWindow extends com.idega.jmodule.object.interfaceobject.Window{

  private static final String NAME = "1";
  private static final String NAME_AND_MIDDLE = "2";
  private static final String SOSIAL_SEC_NUM = "3";
  private static final String ALL = "4";
  private final String SUBMIT_PARAM_NAME = "Cmd";
  private final String STORE = "store";
  private final String FIND = "find";

  private UnionMemberInfo uniMemInfo = null;
  private Family family;
  private DropdownMenu selectFamily;
  private String selectionFamilyName = "familyBox";
  private String[] selectionFamilyValues;
  private String headerText = "Fjölskylda";
  private int unionId;

  private TextInput inputFind;
  private DropdownMenu choise;

  private String findValue;
  private String findName = "familyFinder";


  private String choiseName = "";
  private String choiseValue;


  private String[] selectGroupsValues = new String[0];

  public FamilyInsertWindow(int memberId, int unionId)throws java.sql.SQLException {

      setTitle("Finna fjölskyldu");
      this.unionId = unionId;
      inputFind = new TextInput(findName);
      inputFind.keepStatusOnAction();
      choise = getChoises(choiseName);
      choise.keepStatusOnAction();
      Member mem = new Member();
      uniMemInfo  = new Member(memberId).getUnionMemberInfo(unionId);
  }

  public void main(ModuleInfo modinfo) {
      this.empty();
      add(getInputTable(modinfo));
  }

  private void setVariables(ModuleInfo modinfo) {
      choiseValue = modinfo.getRequest().getParameter(choiseName);
      findValue = getValue(findName, modinfo);

      if(findValue != null && (! findValue.equals(""))) {
          //member.setFamilyId(Integer.parseInt(findValue));
      }

  }

  public Form getInputTable(ModuleInfo modinfo){
      Form form = new Form();
      try {


          String strCommand = modinfo.getRequest().getParameter(SUBMIT_PARAM_NAME);



          HeaderTable hTable = new HeaderTable();
          hTable.setHeaderText(headerText);

          Table table = new Table(1, 6);
          table.mergeCells(1, 1, 2, 1);
          table.add(choise, 1, 3);
          table.add(inputFind, 1, 3);
          table.add(new SubmitButton("Leita", SUBMIT_PARAM_NAME, FIND), 1, 4);
          table.add(new CloseButton("Loka"), 1, 6);
          table.add(new SubmitButton("Skrá", SUBMIT_PARAM_NAME, STORE), 1, 6);

          if((strCommand != null) && (strCommand.equals(STORE))) {

              store(modinfo);
              close();
              setParentToReload();
          }
          else {
              if((strCommand != null) && (strCommand.equals(FIND))) {
                  setVariables(modinfo);
                  int numRecords = 0;
                  List l = find(findValue, choiseValue);

                  System.err.println("\n\nVilla i getInputTable\n\n"+findValue+" hitttt "+ choiseValue);
                  //List l = find("Agnar Ármannsson", "1");
                  setSelectionBox(l);


                  if(l != null) {
                     numRecords = l.size();
                  }
                  table.add("Fjöldi svara: "+numRecords, 1, 1);
                  table.add(selectFamily, 1, 5);
              }
          }
          hTable.add(table);
          form.add(hTable);
      }
      catch(Exception e) {
          System.err.println("\n\nVilla i getInputTable\n\n");
          e.printStackTrace();
      }
      return form;
  }

  public void store(ModuleInfo modinfo)throws SQLException, IOException {
      selectionFamilyValues = modinfo.getRequest().getParameterValues(selectionFamilyName);

      if(selectionFamilyValues != null && selectionFamilyValues.length > 0) {
          System.err.println("\n\nselectionFamilyValues: "+selectionFamilyValues[0]);

          uniMemInfo.setFamilyId(Integer.parseInt(selectionFamilyValues[0]));
          uniMemInfo.update();
      }


  }

  public String getValue(String attribute, ModuleInfo modinfo) {
      return modinfo.getParameter(attribute);
  }

  private DropdownMenu getChoises(String name) {
      DropdownMenu drp = new DropdownMenu(name);
      drp.addMenuElement(NAME, "Fornafn/Fullt nafn");
      drp.addMenuElement(NAME_AND_MIDDLE, "Fornafn og millinafn");
      drp.addMenuElement(SOSIAL_SEC_NUM, "Kennitala");
      drp.addMenuElement(ALL, "Allir");
      return drp;
  }

  private List find(String toFind, String cmd) {
      toFind = toFind.replace('*', '%');
      Name name = new Name(toFind);

      List list = null;

      System.err.println("\n\n nafn \n\n"+name.getFirstName()+name.getMiddleName()+name.getLastName());

      try {
          if ((name.getFirstName().equals("")) && (! cmd.equals(ALL))) {
              return list;
          }

          if(cmd.equals(SOSIAL_SEC_NUM)) {
              //System.err.println("\n\n kennitala \n\n");
              list = EntityFinder.findAll(new UnionMemberInfo(), "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.SOCIAL_SECURITY_NUMBER like '"+name.getFirstName()+"'");
          }
          else if(cmd.equals(ALL)) {
              list = EntityFinder.findAll(new UnionMemberInfo(), "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" order by member.first_name");
          }

          else if (! name.getMiddleName().equals("")) {
              if (cmd.equals(this.NAME)) {
                  //System.err.println("\n\n öll  \n\n");
                  //System.err.println(" \n\n SQL ER: select member.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.first_name = "+name.getFirstName()+" and member.middle_name = "+name.getMiddleName()+" and member.last_name = "+name.getLastName());
                  list = EntityFinder.findAll(new UnionMemberInfo(), "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.first_name like '"+name.getFirstName()+"' and member.middle_name like '"+name.getMiddleName()+"' and member.last_name like '"+name.getLastName()+"'");
              }
              else { // if (cmd.equals(this.NAME_AND_MIDDLE))
                  //System.err.println("\n\n nafn og millinafn \n\n");
                  list = EntityFinder.findAll(new UnionMemberInfo(), "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.first_name like '"+name.getFirstName()+"' and member.middle_name like '"+name.getMiddleName()+"'");
              }
          }
          else if (! name.getLastName().equals("")) {
              //System.err.println("\n\n milli og endanafn \n\n");
              list = EntityFinder.findAll(new UnionMemberInfo(), "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.first_name like '"+name.getFirstName()+"' and member.last_name like '"+name.getLastName()+"'");
          }
          else {
              //System.err.println("\n\n 1 nafn \n\n");
              System.err.println(" \n\n SQL ER: select member.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.first_name = '"+name.getFirstName()+"'");
              list = EntityFinder.findAll(new UnionMemberInfo(), "select union_member_info.* from member, union_member_info where member.member_id = union_member_info.member_id and union_member_info.union_id = "+unionId+" and member.first_name like '"+name.getFirstName()+"'");

          }
      }
      catch(Exception e) {
          System.err.println("Villa i FamilyWinow "+e.getMessage());
          e.printStackTrace();
          return list;
      }

      return list;
  }

  private void setSelectionBox(List list)throws Exception {
      selectFamily = new DropdownMenu(selectionFamilyName);
      //selectFamily.setHeight(8);
      selectFamily.setAttribute("size", "8");
      Member mem = null;
      UnionMemberInfo uni = null;


      if((list == null) || list.isEmpty()) {
          selectFamily.addMenuElement("", "Ekkert fannst ...");
      }
      else if(list != null) {
          for (int i = 0; i < list.size(); i++) {
              uni = (UnionMemberInfo) list.get(i);
              mem = new Member(uni.getMemberID());
              if( mem!=null){
                  selectFamily.addMenuElement(uni.getFamilyId(), mem.getName());
              }
          }
      }
  }

}