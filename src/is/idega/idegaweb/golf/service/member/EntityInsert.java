package is.idega.idegaweb.golf.service.member;





import com.idega.presentation.*;

import com.idega.presentation.text.*;

import com.idega.presentation.ui.InterfaceObject;

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



  protected boolean bUpdate;

  public String styleAttribute = "font-size: 8pt";

  private int fontSize = 1;

  private String fontColor = "#336660";



  public EntityInsert() {

    bUpdate = false;

  }



  public abstract boolean areSomeFieldsEmpty(IWContext iwc);

  public abstract boolean areNeededFieldsEmpty(IWContext iwc);

  public abstract Vector getEmptyFields();

  public abstract Vector getNeededEmptyFields(IWContext iwc);

  public abstract void store(IWContext iwc)throws SQLException, IOException;

  public abstract void setVariables(IWContext iwc);



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



  public boolean isEmpty(IWContext iwc,String inputName) {

      return (getValue(iwc,inputName) == null || getValue(iwc,inputName).equals(""));

  }



  public String getValue(IWContext iwc,String attribute) {

      return iwc.getParameter(attribute);

  }



  protected boolean isUpdate() {

      return bUpdate;

  }



  protected java.sql.Date getDateFromInput(IWContext iwc,String inputName) {

      String strDay = getValue(iwc,inputName+"_day");

      String strMonth = getValue(iwc,inputName+"_month");

      String strYear = getValue(iwc,inputName+"_year");

      if(strDay == null || strMonth == null || strYear == null ||

          strDay.equals("") || strMonth.equals("") || strYear.equals("")) {

          return null;

      }

      IWTimeStamp stamp = new IWTimeStamp(strDay, strMonth, strYear);

      return stamp.getSQLDate();

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



   public Text formatText(String s){

    Text T= new Text();

    if(s!=null){

      T= new Text(s);

      T.setFontColor(this.fontColor);

      T.setFontSize(this.fontSize);

    }

    return T;

  }

  public Text formatText(int i){

    return formatText(String.valueOf(i));

  }

  protected void setStyle(InterfaceObject O){

    O.setAttribute("style",this.styleAttribute);

  }

}
