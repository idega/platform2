package is.idega.idegaweb.golf.service.member;
import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.Country;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.ZipCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
  public class AddressInsert extends EntityInsert{


  private Address eAddress;
  private TextInput addressInput;
  private DropdownMenu zipDrop;
  private DropdownMenu countryDrop;

  private String addressName = "AddressInsert_address";
  private String zipName = "AddressInsert_zipcode";
  private String countryName = "AddressInsert_country";

  private String addressValue = null;
  private String zipValue = null;
  private String countryValue = null;
  //private boolean bUpdate = false;

  private void init(){
    setStyle(addressInput);
    setStyle(countryDrop);
    setStyle(zipDrop);
  }

  public AddressInsert() {
    this.bUpdate = false;
    this.eAddress = (Address) IDOLookup.createLegacy(Address.class);
    addressInput = new TextInput(addressName);
    countryDrop = countryDropDown(countryName, "1");
    zipDrop = zipDropDown(zipName, "2");
    init();
  }

  public AddressInsert( String addressInputName, String countryDropDownName, String zipCodeDropDownName) {
    this.bUpdate = false;
    this.eAddress = (Address) IDOLookup.createLegacy(Address.class);
    addressName = addressInputName;
    countryName = countryDropDownName;
    zipName = zipCodeDropDownName;
    addressInput = new TextInput(addressName);
    countryDrop = countryDropDown(countryName, "1");
    zipDrop = zipDropDown(zipName, "2");
    init();
  }

  public AddressInsert( Address address)throws java.sql.SQLException {
    this.eAddress = address;
    this.bUpdate = true;
    if(eAddress.getStreet() != null)
        addressInput = new TextInput(addressName, eAddress.getStreet());
    else
        addressInput = new TextInput(addressName, "");
    if(eAddress.getCountryId() != -1)
        countryDrop = countryDropDown(countryName, String.valueOf(eAddress.getCountryId()));
    else
        countryDrop = countryDropDown(countryName, "2");
    zipDrop = zipDropDown(zipName, String.valueOf(eAddress.getZipcodeId()));
    init();
  }

  public AddressInsert(Address address, String addressInputName, String countryDropDownName, String zipCodeDropDownName)throws java.sql.SQLException {
    this.eAddress = address;
    this.bUpdate = true;

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
    init();
  }


  private DropdownMenu countryDropDown(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    Country country = (Country) IDOLookup.instanciateEntity(Country.class);
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
    ZipCode zip = (ZipCode) IDOLookup.instanciateEntity(ZipCode.class);
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

  public void setVariables(IWContext modinfo) {
      addressValue = getValue(modinfo,addressName);
      zipValue = getValue(modinfo,zipName);
      countryValue = getValue(modinfo,countryName);

      addressInput.keepStatusOnAction();
      zipDrop.keepStatusOnAction();
      countryDrop.keepStatusOnAction();

      if (addressValue  != null) {
          this.eAddress.setStreet(addressValue);
      }
      if (zipValue  != null) {
          this.eAddress.setZipcodeId(new Integer(zipValue));
      }
      if (countryValue  != null) {
          this.eAddress.setCountryId(new Integer(countryValue));
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
  public boolean areSomeFieldsEmpty(IWContext modinfo) {
      return (isEmpty(modinfo,addressName) || isEmpty(modinfo,zipName) || isEmpty(modinfo,countryName));
  }
  public boolean areNeededFieldsEmpty(IWContext modinfo) {
      return isEmpty(modinfo,addressName);
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

  public Vector getNeededEmptyFields(IWContext modinfo) {
      setVariables(modinfo);
      Vector vec = new Vector();

      if (isInvalid(addressValue)) {
          vec.addElement("Heimilisfang");
      }

      return vec;
  }

  public Address getAddress () {
      return this.eAddress;
  }

  public void store(IWContext modinfo)throws SQLException, IOException {
    setVariables(modinfo);
    if(addressValue == null) {
        return;
    }
    if(this.bUpdate)
        this.eAddress.update();
    else
        this.eAddress.insert();
  }
  /** If to link the address to a member*/
  public void store(IWContext modinfo,Member member)throws SQLException, IOException {
    setVariables(modinfo);
    if(addressValue == null) {
        return;
    }
    if(this.bUpdate) {
      if((this.eAddress.getStreet() != null) && (! this.eAddress.getStreet().equals("")))
        this.eAddress.update();
      else {
        this.eAddress.removeFrom(member);
        this.eAddress.delete();
      }
    }
    else if((this.eAddress.getStreet() != null) && (! this.eAddress.getStreet().equals("")) ){
      this.eAddress.insert();
      this.eAddress.addTo(member);
    }
  }
}

