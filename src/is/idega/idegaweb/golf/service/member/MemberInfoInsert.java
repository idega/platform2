package is.idega.idegaweb.golf.service.member;
import is.idega.idegaweb.golf.entity.MemberInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BorderTable;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
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
    this.eMemberInfo = (MemberInfo) IDOLookup.createLegacy(MemberInfo.class);
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

  public boolean areNeededFieldsEmpty(IWContext modinfo) {
    return false;
  }

  public Vector getNeededEmptyFields(IWContext modinfo) {
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

  public boolean areSomeFieldsEmpty(IWContext modinfo) {
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

  public void store(IWContext modinfo)throws SQLException, IOException {
    setVariables(modinfo);
    if(bUpdate)
        eMemberInfo.update();
    else {
      IWTimestamp stamp = new IWTimestamp();
      eMemberInfo.setHistory(stamp.toString()+": Félagi skráður í kerfið");
      eMemberInfo.setMemberId(memberID);
      eMemberInfo.setFirstHandicap(eMemberInfo.getHandicap());
      eMemberInfo.insert();
    }
  }

  public void setVariables(IWContext modinfo) {
    inputHandicapValue = getValue(modinfo,inputHandicapName);
    if (! isInvalid(inputHandicapValue)) {
      this.eMemberInfo.setHandicap(Float.valueOf(inputHandicapValue));
    }
    else
      this.eMemberInfo.setHandicap(Float.valueOf("100"));
  }
}