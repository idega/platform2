package com.idega.block.mailinglist.presentation;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.text.Link;
import java.sql.SQLException;
import com.idega.block.mailinglist.data.*;
import com.idega.block.mailinglist.business.MailingListBusiness;
import com.idega.data.EntityFinder;

public class MailinglistPresentationAdministrator extends Block {
  int numberOfRows;
  int reply;
  String addAndRemoveParameter =  "addAndRemoveParameter";
  String addMailinglistParameterValue =  "addMailinglistParameterValue";
  String removeMailinglistParameterValue =  "removeMailinglistParameterValue";
  String newMailinglistTextInputName = "mailinglistTextInput";
  String dropdownMenuName = "dropdownMenuName";
  String checkBoxName = "checkbox";
  String emailLetterListName = "emailLetterListName";
  String generalEmailHandleParameter = "generalEmailHandleParameter";
  String viewParameterValue = "viewParameterValue";
  String newEmailHandleParameter = "newEmailHandleParameter";
  String saveParameterValue = "saveParameterValue";
  String newEmailAreaName = "newEmailAreaName";
  String saveAndSendParameterValue = " saveAndSendParameterValue";
  String emailListTextAreaName = "emailListTextAreaName";
  String activeMailinglist;

  public MailinglistPresentationAdministrator() {
  }

  public void wholeTableDraw (IWContext modinfo) throws SQLException{

    MailinglistPresentation plp = new MailinglistPresentation();
    Table wholeTable = new Table(2,4);

    wholeTable.mergeCells(1, 1, 1, 2);
    wholeTable.mergeCells(1, 3, 1, 4);
    wholeTable.add(this.checkBoxForm(), 1, 1);
    wholeTable.add(plp, 2, 4);
    wholeTable.add(this.mailinglistChooserForm(modinfo), 2, 1);
    wholeTable.add(this.emailViewForm(modinfo), 2, 2);
    wholeTable.add(this.newEmailForm(modinfo), 2, 3);
   // wholeTable.add(this.emailSelectionBox(), 1, 3);
    add(wholeTable);
  }

  public Form checkBoxForm () throws SQLException{

    TextInput textInput = new TextInput(newMailinglistTextInputName);

    Table listOfMailinglistsTable = new Table();
    Table postingTable = new Table();
    Table checkBoxTable = new Table( 2, 3);

    //SubmitButton addMailinglistButton = new SubmitButton( "Stofna Póstlista", addMailinglistParameterValue, "");
    //SubmitButton removeMailinglistButton = new SubmitButton( "Fjarlægja Póstlista", removeMailinglistParameterValue, "");

    SubmitButton addMailinglistButton = new SubmitButton( addMailinglistParameterValue, "Stofna Póstlista");
    SubmitButton removeMailinglistButton = new SubmitButton( removeMailinglistParameterValue, "Fjarlægja Póstlista");

    Parameter addAndRemovecontrol = new Parameter(addAndRemoveParameter,"submit");

    CheckBox checkBoxes;

    Form checkBoxForm = new Form();

    checkBoxForm.maintainAllParameters();

    checkBoxForm.add(addAndRemovecontrol);

    Mailinglist postList = new Mailinglist();
    Mailinglist[] postListArray = null;

    postListArray = (Mailinglist[]) postList.findAll();

    if (postListArray != null){
      checkBoxTable = new Table(2, postListArray.length + 3);
      for (int i = 0; i < postListArray.length; i++) {
        checkBoxes = new CheckBox(checkBoxName, String.valueOf(postListArray[i].getID()));
        checkBoxTable.add(checkBoxes, 2, i+1);
        checkBoxTable.add(postListArray[i].getStringColumnValue("Post_list")+" ", 1, i+1);
      }
    }
    else{
      checkBoxTable = new Table( 2, 3);
    }
    numberOfRows = checkBoxTable.getRows();

    checkBoxTable.mergeCells( 1, numberOfRows - 2, 2, numberOfRows - 2);
    checkBoxTable.mergeCells( 1, numberOfRows - 1, 2, numberOfRows - 1);
    checkBoxTable.mergeCells( 1, numberOfRows, 2, numberOfRows);

    checkBoxTable.add(textInput, 1, numberOfRows - 2);
    checkBoxTable.add(addMailinglistButton, 1, numberOfRows - 1);
    checkBoxTable.add(removeMailinglistButton, 1, numberOfRows);

    checkBoxForm.add(checkBoxTable);
    return (checkBoxForm);
  }

  private void preCheckBoxBusiness(IWContext modinfo) throws SQLException{

    String[] checkedBoxes;
    checkedBoxes = (String[]) modinfo.getParameterValues(checkBoxName);
    boolean hasSubmittedText = modinfo.isParameterSet(newMailinglistTextInputName);

    //if ((checkedBoxes != null) || hasSubmittedText) {
    if (modinfo.isParameterSet(addAndRemoveParameter)){
      if (checkedBoxes != null){
        if (modinfo.isParameterSet(removeMailinglistParameterValue)){
          reply = MailingListBusiness.removeMailinglistBusiness(modinfo, checkedBoxes);
          for (int i = 0; i < checkedBoxes.length; i++) {
            add(checkedBoxes[i]+" ");
          }
        }
      }
      if(modinfo.isParameterSet(addMailinglistParameterValue)){
        String postListRemoveName = modinfo.getParameter(newMailinglistTextInputName);
        if(!"".equalsIgnoreCase(postListRemoveName)){
          reply = MailingListBusiness.addMailinglistBusiness(modinfo, postListRemoveName);
          add("Bæta við");
        }
        else{
          add("nothin man");
        }
      }
    }
    else {
      add("gottya");
    }

  }

