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
  public class AddressInsert extends EntityInsert{


  private Address address;
  private TextInput addressInput;
  private DropdownMenu zipDrop;
  private DropdownMenu countryDrop;

  private String addressName = "AddressInsert_address";
  private String zipName = "AddressInsert_zipcode";
  private String countryName = "AddressInsert_country";

  private String addressValue = null;
  private String zipValue = null;
  private String countryValue = null;

  private String errorRedirect = "membererror.jsp";
  private String sessionId = "error";
  private boolean allowEmpty = false;

  public AddressInsert(ModuleInfo modinfo) {
      super(modinfo);
      isUpdate = false;
      address = new Address();
      addressInput = new TextInput(addressName);
      countryDrop = countryDropDown(countryName, "1");
      zipDrop = zipDropDown(zipName, "2");
      //setVariables();
  }

  public AddressInsert(ModuleInfo modinfo, String addressInputName, String countryDropDownName, String zipCodeDropDownName) {
      super(modinfo);
      isUpdate = false;
      address = new Address();
      addressName = addressInputName;
      countryName = countryDropDownName;
      zipName = zipCodeDropDownName;
      addressInput = new TextInput(addressName);
      countryDrop = countryDropDown(countryName, "1");
      zipDrop = zipDropDown(zipName, "2");
      //setVariables();
  }

  public AddressInsert(ModuleInfo modinfo, int addressId)throws java.sql.SQLException {
      super(modinfo, addressId);
      isUpdate = true;
      address = new Address(addressId);
      if(address.getStreet() != null)
          addressInput = new TextInput(addressName, address.getStreet());
      else
          addressInput = new TextInput(addressName, "");
      if(address.getCountryId() != -1)
          countryDrop = countryDropDown(countryName, String.valueOf(address.getCountryId()));
      else
          countryDrop = countryDropDown(countryName, "2");
      zipDrop = zipDropDown(zipName, String.valueOf(address.getZipcodeId()));
      //setVariables();
  }

  public AddressInsert(ModuleInfo modinfo, int addressId, String addressInputName, String countryDropDownName, String zipCodeDropDownName)throws java.sql.SQLException {
      super(modinfo, addressId);
      isUpdate = true;
      address = new Address(addressId);
      addressName = addressInputName;
      countryName = countryDropDownName;
      zipName = zipCodeDropDownName;
      if(address.getStreet() != null)
          addressInput = new TextInput(addressName, address.getStreet());
      else
          addressInput = new TextInput(addressName, "");
      if(address.getCountryId() != -1)
          countryDrop = countryDropDown(countryName, String.valueOf(address.getCountryId()));
      else
          countryDrop = countryDropDown(countryName, "2");
      zipDrop = zipDropDown(zipName, String.valueOf(address.getZipcodeId()));
      //setVariables();
  }


  public void allowEmpty(boolean allow) {
      allowEmpty = allow;
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

  private DropdownMenu zipDropDown(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    ZipCode zip = new ZipCode();
    try {
        ZipCode [] zipArr = (ZipCode[]) zip.findAllOrdered("code");
        for(int i = 0; i < zipArr.length; i++) {
            drp.addMenuElement(zipArr[i].getID(), zipArr[i].getCode()+" "+zipArr[i].getCity());
        }
        drp.setSelectedElement(selected);
    }
    catch(SQLException e) {
        e.printStackTrace();
    }
    return drp;
  }

  public void setVariables() {
      addressValue = getValue(addressName);
      zipValue = getValue(zipName);
      countryValue = getValue(countryName);

      if (addressValue  != null) {
          address.setStreet(addressValue);
      }
      if (zipValue  != null) {
          address.setZipcodeId(new Integer(zipValue));
      }
      if (countryValue  != null) {
          address.setCountryId(new Integer(countryValue));
      }
  }

  public TextInput getInputAddress() {
      return addressInput;
  }

  public DropdownMenu getDropCountry() {
      return countryDrop;
  }

  public DropdownMenu getDropZipcode() {
      return zipDrop;
  }

  public boolean areSomeFieldsEmpty() {
      return (isEmpty(addressName) || isEmpty(zipName) || isEmpty(countryName));
  }

  public boolean areAllFieldsEmpty() {
      return (isEmpty(addressName) && isEmpty(zipName) && isEmpty(countryName));
  }

  public boolean areNeetedFieldsEmpty() {
      return isEmpty(addressName);
  }

  public Vector getEmptyFields() {
      Vector vec = new Vector();

      if (isInvalid(addressValue)) {
          vec.addElement("Heimilisfang");
      }
      if (isInvalid(countryValue)) {
          vec.addElement("Land");
      }
      if (isInvalid(zipValue)) {
          vec.addElement("Póstnúmer");
      }

      return vec;
  }

  public Vector getNeetedEmptyFields() {
      setVariables();
      Vector vec = new Vector();

      if(allowEmpty == false) {
          if (isInvalid(addressValue)) {
              vec.addElement("Heimilisfang");
          }
      }

      return vec;
  }

  public Address getAddress () {
      return this.address;
  }


  public void store()throws SQLException, IOException {

      PrintWriter out = modinfo.getResponse().getWriter();
      if(allowEmpty && areNeetedFieldsEmpty()) {
          return;
      }

      if(isUpdate)
          address.update();
      else
          address.insert();

  }
  /** If to link the address to a member*/
  public void store(Member member)throws SQLException, IOException {

      PrintWriter out = modinfo.getResponse().getWriter();
      if(allowEmpty && areNeetedFieldsEmpty()) {
          return;
      }

      if(isUpdate)
          address.update();
      else {
          address.insert();
          address.addTo(member);
      }

  }

}
