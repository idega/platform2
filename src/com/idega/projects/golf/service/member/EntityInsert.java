package com.idega.projects.golf.service.member;


import com.idega.jmodule.object.*;
import com.idega.util.*;
import java.util.*;
import java.sql.*;
import java.io.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public abstract class EntityInsert {

  protected String errorRedirect;
  protected String sessionId;
  protected ModuleInfo modinfo;
  protected boolean isUpdate;
  protected String headerText = "";
  private int entityID;

  public EntityInsert(ModuleInfo modinfo) {
      this.modinfo = modinfo;
      errorRedirect = "membererror.jsp";
      sessionId = "error";
      isUpdate = false;
  }

  public EntityInsert(ModuleInfo modinfo, int entityId) {
      this.modinfo = modinfo;
      errorRedirect = "membererror.jsp";
      sessionId = "error";
      isUpdate = true;
      entityID = entityId;
  }


  public abstract boolean areAllFieldsEmpty();
  public abstract boolean areSomeFieldsEmpty();
  public abstract boolean areNeetedFieldsEmpty();
  //public abstract String[] getEmptyFields();
  //public abstract String[] getNeetedEmptyFields();
  public abstract Vector getEmptyFields();
  public abstract Vector getNeetedEmptyFields();
 // public abstract void showInputForm()throws SQLException, IOException;
  public abstract void store()throws SQLException, IOException;
  public abstract void setVariables();

  public boolean isDateInputValid(String inputName) {
      if(inputName+"_day" == null || (inputName+"_day").equals(""))
          return false;
      else if(inputName+"_month" == null || (inputName+"_month").equals(""))
          return false;
      else if(inputName+"_year" == null || (inputName+"_year").equals(""))
          return false;

      return true;
  }

  public boolean isDigitOnly(String value) {
      if(value == null || value.length() == 0)
          return false;
      char[] arr = value.toCharArray();
      for(int i = 0; i < arr.length; i++) {
          if(! Character.isDigit(arr[i]))
              return false;
      }
      return true;
  }

  public boolean isEmpty(String inputName) {
      return (getValue(inputName) == null || getValue(inputName).equals(""));
  }

  public String getValue(String attribute) {
      return modinfo.getParameter(attribute);
  }

  public void setErrorRedirectPageAndSessionID(String redirectPage, String sessionID) {
      this.errorRedirect = redirectPage;
      this.sessionId = sessionID;
  }

  protected boolean isUpdate() {
      return isUpdate;
  }

  protected java.sql.Date getDateFromInput(String inputName) {
      String strDay = getValue(inputName+"_day");
      String strMonth = getValue(inputName+"_month");
      String strYear = getValue(inputName+"_year");
      if(strDay == null || strMonth == null || strYear == null ||
          strDay.equals("") || strMonth.equals("") || strYear.equals("")) {
          return null;
      }
      idegaTimestamp stamp = new idegaTimestamp(strDay, strMonth, strYear);
      return stamp.getSQLDate();
  }

  public void setHeaderText(String text) {
      this.headerText = text;
  }

  public boolean isInvalid(String str) {
      return ((str == null) || str.equals(""));
  }

  protected Vector StringArrToVector(String[] strArr) {
      Vector vec = new Vector();
      for (int i = 0; i < strArr.length; i++) {
          vec.addElement(strArr[i]);
      }
      return vec;
  }

  public void forward(String forwardPage)throws IOException
   {
      modinfo.getResponse().sendRedirect(forwardPage);
   }



}