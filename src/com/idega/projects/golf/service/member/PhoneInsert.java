package com.idega.projects.golf.service.member;
import com.idega.projects.golf.entity.*;
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

  private Phone ePhone;
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

  public PhoneInsert() {
    bUpdate = false;
    ePhone = new Phone();
    ePhone.setDefaultValues();
    inputPhoneNumber = new TextInput(phoneNumberName);
    countryDrop = countryDropDown(countryName, "1");
    typeDrop = typeDropDown(typeName, "1");
    init();
  }

  public PhoneInsert(String inputPhoneName, String dropdownTypeName) {
    bUpdate = false;
    ePhone = new Phone();
    ePhone.setDefaultValues();
    phoneNumberName = inputPhoneName;
    typeName = dropdownTypeName;
    inputPhoneNumber = new TextInput(phoneNumberName);
    countryDrop = countryDropDown(countryName, "1");
    typeDrop = typeDropDown(typeName, "1");
    init();
  }

  public PhoneInsert( String inputPhoneName, String countryDropName, String dropdownTypeName) {
    this.bUpdate = false;
    this.ePhone = new Phone();
    ePhone.setDefaultValues();
    phoneNumberName = inputPhoneName;
    typeName = dropdownTypeName;
    countryName = countryDropName;
    inputPhoneNumber = new TextInput(phoneNumberName);
    countryDrop = countryDropDown(countryName, "1");
    typeDrop = typeDropDown(typeName, "1");
    init();
  }

  public PhoneInsert( Phone ePhone)throws java.sql.SQLException {
    this.ePhone = ePhone;
    this.bUpdate = true;
    ePhone.setDefaultValues();
    inputPhoneNumber = new TextInput(phoneNumberName, ePhone.getNumber());
    countryDrop = countryDropDown(countryName, String.valueOf(ePhone.getCountryId()));
    if(ePhone.getPhoneTypeId() != -1)
        typeDrop = typeDropDown(typeName, String.valueOf(ePhone.getPhoneTypeId()));
    else
        typeDrop = typeDropDown(typeName, "1");
    init();
  }


  public PhoneInsert(Phone ePhone, String inputPhoneName, String countryDropName, String dropdownTypeName)throws java.sql.SQLException {
    this.bUpdate = true;
    this.ePhone = ePhone;
    ePhone.setDefaultValues();
    phoneNumberName = inputPhoneName;
    typeName = dropdownTypeName;
    countryName = countryDropName;
    inputPhoneNumber = new TextInput(phoneNumberName, ePhone.getNumber());
    countryDrop = countryDropDown(countryName, String.valueOf(ePhone.getCountryId()));
    if(ePhone.getPhoneTypeId() != -1)
      typeDrop = typeDropDown(typeName, String.valueOf(ePhone.getPhoneTypeId()));
    else
      typeDrop = typeDropDown(typeName, "1");
      init();
  }

  private void init(){
    setStyle(inputPhoneNumber);
    setStyle(countryDrop);
    setStyle(typeDrop);
  }

  public boolean areNeededFieldsEmpty(IWContext iwc) {
    return isEmpty(iwc,phoneNumberName);
  }

  public Vector getNeededEmptyFields(IWContext iwc) {
    Vector vec = new Vector();
    if (isInvalid(phoneNumberValue)) {
        vec.addElement(phoneType);
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

  public boolean areAllFieldsEmpty(IWContext iwc) {
      return (isEmpty(iwc,phoneNumberName) && isEmpty(iwc,countryName) && isEmpty(iwc,typeName));
  }

  public boolean areSomeFieldsEmpty(IWContext iwc) {
      return areAllFieldsEmpty(iwc);
  }

  public void setPhoneType(String phoneType) {
      this.phoneType = phoneType;
  }

  public Vector getEmptyFields() {
    Vector vec = new Vector();
    if (isInvalid(phoneNumberValue)) {
        vec.addElement(phoneType);
    }
    return vec;
  }

  public BorderTable getInputTable(boolean submitButton) {
    BorderTable hTable = new BorderTable();
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
  public void store(IWContext iwc)throws SQLException, IOException {
    setVariables(iwc);
    if(phoneNumberValue == null) {
      return;
    }
    else if(isUpdate()) {
      ePhone.update();
    }
    else {
      ePhone.insert();
    }
  }

  public void store(IWContext iwc,Member member)throws SQLException, IOException {
    setVariables(iwc);
    if(phoneNumberValue == null) {
        System.err.println("   Empty   ()");
        return;
    }
    else if(isUpdate()) {
        if((ePhone.getNumber() != null) && (! ePhone.getNumber().equals("")) ){
            ePhone.update();
        }
        else {
            ePhone.removeFrom(member);
            ePhone.delete();
        }
    }
    else if((ePhone.getNumber() != null) && (! ePhone.getNumber().equals("")) ){
        ePhone.insert();
        ePhone.addTo(member);
    }
  }
  public void setVariables(IWContext iwc) {
    phoneNumberValue = getValue(iwc,phoneNumberName);
    typeValue = getValue(iwc,typeName);

    if(! isInvalid(typeValue))
        ePhone.setPhoneTypeId(Integer.parseInt(typeValue));

    if (phoneNumberValue != null) {
        ePhone.setNumber(phoneNumberValue);
    }
    else
        ePhone.setNumber("");

    ePhone.setCountryId(new Integer(countryValue));
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
