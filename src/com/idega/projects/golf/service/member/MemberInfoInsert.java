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
public class MemberInfoInsert extends EntityInsert {


  private MemberInfo eMemberInfo;
  private String inputHandicapName = "MemberInfoInsert_memInfonumber";
  private int memberID = 1;
  private TextInput inputHandicap;
  private String inputHandicapValue;
  private String headerText = "Forgjöf";
  private Text handicap = null;

  public MemberInfoInsert() {
    bUpdate = false;
    this.eMemberInfo = new MemberInfo();
    this.eMemberInfo.setDefaultValues();
    inputHandicap = new TextInput(inputHandicapName);
  }

  public MemberInfoInsert( MemberInfo eMemberInfo)throws java.sql.SQLException {
    bUpdate = true;
    this.eMemberInfo = eMemberInfo;
    this.eMemberInfo.setDefaultValues();
    handicap = formatText(String.valueOf(this.eMemberInfo.getHandicap()));
    handicap.setFontSize(6);
  }

  public MemberInfo getMemberInfo() {
    return this.eMemberInfo;
  }

  public void setMemberId(int id) {
    memberID = id;
  }

  public boolean areNeededFieldsEmpty(ModuleInfo modinfo) {
    return false;
  }

  public Vector getNeededEmptyFields(ModuleInfo modinfo) {
    return new Vector();
  }

  public TextInput getInputHandicap() {
    inputHandicap.setMaxlength(4);
    inputHandicap.setLength(4);
    return inputHandicap;
  }

  public Text getHandicap(){
    return this.handicap;
  }

  public boolean areSomeFieldsEmpty(ModuleInfo modinfo) {
    return (isEmpty(modinfo,inputHandicapName));
  }

  public Vector getEmptyFields() {
    Vector vec = new Vector();
    if (isInvalid(inputHandicapValue)) {
        vec.addElement("Forgjöf");
    }
    return vec;
  }

  public BorderTable getInputTable() {
    BorderTable hTable = new BorderTable();
    if(bUpdate){
      Table table = new Table(1, 1);
      hTable.add(table);
      table.add(getHandicap(),1,1);
    }
    else{
      Table table = new Table(1, 2);
      hTable.add(table);
      table.add(formatText("Forgjöf"), 1, 1);
      table.add(getInputHandicap(), 1, 2);
    }
    return hTable;
  }

  public void store(ModuleInfo modinfo)throws SQLException, IOException {
    setVariables(modinfo);
    if(bUpdate)
        eMemberInfo.update();
    else {
      idegaTimestamp stamp = new idegaTimestamp();
      eMemberInfo.setHistory(stamp.toString()+": Félagi skráður í kerfið");
      eMemberInfo.setMemberId(memberID);
      eMemberInfo.setFirstHandicap(eMemberInfo.getHandicap());
      eMemberInfo.insert();
    }
  }

  public void setVariables(ModuleInfo modinfo) {
    inputHandicapValue = getValue(modinfo,inputHandicapName);
    if (! isInvalid(inputHandicapValue)) {
      this.eMemberInfo.setHandicap(Float.valueOf(inputHandicapValue));
    }
    else
      this.eMemberInfo.setHandicap(Float.valueOf("100"));
  }
}