package com.idega.projects.golf.service.member;
import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.service.*;
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


  public class UnionMemberInfoInsert extends EntityInsert {

  private UnionMemberInfo member;

  private DropdownMenu dropFamilyStatus;
  private DropdownMenu dropNumberOfPaiments;
  private TextInput inputLocker;
  private DateInput dateInputFirstPayday;
  private TextArea areaComment;
  private DropdownMenu dropVisible;
  private DropdownMenu dropPaymentTypes;
  private DropdownMenu dropCatalogue;
  private DropdownMenu dropActive;

  private final String dropFamilyStatusName = "UnionMemberInfoInsert_family_status";
  private final String dropNumberOfPaimentsName = "UnionMemberInfoInsert_numpayments";
  private final String inputLockerName = "UnionMemberInfoInsert_locker";
  private final String dateInputFirstPaydayName = "UnionMemberInfoInsert_firstpayday";
  private final String areaCommentName = "UnionMemberInfoInsert_comment";
  private final String dropVisibleName = "UnionMemberInfoInsert_isvisible";
  private final String dropPaymentTypesName = "UnionMemberInfoInsert_paymenttypes";
  private final String dropCatalogueName = "UnionMemberInfoInsert_catalogue";
  private final String dropActiveName = "UnionMemberInfoInsert_isactive";

  private String dropFamilyStatusValue;
  private String dropNumberOfPaimentsValue;
  private String inputLockerValue;
  private Date dateInputFirstPaydayValue;
  private String areaCommentValue;
  private String dropVisibleValue;
  private String dropPaymentTypesValue;
  private String dropCatalogueValue;
  private String dropActiveValue;


  public UnionMemberInfoInsert(ModuleInfo modinfo) throws java.sql.SQLException{
      super(modinfo);
      isUpdate = false;
      GregorianCalendar cal = new GregorianCalendar();
      dateInputFirstPayday = new DateInput(dateInputFirstPaydayName, true);
      dateInputFirstPayday.setDay(1);
      dateInputFirstPayday.setMonth(2);
      //dateInputFirstPayday.setYearRange(cal.get(cal.YEAR), cal.get(cal.YEAR));
      dateInputFirstPayday.setYear(cal.get(cal.YEAR));

      member = new UnionMemberInfo();
      member.setRegistrationDate(new Date(System.currentTimeMillis()));

      inputLocker = new TextInput(inputLockerName);
      inputLocker.setSize(15);
      areaComment = new TextArea(areaCommentName);
      dropVisible = visibleDropdown(dropVisibleName, "Y");
      dropPaymentTypes = paymentTypeDrop(dropPaymentTypesName, "1");
      dropCatalogue = catalogueDrop(dropCatalogueName, "0");
      dropActive = activeDropdown(dropActiveName, "a");
      dropNumberOfPaiments = numberOfPaymentDrop(dropNumberOfPaimentsName, "0");
      dropFamilyStatus = familyStatusDropdown(dropFamilyStatusName, "höfuð");
      //setVariables();
  }

  public UnionMemberInfoInsert(ModuleInfo modinfo, int unionMemInfoId, int memberID, int unionID)throws SQLException {
      super(modinfo, unionMemInfoId);
      isUpdate = true;
      GregorianCalendar cal = new GregorianCalendar();
      member = new UnionMemberInfo(unionMemInfoId);
      setMemberId(memberID);
      setUnionId(unionID);
      dateInputFirstPayday = new DateInput(dateInputFirstPaydayName, true);
      //dateInputFirstPayday.setYearRange(cal.get(cal.YEAR), cal.get(cal.YEAR));
      dateInputFirstPayday.setYear(cal.get(cal.YEAR));

      if(member.getMemberStatus() != null) {
          //debug
          dropActive = activeDropdown(dropActiveName, member.getMemberStatus());
          //dropActive = activeDropdown(dropActiveName, "a");
      }
      else
          dropActive = activeDropdown(dropActiveName, "a");
      if(member.getFamilyStatus() != null)
          dropFamilyStatus = familyStatusDropdown(dropFamilyStatusName, member.getFamilyStatus());
      else
          dropFamilyStatus = familyStatusDropdown(dropFamilyStatusName, "höfuð");
      if(member.getPreferredInstallmentNr() != -1)
          dropNumberOfPaiments = numberOfPaymentDrop(dropNumberOfPaimentsName, String.valueOf(member.getPreferredInstallmentNr()));
      else
          dropNumberOfPaiments = numberOfPaymentDrop(dropNumberOfPaimentsName, "0");
      if(member.getLockerNumber() != null)
          inputLocker = new TextInput(inputLockerName, member.getLockerNumber());
      else
          inputLocker = new TextInput(inputLockerName);
      if(member.getComment() != null)
          areaComment = new TextArea(areaCommentName, member.getComment());
      else
          areaComment = new TextArea(areaCommentName);

      if(member.getVisible())
          dropVisible = visibleDropdown(dropVisibleName, "Y");
      else
          dropVisible = visibleDropdown(dropVisibleName, "N");
      if(member.getPaymentTypeID() != -1)
          dropPaymentTypes = paymentTypeDrop(dropPaymentTypesName, String.valueOf(member.getPaymentTypeID()));
      else
          dropPaymentTypes = paymentTypeDrop(dropPaymentTypesName, "1");
      if(member.getFirstInstallmentDate() != null) {
          dateInputFirstPayday.setDate(member.getFirstInstallmentDate());
      }
      else {
          dateInputFirstPayday.setDay(1);
          dateInputFirstPayday.setMonth(2);
      }
      inputLocker.setSize(15);
      dropCatalogue = catalogueDrop(dropCatalogueName, String.valueOf(member.getPriceCatalogueID()));
  }

 /* public TextInput getInputMemberNumber() {
      return inputNumber;
  }*/

  public UnionMemberInfo getUnionMemberInfo() {
      return member;
  }

  public void setUnionId(int id ) {
      this.member.setUnionID(id);
  }

  public void setMemberId(int id ) {
      this.member.setMemberID(id);
  }

  public void setCardId(int id ) {
      this.member.setCardId(id);
  }

  public DropdownMenu getDropdownNumberOfPayments() {
      return this.dropNumberOfPaiments;
  }

  public DropdownMenu getDropdownFamilyStatus() {
      return this.dropFamilyStatus;
  }

  public TextInput getInputLocker() {
      return this.inputLocker;
  }

  public DateInput getDateInputFirstPaymentDate() {
      return dateInputFirstPayday;
  }

  public DropdownMenu getDropVisible() {
      return this.dropVisible;
  }

  public DropdownMenu getDropCatalogue() {
      return dropCatalogue;
  }

  public DropdownMenu getDropMemberStatus() {
      return dropActive;
  }

  public DropdownMenu getDropdownPaymentType() {
      return dropPaymentTypes;
  }

  public TextArea getAreaComment() {
      return areaComment;
  }

  public Vector getEmptyFields() {
      Vector vec = new Vector();

      if ( isInvalid(inputLockerValue)) {
          vec.addElement("Skápanúmer");
      }
      if (! isDateInputValid(dateInputFirstPaydayName)) {
          vec.addElement("Dagsetning fyrstu afborgunar");
      }

      return vec;
  }

  public Vector getNeetedEmptyFields() {
      return new Vector();
  }

  public boolean areSomeFieldsEmpty() {
      return (getEmptyFields().size() > 0);
  }

  public boolean areNeetedFieldsEmpty() {
    return (getNeetedEmptyFields().size() > 0);
  }

  public void store() throws java.io.IOException, java.sql.SQLException {
      setVariables();

      if(isUpdate())
          member.update();
      else {
          if(member.getUnionID() == 0 || member.getUnionID() == -1 || member.getMemberID() == 0 || member.getMemberID() == -1)
              throw new SQLException("<br><B>ABB ABB BABB!!! SETTU UNION_ID OG/EÐA MEMBER_ID GILDI A KALLINN</B><BR>");
          int nextNumber = member.getMaxColumnValue("member_number", "union_id", String.valueOf(member.getUnionID()));
          nextNumber++;
          member.setMemberNumber(nextNumber);
          member.insert();
      }
  }


    public HeaderTable getInputTable() {

      Table table = null;

      Link link = Tariffer.getExtraCatalogueLink("Skrá Gjaldflokk", String.valueOf(member.getUnionID()));
      link.setObject(new Image("/pics/flipar_takkar/form_takkar/skra.gif"));

      if(isUpdate()) {
          table = new Table(2, 10);
          table.add("Númer", 1, 1);
          table.add("Staða", 1, 2);
          table.add("Greiðslufjöldi", 1, 3);
          table.add("Fyrsta borgun", 1, 4);
          table.add("Greiðslumáti", 1, 5);
          table.add("Vefur", 1, 6);
          table.add("Skápur", 1, 7);
          table.add("Fjölskyldustaða", 1, 8);
          table.add("Gjaldflokkar", 1, 9);

          table.add(String.valueOf(member.getMemberNumber()), 2, 1);
          table.add(getDropMemberStatus(), 2, 2);
          table.add(getDropdownNumberOfPayments(), 2, 3);
          table.add(getDateInputFirstPaymentDate(), 2, 4);
          table.add(getDropdownPaymentType(), 2, 5);
          table.add(getDropVisible(), 2, 6);
          table.add(getInputLocker(), 2, 7);
          table.add(getDropdownFamilyStatus(), 2, 8);
          table.add(getDropCatalogue(), 2, 9);
          table.add(link, 2, 10);
      }
      else {
          table = new Table(2, 9);
          table.add("Staða", 1, 1);
          table.add("Greiðslufjöldi", 1, 2);
          table.add("Fyrsta borgun", 1, 3);
          table.add("Greiðslumáti", 1, 4);
          table.add("Vefur", 1, 5);
          table.add("Skápur", 1, 6);
          table.add("Fjölskyldustaða", 1, 7);
          table.add("Gjaldflokkar", 1, 8);

          table.add(getDropMemberStatus(), 2, 1);
          table.add(getDropdownNumberOfPayments(), 2, 2);
          table.add(getDateInputFirstPaymentDate(), 2, 3);
          table.add(getDropdownPaymentType(), 2, 4);
          table.add(getDropVisible(), 2, 5);
          table.add(getInputLocker(), 2, 6);
          table.add(getDropdownFamilyStatus(), 2, 7);
          table.add(getDropCatalogue(), 2, 8);
          table.add(link, 2, 9);
      }
      //table.setWidth(2, "800");
      HeaderTable hTable = new HeaderTable();
      hTable.setHeaderText("Félags upplýsingar");

      hTable.add(table);

      return hTable;
    }

  public void setVariables() {
      dropNumberOfPaimentsValue = getValue(dropNumberOfPaimentsName);
      inputLockerValue = getValue(inputLockerName);
      dateInputFirstPaydayValue = getDateFromInput(dateInputFirstPaydayName);
      areaCommentValue = getValue(areaCommentName);
      dropVisibleValue = getValue(dropVisibleName);
      dropPaymentTypesValue = getValue(dropPaymentTypesName);
      dropCatalogueValue = getValue(dropCatalogueName);
      dropActiveValue = getValue(dropActiveName);
      dropFamilyStatusValue = getValue(dropFamilyStatusName);
      setEntity();
  }

  private void setEntity() {
      if (! isInvalid(dropNumberOfPaimentsValue)) {
          if(isDigitOnly(dropNumberOfPaimentsValue))
              member.setPreferredInstallmentNr(new Integer(dropNumberOfPaimentsValue));
      }
      if (inputLockerValue != null) {
          member.setLockerNumber(inputLockerValue);
      }
      if (dateInputFirstPaydayValue != null) {
          member.setFirstInstallmentDate(dateInputFirstPaydayValue);
      }
      /*if (! isInvalid(areaCommentValue)) {
          member.setComment(areaCommentValue);
      }*/
      if (! isInvalid(dropVisibleValue)) {
          if(dropVisibleValue.equalsIgnoreCase("Y"))
              member.setVisible(true);
          else
              member.setVisible(false);
      }
      if (! isInvalid(dropPaymentTypesValue)) {
          member.setPaymentTypeID(new Integer(dropPaymentTypesValue));
      }
      if(! isInvalid(dropCatalogueValue)) {
          member.setPriceCatalogueID(Integer.parseInt(dropCatalogueValue));
      }
      member.setMemberStatus(dropActiveValue);
      member.setFamilyStatus(dropFamilyStatusValue);

  }

  public boolean areAllFieldsEmpty() {
      return (getEmptyFields().size() == 7);
  }

  public DropdownMenu paymentTypeDrop(String name, String selected)throws java.sql.SQLException {
	DropdownMenu drp = new DropdownMenu(name);
	PaymentType type = new PaymentType();
        PaymentType[] types = (PaymentType[]) type.findAll();

        for( int i=0;i<types.length;i++){
            drp.addMenuElement(types[i].getID(),types[i].getName());
        }
        drp.setSelectedElement(selected);
	return drp;
  }

  public DropdownMenu catalogueDrop(String name, String selected)throws java.sql.SQLException {
	if(selected.equals("-1"))
            selected = "0";
        DropdownMenu drp = new DropdownMenu( name);
        drp.addMenuElement(0, "Enginn sér gjaldflokkur er valinn");
        List list = Tariffer.getExtraCatalogList(String.valueOf(member.getUnionID()));
        if(list != null) {
            ListIterator iter = list.listIterator();
            PriceCatalogue cate = null;

            while(iter.hasNext()) {
                cate = (PriceCatalogue) iter.next();
                drp.addMenuElement(cate.getID(), cate.getName());
            }
            drp.setSelectedElement(selected);
        }
	return drp;
  }

  public DropdownMenu numberOfPaymentDrop(String name, String selected)throws java.sql.SQLException {

        DropdownMenu drp = new DropdownMenu( name);
        drp.addMenuElement(0, "Óskilgreint");

        for(int i = 1; i < 13; i++) {
            drp.addMenuElement(i, String.valueOf(i));
        }
        drp.setSelectedElement(selected);
	return drp;
  }


  public DropdownMenu activeDropdown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);
	drp.addMenuElement("a", "Virkur meðlimur");
	drp.addMenuElement("i", "Óvirkur meðlimur");
        drp.addMenuElement("w", "Í bið");
        drp.addMenuElement("q", "Hættur");
        drp.addMenuElement("d", "Látinn");
        drp.setSelectedElement(selected);
	return drp;
  }

  public DropdownMenu familyStatusDropdown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);
	drp.addMenuElement("höfuð", "Höfuð");
	drp.addMenuElement("maki", "Maki");
        drp.addMenuElement("barn", "barn");
        drp.setSelectedElement(selected);
	return drp;
  }

  public DropdownMenu visibleDropdown(String name, String selected) {
	DropdownMenu drp = new DropdownMenu(name);
	drp.addMenuElement("Y", "Sjást á vef");
	drp.addMenuElement("N", "Ekki sjást á vef");
        drp.setSelectedElement(selected);
	return drp;
  }


}