package com.idega.projects.golf.service.member;

import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.util.*;
import java.util.*;
import java.sql.*;
import java.io.*;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
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
  public class CardInsert extends EntityInsert{
  private Card card;
  private TextInput nameInput;
  private TextInput socialInput;
  private TextInput numberInput;
  private TextInput typeInput;
  private TextInput companyInput;
  private DropdownMenu expireMonth;
  private DropdownMenu expireYear;

  private String ownarName = "CardInsert_name";
  private String socialName = "CardInsert_social";
  private String numberName = "CardInsert_number";
  private String typeName = "CardInsert_type";
  private String companyName = "CardInsert_company";
  private String expireMonthName = "CardInsert_expiremonth";
  private String expireYearName = "CardInsert_expireyear";

  private String ownarValue = null;
  private String socialValue = null;
  private String numberValue = null;
  private String typeValue = null;
  private String companyValue = null;
  private String expireMonthValue = null;
  private String expireYearValue = null;
  private String headerText = "Kortaupplýsingar";

  public CardInsert(ModuleInfo modinfo) {
      super(modinfo);
      isUpdate = false;
      card = new Card();
      nameInput = new TextInput(ownarName);
      socialInput = new TextInput(socialName);
      numberInput = new TextInput(numberName);
      typeInput = new TextInput(typeName);
      companyInput = new TextInput(companyName);
      setExireDate(null);
      isUpdate = false;
  }

  public CardInsert(ModuleInfo modinfo, int cardId)throws SQLException {

      super(modinfo, cardId);
      isUpdate = true;
      card = new Card(cardId);
      if(card.getName() != null)
          nameInput = new TextInput(ownarName, card.getName());
      else
          nameInput = new TextInput(ownarName);
      if(card.getSocialSecurityNumber() != null)
          socialInput = new TextInput(socialName, card.getSocialSecurityNumber());
      else
          socialInput = new TextInput(socialName);
      if(card.getCardNumber() != null)
          numberInput = new TextInput(numberName, card.getCardNumber());
      else
          numberInput = new TextInput(numberName);

      if(card.getCardType() != null)
          typeInput = new TextInput(typeName, card.getCardType());
      else
          typeInput = new TextInput(typeName);
      if(card.getCardCompany() != null)
          companyInput = new TextInput(companyName, card.getCardCompany());
      else
          companyInput = new TextInput(companyName);
      setExireDate(card.getExpireDate());
      isUpdate = true;
  }

  private void setExireDate(java.sql.Date date) {
      GregorianCalendar cal = new GregorianCalendar();

      if(date != null) {
          idegaTimestamp stamp = new idegaTimestamp(date);
          expireMonth = monthDropDown(expireMonthName, String.valueOf(stamp.getMonth()));
          expireYear = yearDropDown(expireYearName, String.valueOf(stamp.getYear()), (cal.get(Calendar.YEAR)-5), (cal.get(Calendar.YEAR)+3));
      }
      else {
          expireMonth = monthDropDown(expireMonthName, "");
          expireYear = yearDropDown(expireYearName, "", (cal.get(Calendar.YEAR)-5), (cal.get(Calendar.YEAR)+3));
      }
  }

  public Card getCard() {
      return card;
  }

  public TextInput getNameInput() {
      return this.nameInput;
  }

  public TextInput getSocialSequrityNumberInput() {
      return this.socialInput;
  }

  public TextInput getNumberInput() {
      return this.numberInput;
  }

  public DropdownMenu getDropYear() {
      return this.expireYear;
  }

  public DropdownMenu getDropMonth() {
      return this.expireMonth;
  }

  public TextInput getTypeInput() {
      return this.typeInput;
  }

  public TextInput getCompanyInput() {
      return this.companyInput;
  }

  public boolean areNeetedFieldsEmpty() {
      setVariables();
      if(areAllFieldsEmpty())
          return false;
      else return areSomeFieldsEmpty();

  }

  public Vector getNeetedEmptyFields() {
      setVariables();
      boolean isAllEmpty = areAllFieldsEmpty();
      if( isAllEmpty){
          return new Vector();
      }
      else return getEmptyFields();
  }

  public boolean areAllFieldsEmpty()  {

      boolean isEmpty = true;
      isEmpty = (isEmpty(companyName) && isInvalid(expireMonthValue) && isInvalid(expireYearValue)/*expireValue == null*/ && isEmpty(ownarName) &&
              isEmpty(numberName) && isEmpty(socialName) && isEmpty(typeName));

      return isEmpty;

  }

  public boolean areSomeFieldsEmpty() {
      return ((companyValue == null) || isInvalid(expireMonthValue) || isInvalid(expireYearValue) || (ownarValue == null) ||
              (numberValue == null) || (socialValue == null) || (typeValue == null) ||
              ! companyValue.equals("") || ! ownarValue.equals("") ||
              ! numberValue.equals("") || ! socialValue.equals("") || ! typeValue.equals(""));
  }

  public void setVariables() {
      companyValue = getValue(companyName);
      ownarValue = getValue(ownarName);
      numberValue = getValue(numberName);
      socialValue = getValue(socialName);
      typeValue = getValue(typeName);
      expireMonthValue = getValue(expireMonthName);
      expireYearValue = getValue(expireYearName);

      if (companyValue  != null) {
          //System.out.print("\n\ncompany "+companyValue+"\n\n");
          card.setCardCompany(companyValue);
      }
      if (numberValue  != null) {
          card.setCardNumber(numberValue);
      }
      if(! isInvalid(expireMonthValue) && ! isInvalid(expireYearValue)) {
          idegaTimestamp stamp = new idegaTimestamp("1", expireMonthValue, expireYearValue);
          card.setExpireDate(stamp.getSQLDate());
      }
      if (typeValue  != null) {
          card.setCardType(typeValue);
      }
      if (ownarValue != null) {
          card.setName(ownarValue);
      }
      if (numberValue  != null) {
          card.setCardNumber(numberValue);
      }
      if(socialValue != null) {
          card.setSocialSecurityNumber(socialValue);
      }
  }

  public Vector getEmptyFields() {
      Vector vec = new Vector();

      if (isInvalid(companyValue)) {
          vec.addElement("Fyrirtæki");
      }

      if (isInvalid(expireMonthValue) || isInvalid(expireYearValue)) {
          vec.addElement("Gildistími");
      }
      if (isInvalid(ownarValue)) {
          vec.addElement("Korthafi");
      }
      if (isInvalid(numberValue)) {
          vec.addElement("Kortanúmer");
      }
      /*else {
          cardChecker check = new cardChecker();
          if( ! check.isValid(numberValue)) {
              vec.addElement("Kortanúmer ekki rétt");
          }
      }*/
      if (isInvalid(socialValue)) {
          vec.addElement("Kennitala");
      }
      else {
          if(! ErrorChecker.isValidSosialSecurityNumber(socialValue))
              vec.addElement("Kennitala ekki rétt");
      }
      if (isInvalid(typeValue)) {
          vec.addElement("Tegund korts");
      }

      return vec;
  }
  //precondition functioncall getNeatedEmptyFields()
  public void store()throws SQLException, IOException {

      if(isUpdate())
          card.update();
      else
          card.insert();
  }

    public HeaderTable getInputTable() {

      HeaderTable hTable = new HeaderTable();
      hTable.setHeaderText(headerText);
      Table table = new Table(2, 6);
      hTable.add(table);


      table.add("Fyrirtæki", 1, 1);
      table.add(getCompanyInput(), 2, 1);

      table.add("Kennitala", 1, 2);
      table.add(getSocialSequrityNumberInput(), 2, 2);

      table.add("Gildistími", 1, 3);
      table.add(getDropMonth(), 2, 3);
      table.add(getDropYear(), 2, 3);

      table.add("Korthafi", 1, 4);
      table.add(getNameInput(), 2, 4);

      table.add("Kortanúmer", 1, 5);
      table.add(getNumberInput(), 2, 5);

      table.add("Tegund", 1, 6);
      table.add(getTypeInput(), 2, 6);

      return hTable;
  }

  private DropdownMenu monthDropDown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);
        String month = null;

        drp.addMenuElement("", "Mán.");
        for(int i = 1; i < 13; i++) {

            if(i < 10)
                month = "0"+i;
            else
                month = String.valueOf(i);
            drp.addMenuElement(String.valueOf(i), month);
        }
        drp.setSelectedElement(selected);

	return drp;
  }

  private DropdownMenu yearDropDown(String name, String selected, int from, int to) {
	DropdownMenu drp = new DropdownMenu(name);

        drp.addMenuElement("", "Ár");
        for(int i = from; i <= to; i++) {
            drp.addMenuElement(String.valueOf(i), String.valueOf(i));
        }
        drp.setSelectedElement(selected);

	return drp;
  }
}


