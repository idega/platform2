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
 * Copyright:
 *
 * Company:
 * @author
 * @version 1.0
 */
public class PhoneInsert extends EntityInsert {

  private Phone phone;
  private String phoneNumberName = "PhoneInsert_phonenumber";
  private String countryName = "PhoneInsert_country";
  private String typeName = "PhoneInsert_phonetype";

  private TextInput inputPhoneNumber;
  private DropdownMenu countryDrop;
  private DropdownMenu typeDrop;

  private String phoneNumberValue;
  private String countryValue = "1";
  private String typeValue = "1";

  private String phoneType = "Sími";
  private boolean allowEmpty = false;
  private String headerText = "Sími";

  public PhoneInsert(ModuleInfo modinfo) {
      super(modinfo);
      isUpdate = false;
      phone = new Phone();
      phone.setDefaultValues();
      inputPhoneNumber = new TextInput(phoneNumberName);
      countryDrop = countryDropDown(countryName, "1");
      typeDrop = typeDropDown(typeName, "1");
      //setVariables();
  }

  public PhoneInsert(ModuleInfo modinfo, String inputPhoneName, String dropdownTypeName) {
      super(modinfo);
      isUpdate = false;
      phone = new Phone();
      phone.setDefaultValues();
      phoneNumberName = inputPhoneName;
      typeName = dropdownTypeName;
      inputPhoneNumber = new TextInput(phoneNumberName);
      countryDrop = countryDropDown(countryName, "1");
      typeDrop = typeDropDown(typeName, "1");
      //setVariables();
  }

  public PhoneInsert(ModuleInfo modinfo, String inputPhoneName, String countryDropName, String dropdownTypeName) {
      super(modinfo);
      isUpdate = false;
      phone = new Phone();
      phone.setDefaultValues();
      phoneNumberName = inputPhoneName;
      typeName = dropdownTypeName;
      countryName = countryDropName;
      inputPhoneNumber = new TextInput(phoneNumberName);
      countryDrop = countryDropDown(countryName, "1");
      typeDrop = typeDropDown(typeName, "1");
      //setVariables();
  }

  public PhoneInsert(ModuleInfo modinfo, int phoneId)throws java.sql.SQLException {
      super(modinfo, phoneId);
      isUpdate = true;
      phone = new Phone(phoneId);
      phone.setDefaultValues();
      inputPhoneNumber = new TextInput(phoneNumberName, phone.getNumber());
      countryDrop = countryDropDown(countryName, String.valueOf(phone.getCountryId()));
      if(phone.getPhoneTypeId() != -1)
          typeDrop = typeDropDown(typeName, String.valueOf(phone.getPhoneTypeId()));
      else
          typeDrop = typeDropDown(typeName, "1");
      //setVariables();
  }


  public PhoneInsert(ModuleInfo modinfo, int phoneId, String inputPhoneName, String countryDropName, String dropdownTypeName)throws java.sql.SQLException {
      super(modinfo, phoneId);
      isUpdate = true;
      phone = new Phone(phoneId);
      phone.setDefaultValues();
      phoneNumberName = inputPhoneName;
      typeName = dropdownTypeName;
      countryName = countryDropName;
      inputPhoneNumber = new TextInput(phoneNumberName, phone.getNumber());
      countryDrop = countryDropDown(countryName, String.valueOf(phone.getCountryId()));
      if(phone.getPhoneTypeId() != -1)
          typeDrop = typeDropDown(typeName, String.valueOf(phone.getPhoneTypeId()));
      else
          typeDrop = typeDropDown(typeName, "1");
      //setVariables();
  }

  public boolean areNeetedFieldsEmpty() {
      return isEmpty(phoneNumberName);
  }

  public Vector getNeetedEmptyFields() {
      setVariables();
      Vector vec = new Vector();

      if(allowEmpty == false) {
          if (isInvalid(phoneNumberValue)) {
              vec.addElement(phoneType);
          }
      }

      return vec;
  }

  public TextInput getInputPhoneNumber() {
      return inputPhoneNumber;
  }

  public DropdownMenu getDropCountry() {
      return countryDrop;
  }

  public DropdownMenu getDropType() {
      return this.typeDrop;
  }

  public boolean areAllFieldsEmpty() {
      return (isEmpty(phoneNumberName) && isEmpty(countryName) && isEmpty(typeName));
  }

  public boolean areSomeFieldsEmpty() {
      return areAllFieldsEmpty();
  }

  public void setPhoneType(String phoneType) {
      this.phoneType = phoneType;
  }

  public void allowEmpty(boolean empty) {
      this.allowEmpty = empty;
  }

  public Vector getEmptyFields() {
      Vector vec = new Vector();

      if (isInvalid(phoneNumberValue)) {
          vec.addElement(phoneType);
      }
      //if (isInvalid(countryValue)) {
        //  vec.addElement("Land");
      //}
      //if (isInvalid(typeValue)) {
        //  vec.addElement("Tegund síma");
      //}

      return vec;
  }

  public HeaderTable getInputTable(boolean submitButton) {

      HeaderTable hTable = new HeaderTable();
      hTable.setHeaderText(headerText);
      Table table = new Table(2, 3);
      hTable.add(table);
      table.add("Tegund", 1, 1);
      table.add("Númer", 1, 2);
      table.add(getDropType(), 2, 1);
      table.add(getInputPhoneNumber(), 2, 2);
      if(submitButton)
          table.add(new SubmitButton(), 2, 3);

      return hTable;

  }
  public void store()throws SQLException, IOException {
  PrintWriter out = modinfo.getResponse().getWriter();
      if(allowEmpty && getNeetedEmptyFields().size() == 0) {
          return;
      }
      else if(isUpdate())
          phone.update();
      else {
          phone.insert();
      }
  }

  public void store(Member member)throws SQLException, IOException {

      PrintWriter out = modinfo.getResponse().getWriter();
      if(allowEmpty && areNeetedFieldsEmpty()) {
          return;
      }
      else if(isUpdate())
          phone.update();
      else {
          phone.insert();
          phone.addTo(member);
      }


  }
  public void setVariables() {
      phoneNumberValue = getValue(phoneNumberName);
      typeValue = getValue(typeName);

     // if(getValue(countryName) != null)
         // countryValue = getValue(countryName);

      if(! isInvalid(typeValue))
          phone.setPhoneTypeId(Integer.parseInt(typeValue));

      if (phoneNumberValue != null) {
          phone.setNumber(phoneNumberValue);
      }
      else
          phone.setNumber("");

      phone.setCountryId(new Integer(countryValue));
      //phone.setPhoneType(phoneType);
  }

  private DropdownMenu countryDropDown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);
        Country country = new Country();
	try {
            Country[] countryArr = (Country[]) country.findAll();
            for(int i = 0; i < countryArr.length; i++) {
                drp.addMenuElement(countryArr[i].getID(), countryArr[i].getName());
            }
            drp.setSelectedElement(selected);
	}catch(Exception e) {
            e.printStackTrace();
	}
	return drp;
  }

  private DropdownMenu typeDropDown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);
        PhoneType type = new PhoneType();
	try {
            PhoneType[] typeArr = (PhoneType[]) type.findAll();
            for(int i = 0; i < typeArr.length; i++) {
                drp.addMenuElement(String.valueOf(typeArr[i].getID()), typeArr[i].getName());
            }
            drp.setSelectedElement(selected);
	}catch(Exception e) {
            e.printStackTrace();
	}
	return drp;
  }
}
