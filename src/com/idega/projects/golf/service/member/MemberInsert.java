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

public class MemberInsert extends EntityInsert {

  private Member member;

  private TextInput inputSocial;
  private TextInput inputName;
  private TextInput inputEmail;
  private TextInput inputJob;
  private TextInput inputWorkPlace;
  private DropdownMenu dropGender;

  private final String inputSocialName = "MemberInsert_sociasecuritynumber";
  private final String inputNameName = "MemberInsert_name";
  private final String inputEmailName = "MemberInsert_email";
  private final String inputJobName = "MemberInsert_job";
  private final String inputWorkPlaceName = "MemberInsert_workplace";
  private final String dropGenderName = "MemberInsert_gender";

  private String inputSocialValue;
  private String inputNameValue;
  private String inputEmailValue;
  private String inputJobValue;
  private String inputWorkPlaceValue;
  private String dropGenderValue;
  private String headerText = "Meðlimur";

  public boolean debug = true;

  public MemberInsert(ModuleInfo modinfo) throws java.sql.SQLException{
      super(modinfo);
      isUpdate = false;

      member = new Member();
      if(debug) {
          member.setDefaultValues();
          member.setCardId(1);
      }
      inputSocial = new TextInput(inputSocialName);
      inputName = new TextInput(inputNameName);
      inputName.setAsNotEmpty("Vinsamelgast settu nafn");
      inputSocial.setAsNotEmpty("Vinsamelgast settu Kennitölu");
      inputEmail = new TextInput(inputEmailName);
      inputJob = new TextInput(inputJobName);
      inputWorkPlace = new TextInput(inputWorkPlaceName);
      dropGender = genderDrop(dropGenderName, "m");
      //setVariables();
  }

  public MemberInsert(ModuleInfo modinfo, int memberId)throws SQLException {
      super(modinfo, memberId);
      isUpdate = true;
      member = new Member(memberId);

      if(member.getSocialSecurityNumber() != null)
          inputSocial = new TextInput(inputSocialName, member.getSocialSecurityNumber());
      else
          inputSocial = new TextInput(inputSocialName);
      if(member.getName() != null)
          inputName = new TextInput(inputNameName, member.getName());
      else
          inputName = new TextInput(inputNameName);
      if(member.getEmail() != null)
          inputEmail = new TextInput(inputEmailName, member.getEmail());
      else
          inputEmail = new TextInput(inputEmailName);

      if(member.getJob() != null)
          inputJob = new TextInput(inputJobName, member.getJob());
      else
          inputJob = new TextInput(inputJobName);
      if(member.getWorkPlace() != null)
          inputWorkPlace = new TextInput(inputWorkPlaceName, member.getWorkPlace());
      else
          inputWorkPlace = new TextInput(inputWorkPlaceName);

      if(member.getGender() != null)
          dropGender = genderDrop(dropGenderName, member.getGender());
      else
          dropGender = genderDrop(dropGenderName, "m");

      inputName.setAsNotEmpty("Vinsamelgast settu nafn");

      //setVariables();
  }


  public TextInput getInputSocialSecurityNumber() {
      return this.inputSocial;
  }

  public TextInput getInputMemberName() {
      return this.inputName;
  }

  public TextInput getInputEmail() {
      return this.inputEmail;
  }


  public TextInput getInputJob() {
      return this.inputJob;
  }

  public TextInput getInputWorkPlace() {
      return this.inputWorkPlace;
  }

  public DropdownMenu getDropdownGender() {
      return dropGender;
  }

  public Vector getEmptyFields() {
      Vector vec = new Vector();

      if ( isInvalid(inputSocialValue)) {
          vec.addElement("Kennitölu");
      }
      else if(! ErrorChecker.isValidSosialSecurityNumber(inputSocialValue)) {
          vec.addElement("Kennitala er ekki rétt");
      }
      if ( isInvalid(inputNameValue)) {
          vec.addElement("Nafn");
      }
      else {
          Name name = new Name(inputNameValue);
          if(name.getLastName().equals(""))
              vec.addElement("Eftirnafn");
      }
      if ( isInvalid(inputEmailValue)) {
          vec.addElement("Netfang");
      }
      if ( isInvalid(inputJobValue)) {
          vec.addElement("Starfsheiti");
      }
      if ( isInvalid(inputWorkPlaceValue)) {
          vec.addElement("Vinnustaður");
      }
      if ( isInvalid(dropGenderValue)) {
          vec.addElement("Kyn");
      }
      return vec;
  }