  public Form mailinglistChooserForm(IWContext modinfo) throws SQLException{
    String chosen;
    Mailinglist mailinglist = new Mailinglist();
    Form postListChooserForm = new Form();
    postListChooserForm.maintainAllParameters();
    Table mailinglistChooserTable = new Table(3,1);
    DropdownMenu dropDownMenu = new DropdownMenu(dropdownMenuName);
    dropDownMenu.addSeparator();
    dropDownMenu.addMenuElements(EntityFinder.findAll(mailinglist));
    //dropDownMenu.addMenuElementFirst("makes no diff", "Bite My Shiny Metal Ass");

    dropDownMenu.setToSubmit();
    dropDownMenu.keepStatusOnAction();
    chosen = modinfo.getParameter(dropdownMenuName);
    mailinglistChooserTable.add(dropDownMenu, 2, 1);
    System.err.println("dropdownMenuName = "+modinfo.getParameter(dropdownMenuName));
    if((chosen != null) && (!"".equals(chosen))){
      Mailinglist chosenMailinglist = new Mailinglist( Integer.parseInt(chosen));
      mailinglistChooserTable.add( chosenMailinglist.getName(), 1, 1);
      mailinglistChooserTable.add(this.emailSelectionBox(chosenMailinglist), 3, 1);
    }
/*    else{
      Mailinglist chosenMailinglist = new Mailinglist( Integer.parseInt(oldChosen));
      mailinglistChooserTable.add( chosenMailinglist.getName(), 1, 1);
      mailinglistChooserTable.add(this.emailSelectionBox(chosenMailinglist), 3, 1);
    }*/
    postListChooserForm.add(mailinglistChooserTable);
    return (postListChooserForm);
  }

  public Form emailViewForm(IWContext modinfo) throws SQLException {

    SubmitButton viewButton = new SubmitButton("Skoða Bréf", generalEmailHandleParameter, viewParameterValue);
    ViewWindow viewEmailWindow = new ViewWindow();

    Form emailViewForm = new Form(viewEmailWindow);
    //emailViewForm.maintainAllParameters();
    Link test = new Link(viewEmailWindow);
    emailViewForm.add(test);
    TextArea emailLetterListTextArea = new TextArea(emailLetterListName, 40, 6);
    Table emailViewTable = new Table(1,2);

    emailViewTable.add(viewButton, 1, 2);
    emailViewTable.add(emailLetterListTextArea, 1, 1);
    emailViewForm.add(emailViewTable);

    return (emailViewForm);
  }

  public Form newEmailForm(IWContext modinfo) throws SQLException{

    SubmitButton saveButton = new SubmitButton("Vista Bréf", newEmailHandleParameter, saveParameterValue);
    SubmitButton saveAndSendButton = new SubmitButton("Vista og Senda Bréf", newEmailHandleParameter, saveAndSendParameterValue);
    Form newEmailForm = new Form();
    //newEmailForm.maintainAllParameters();
    TextArea newEmailTextArea = new TextArea(newEmailAreaName, 40, 10);
    Table newEmailTable = new Table( 3, 2);
    newEmailTable.mergeCells( 1, 1, 3, 1);

    newEmailTable.add(saveButton, 2, 2);
    newEmailTable.add(saveAndSendButton, 3, 2);
    newEmailTable.add(newEmailTextArea, 1, 1);
    newEmailForm.add(newEmailTable);

    return (newEmailForm);
  }

  public SelectionBox emailSelectionBox(Mailinglist chosenMailinglist) throws SQLException{

    SelectionBox emailSelectionBox = new SelectionBox();
    MailAccount emailList = new MailAccount();
    MailAccount[] emailListArray;
    emailListArray = (MailAccount[]) emailList.findAllOrdered(MailAccount.EMAIL);
    if (!((emailListArray == null) || (emailListArray.length == 0))) {
      System.out.println("LENGD Á emailListArray = "+emailListArray.length);
      emailListArray = (MailAccount[]) EntityFinder.findRelated(chosenMailinglist, emailList).toArray( new MailAccount[0]);
      if (!((emailListArray == null) || (emailListArray.length == 0))) {
        emailSelectionBox.addMenuElements(emailListArray, emailList.EMAIL);
      }
    }
    emailSelectionBox.setHeight(3);
    return (emailSelectionBox);
  }

  public void main(IWContext modinfo) throws SQLException{

    this.preCheckBoxBusiness(modinfo);
    this.wholeTableDraw(modinfo);
    //String.valueOf(postListArray[1].getID())
  }
}