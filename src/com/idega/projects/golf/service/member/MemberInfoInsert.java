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


  private MemberInfo memInfo;
  private String inputHandicapName = "MemberInfoInsert_memInfonumber";
  private int memberID = 1;

  private TextInput inputHandicap;

  private String inputHandicapValue;
  private String headerText = "Forgjöf";

  public MemberInfoInsert(ModuleInfo modinfo) {
      super(modinfo);
      isUpdate = false;
      memInfo = new MemberInfo();
      memInfo.setDefaultValues();
      inputHandicap = new TextInput(inputHandicapName);
      //setVariables();
  }

  public MemberInfoInsert(ModuleInfo modinfo, int memInfoId)throws java.sql.SQLException {
      super(modinfo, memInfoId);
      isUpdate = true;
      memInfo = new MemberInfo(memInfoId);
      memInfo.setDefaultValues();
      inputHandicap = new TextInput(inputHandicapName, String.valueOf(memInfo.getHandicap()));
      //setVariables();
  }

  public MemberInfo getMemberInfo() {
      return this.memInfo;
  }

  public void setMemberId(int id) {
      memberID = id;
  }

  public boolean areNeetedFieldsEmpty() {
      return false;
  }

  public Vector getNeetedEmptyFields() {
      return new Vector();
  }

  public TextInput getInputHandicap() {
      return inputHandicap;
  }

  public boolean areAllFieldsEmpty() {
      return (isEmpty(inputHandicapName));
  }

  public boolean areSomeFieldsEmpty() {
      return areAllFieldsEmpty();
  }

  public Vector getEmptyFields() {
      Vector vec = new Vector();

      if (isInvalid(inputHandicapValue)) {
          vec.addElement("Forgjöf");
      }

      return vec;
  }

  public HeaderTable getInputTable() {
      HeaderTable hTable = new HeaderTable();
      hTable.setHeaderText(headerText);
      Table table = new Table(2, 1);
      hTable.add(table);

      table.add("Forgjöf", 1, 1);
      table.add(getInputHandicap(), 2, 1);

      return hTable;
  }

  public void store()throws SQLException, IOException {
      PrintWriter out = modinfo.getResponse().getWriter();
      setVariables();
      if(isUpdate())
          memInfo.update();
      else {
          idegaTimestamp stamp = new idegaTimestamp(new java.sql.Date(System.currentTimeMillis()));
          memInfo.setHistory(stamp.toString()+": Félagi skráður í kerfið");
          memInfo.setMemberId(memberID);
          memInfo.setFirstHandicap(memInfo.getHandicap());
          memInfo.insert();
      }


  }

  public void setVariables() {
      inputHandicapValue = getValue(inputHandicapName);

      if (! isInvalid(inputHandicapValue)) {
          memInfo.setHandicap(Float.valueOf(inputHandicapValue));
      }
      else
          memInfo.setHandicap(Float.valueOf("100"));
  }
}