  public Vector getNeetedEmptyFields() {
    setVariables();
    Vector vec = new Vector();

      if ( isInvalid(inputSocialValue)) {
          vec.addElement("Kennitölu");
      }
      else if(! ErrorChecker.isValidSosialSecurityNumber(inputSocialValue)) {
          vec.addElement("Kennitala er ekki rétt");
      }
      if ( isInvalid(inputNameValue)) {
          vec.addElement("Nafn");
      }
      if ( isInvalid(dropGenderValue)) {
          vec.addElement("Kyn");
      }

      return vec;
  }

  public boolean areSomeFieldsEmpty() {
      return (getEmptyFields().size() > 0);
  }

  public Member getMember() {
      return this.member;
  }

  public boolean areNeetedFieldsEmpty() {
    return (getNeetedEmptyFields().size() > 0);
  }

  //precondition Have to call getNetedEmptyFields() !!!
  public void store() throws java.io.IOException, java.sql.SQLException {
      if(isUpdate())
          member.update();
      else
          member.insert();
  }

  public HeaderTable getInputTable() {
      HeaderTable hTable = new HeaderTable();
      hTable.setHeaderText(headerText);
      Table table = new Table(2, 6);
      hTable.add(table);

      table.add("Nafn", 1, 1);
      table.add("Kennitala", 1, 2);
      table.add("Kyn", 1, 3);
      table.add("Netfang", 1, 4);
      table.add("Starfsheiti", 1, 5);
      table.add("Vinnustaður", 1, 6);
      table.add(getInputMemberName(), 2, 1);
      table.add(getInputSocialSecurityNumber(), 2, 2);
      table.add(getDropdownGender(), 2, 3);
      table.add(getInputEmail(), 2, 4);
      table.add(getInputJob(), 2, 5);
      table.add(getInputWorkPlace(), 2, 6);

      return hTable;
  }


  public void setVariables() {
      inputSocialValue = getValue(inputSocialName);
      inputNameValue = getValue(inputNameName);
      inputEmailValue = getValue(inputEmailName);
      inputJobValue = getValue(inputJobName);
      inputWorkPlaceValue = getValue(inputWorkPlaceName);
      dropGenderValue = getValue(dropGenderName);
      setEntity();
  }

  private void setEntity() {

      if (! isInvalid(inputSocialValue)) {
          if(ErrorChecker.isValidSosialSecurityNumber(inputSocialValue)) {
              member.setSocialSecurityNumber(inputSocialValue);
              member.setDateOfBirth(DateManipulator.getDateSQLFromSocialSecurityNumber(inputSocialValue));
          }
      }
      if (! isInvalid(inputNameValue)) {
          Name name = new Name(inputNameValue);
          member.setFirstName(name.getFirstName());
          member.setMiddleName(name.getMiddleName());
          member.setLastName(name.getLastName());
      }
      if (inputEmailValue != null) {
          member.setEmail(inputEmailValue);
      }
      if (inputJobValue != null) {
          member.setJob(inputJobValue);
      }
      if (inputWorkPlaceValue != null) {
          member.setWorkPlace(inputWorkPlaceValue);
      }
      if (dropGenderValue != null) {
          member.setGender(dropGenderValue);
      }
  }
  public boolean areAllFieldsEmpty() {
      return (getEmptyFields().size() == 6);
  }

  public DropdownMenu genderDrop(String name, String selected) {
          DropdownMenu drp = new DropdownMenu(name);
          drp.addMenuElement("m", "KK");
          drp.addMenuElement("f", "KVK");
          drp.setSelectedElement(selected);
          return drp;
  }
}
