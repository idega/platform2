package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.util.*;
import com.idega.util.text.Name;
import com.idega.util.text.*;
import java.util.*;
import java.sql.Date;
import java.sql.*;
import java.io.*;

import com.idega.presentation.*;

import com.idega.presentation.text.*;
import is.idega.idegaweb.golf.*;
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

public class FamilyInsertWindow extends com.idega.presentation.ui.Window{

  private static final String NAME = "1";
  private static final String NAME_AND_MIDDLE = "2";
  private static final String SOSIAL_SEC_NUM = "3";
  private static final String ALL = "4";
  private final String STORE_NAME = "STOREFAMILY";
  private final String FIND_NAME = "FINDFAMILY";
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


  private String choiseName = "keyToSearch";
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

  public void main(IWContext iwc) {
      this.empty();
      add(getInputTable(iwc));
  }

  private void setVariables(IWContext iwc) {
      choiseValue = iwc.getRequest().getParameter(choiseName);
      findValue = getValue(findName, iwc);

      if(findValue != null && (! findValue.equals(""))) {
          //member.setFamilyId(Integer.parseInt(findValue));
      }

  }

  public Form getInputTable(IWContext iwc){
      Form form = new Form();
      form.setMethod("get");
      try {


        String strStore = iwc.getRequest().getParameter(STORE_NAME+".x");
        String strFind = iwc.getRequest().getParameter(FIND_NAME+".x");

        BorderTable hTable = new BorderTable();

        Table table = new Table(1, 6);
        table.mergeCells(1, 1, 2, 1);
        table.add(choise, 1, 3);
        table.add(inputFind, 1, 3);

        Table buttonTable = new Table(2,1);
        buttonTable.setCellpadding(0);
        buttonTable.setCellspacing(0);


        table.add(new SubmitButton(new Image("/pics/formtakks/finna.gif"), FIND_NAME, "findval"), 1, 4);

        buttonTable.add(new SubmitButton(new Image("/pics/formtakks/skra.gif"), STORE_NAME, "storeval"), 1, 1);
        buttonTable.add(new CloseButton(new Image("/pics/formtakks/loka.gif")), 2, 1);

        table.add(buttonTable, 1, 6);
        if(strStore != null) {
          store(iwc);
          close();
          setParentToReload();
        }
        else {
          if(strFind != null) {
            setVariables(iwc);
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


  private void store(IWContext iwc) {
      selectionFamilyValues = iwc.getRequest().getParameterValues(selectionFamilyName);
      String familyId = selectionFamilyValues[0];
      int nOldFamilyID = this.uniMemInfo.getFamilyId();

      try {
          if(selectionFamilyValues != null && selectionFamilyValues.length > 0) {
          System.err.println("\n\nNýtt Fjölskyldu id: "+familyId);
          System.err.println("Member id: "+this.uniMemInfo.getMemberID()+"\n");
          System.err.println("Union id: "+this.uniMemInfo.getUnionID()+"\n");
          System.err.println("Gamalt Fjölskyldu id: "+nOldFamilyID+"\n");

            if(! familyId.equals("")) {
                uniMemInfo.setFamilyId(new Integer(familyId));
                uniMemInfo.update();
            }
          }
          System.err.println(" Fjölskyldu id nu: "+nOldFamilyID+"\n");
      }
      catch(SQLException e) {
          System.err.println("Villa í store!! ... !!\n");
          System.err.println(e.getMessage());
      }
  }

  public String getValue(String attribute, IWContext iwc) {
      return iwc.getParameter(attribute);
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
        selectFamily.addMenuElement("", "Leit bar ekki árangur");
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
