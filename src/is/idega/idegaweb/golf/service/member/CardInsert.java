package is.idega.idegaweb.golf.service.member;

import is.idega.idegaweb.golf.entity.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.TextInput;
import com.idega.util.*;
import java.util.*;
import java.sql.*;
import java.io.*;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
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
public class CardInsert extends EntityInsert{
  private Card eCard;
  private TextInput nameInput;
  private TextInput socialInput;
  private TextInput numberInput;
  private DropdownMenu typeDrop;
  private DropdownMenu expireMonth;
  private DropdownMenu expireYear;

  private String ownerName = "CardInsert_name";
  private String socialName = "CardInsert_social";
  private String numberName = "CardInsert_number";
  private String typeName = "CardInsert_type";
  private String expireMonthName = "CardInsert_expiremonth";
  private String expireYearName = "CardInsert_expireyear";

  private String ownerValue = null;
  private String socialValue = null;
  private String numberValue = null;
  private String typeValue = null;
  private String expireMonthValue = null;
  private String expireYearValue = null;
  private String headerText = "Kortaupplýsingar";

  public CardInsert() {
    eCard = new Card();
    this.bUpdate = false;
    nameInput = new TextInput(ownerName);
    socialInput = new TextInput(socialName);
    numberInput = new TextInput(numberName);
    typeDrop = typeDropDown(typeName, "");
    setExireDate(null);
    init();
  }

  public CardInsert(Card card)throws SQLException {
    this.bUpdate = true;
    eCard = card;
    if(card.getName() != null)
        nameInput = new TextInput(ownerName, eCard.getName());
    else
        nameInput = new TextInput(ownerName);
    if(card.getSocialSecurityNumber() != null)
        socialInput = new TextInput(socialName, eCard.getSocialSecurityNumber());
    else
        socialInput = new TextInput(socialName);
    if(card.getCardNumber() != null)
        numberInput = new TextInput(numberName, eCard.getCardNumber());
    else
        numberInput = new TextInput(numberName);

    if(card.getCardType() != null)
        typeDrop = typeDropDown(typeName, eCard.getCardType());
    else
        typeDrop = typeDropDown(typeName, "");
    setExireDate(card.getExpireDate());
    init();
  }
  private void init(){
    setStyle(nameInput);
    setStyle(socialInput);
    setStyle(numberInput);
    setStyle(typeDrop);
    setStyle(expireMonth);
    setStyle(expireYear);
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
      return eCard;
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

  public DropdownMenu getTypeDrop() {
      return this.typeDrop;
  }

  public boolean areNeededFieldsEmpty(IWContext iwc) {
      if(areAllFieldsEmpty(iwc))
          return false;
      else return areSomeFieldsEmpty(iwc);

  }

  public boolean areAllFieldsEmpty(IWContext iwc)  {
      boolean isEmpty = true;
      isEmpty = (isInvalid(expireMonthValue) && isInvalid(expireYearValue)/*expireValue == null*/ && isEmpty(iwc,ownerName) &&
              isEmpty(iwc,numberName) && isEmpty(iwc,socialName) && isEmpty(iwc,typeName));
      return isEmpty;
  }

  public boolean areSomeFieldsEmpty(IWContext iwc) {
    return (isInvalid(expireMonthValue) || isInvalid(expireYearValue) || (ownerValue == null) ||
      (numberValue == null) || (socialValue == null) || (typeValue == null) || ! ownerValue.equals("") ||
      ! numberValue.equals("") || ! socialValue.equals("") || ! typeValue.equals(""));
  }

  public void setVariables(IWContext iwc) {
    ownerValue = getValue(iwc,ownerName);
    numberValue = getValue(iwc,numberName);
    socialValue = getValue(iwc,socialName);
    typeValue = getValue(iwc,typeName);
    expireMonthValue = getValue(iwc,expireMonthName);
    expireYearValue = getValue(iwc,expireYearName);

    if (numberValue  != null) {
        eCard.setCardNumber(numberValue);
    }
    if(! isInvalid(expireMonthValue) && ! isInvalid(expireYearValue)) {
        idegaTimestamp stamp = new idegaTimestamp("1", expireMonthValue, expireYearValue);
        eCard.setExpireDate(stamp.getSQLDate());
    }
    if (typeValue  != null) {
        eCard.setCardType(typeValue);
    }
    if (ownerValue != null) {
        eCard.setName(ownerValue);
    }
    if (numberValue  != null) {
        eCard.setCardNumber(numberValue);
    }
    if(socialValue != null) {
        eCard.setSocialSecurityNumber(socialValue);
    }
  }

  public Vector getNeededEmptyFields(IWContext iwc){
    return getEmptyFields();
  }

  public Vector getEmptyFields() {
    Vector vec = new Vector();
    if (isInvalid(expireMonthValue) || isInvalid(expireYearValue)) {
        vec.addElement("Gildistími");
    }
    if (isInvalid(ownerValue)) {
        vec.addElement("Korthafi");
    }
    if (isInvalid(numberValue)) {
        vec.addElement("Kortanúmer");
    }
    if (isInvalid(socialValue)) {
        vec.addElement("Kennitala");
    }
    if (isInvalid(typeValue)) {
        vec.addElement("Tegund korts");
    }
    return vec;
  }


  public void store(IWContext iwc)throws SQLException, IOException {
      setVariables(iwc);
      Vector vError = getEmptyFields();
      int errSize = vError.size();
      if((! bUpdate) && errSize == 5) {
          return;
      }
      else if((! bUpdate) && errSize > 0) {
          return;
      }
      if(bUpdate) {
          eCard.update();
      }
      else {
          eCard.insert();
      }
  }


  public BorderTable getInputTable() {

    BorderTable hTable = new BorderTable();
    Table table = new Table(2, 5);
    hTable.add(table);

    table.add(formatText("Kennitala"), 1, 1);
    table.add(getSocialSequrityNumberInput(), 2, 1);

    table.add(formatText("Gildistími"), 1, 2);
    table.add(getDropMonth(), 2, 2);
    table.add(getDropYear(), 2, 2);

    table.add(formatText("Korthafi"), 1, 3);
    table.add(getNameInput(), 2, 3);

    table.add(formatText("Kortanúmer"), 1, 4);
    table.add(getNumberInput(), 2, 4);

    table.add(formatText("Tegund"), 1, 5);
    table.add(getTypeDrop(), 2, 5);

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

  private DropdownMenu typeDropDown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);

        drp.addMenuElement("", "Tegund");
        drp.addMenuElement("visa", "Vísa");
        drp.addMenuElement("eurocard", "Eurocard");
        drp.setSelectedElement(selected);

	return drp;
  }
}



