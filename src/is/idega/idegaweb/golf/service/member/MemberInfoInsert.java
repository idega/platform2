package is.idega.idegaweb.golf.service.member;

import is.idega.idegaweb.golf.entity.*;

import com.idega.presentation.*;

import com.idega.presentation.ui.*;

import com.idega.util.*;

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

    this.eMemberInfo = ((is.idega.idegaweb.golf.entity.MemberInfoHome)com.idega.data.IDOLookup.getHomeLegacy(MemberInfo.class)).createLegacy();

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



  public boolean areNeededFieldsEmpty(IWContext iwc) {

    return false;

  }



  public Vector getNeededEmptyFields(IWContext iwc) {

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



  public boolean areSomeFieldsEmpty(IWContext iwc) {

    return (isEmpty(iwc,inputHandicapName));

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



  public void store(IWContext iwc)throws SQLException, IOException {

    setVariables(iwc);

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



  public void setVariables(IWContext iwc) {

    inputHandicapValue = getValue(iwc,inputHandicapName);

    if (! isInvalid(inputHandicapValue)) {

      this.eMemberInfo.setHandicap(Float.valueOf(inputHandicapValue));

    }

    else

      this.eMemberInfo.setHandicap(Float.valueOf("100"));

  }

}
