package is.idega.idegaweb.golf.service.member;
import is.idega.idegaweb.golf.entity.Country;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.PhoneType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BorderTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
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
    ePhone = (Phone) IDOLookup.createLegacy(Phone.class);
    ePhone.setDefaultValues();
    inputPhoneNumber = new TextInput(phoneNumberName);
    countryDrop = countryDropDown(countryName, "1");
    typeDrop = typeDropDown(typeName, "1");
    init();
  }

  public PhoneInsert(String inputPhoneName, String dropdownTypeName) {
    bUpdate = false;
    ePhone = (Phone) IDOLookup.createLegacy(Phone.class);
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
    this.ePhone = (Phone) IDOLookup.createLegacy(Phone.class);
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

  public boolean areNeededFieldsEmpty(IWContext modinfo) {
    return isEmpty(modinfo,phoneNumberName);
  }

  public Vector getNeededEmptyFields(IWContext modinfo) {
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

  public boolean areAllFieldsEmpty(IWContext modinfo) {
      return (isEmpty(modinfo,phoneNumberName) && isEmpty(modinfo,countryName) && isEmpty(modinfo,typeName));
  }

  public boolean areSomeFieldsEmpty(IWContext modinfo) {
      return areAllFieldsEmpty(modinfo);
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
  public void store(IWContext modinfo)throws SQLException, IOException {
    setVariables(modinfo);
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

  public void store(IWContext modinfo,Member member)throws SQLException, IOException {
    setVariables(modinfo);
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
  public void setVariables(IWContext modinfo) {
    phoneNumberValue = getValue(modinfo,phoneNumberName);
    typeValue = getValue(modinfo,typeName);

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

  private DropdownMenu typeDropDown(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    PhoneType type = (PhoneType) IDOLookup.instanciateEntity(PhoneType.class);
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
