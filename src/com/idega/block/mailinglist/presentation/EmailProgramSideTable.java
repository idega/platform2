package com.idega.block.mailinglist.presentation;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import com.idega.block.mailinglist.business.EmailServiceHandler;
import com.idega.block.mailinglist.data.EmailLetterData;
import com.idega.block.mailinglist.data.MailAccount;
import com.idega.block.mailinglist.data.Mailinglist;
import com.idega.data.EntityFinder;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class EmailProgramSideTable extends Block {

  public static final String inboxButtonName = "inboxButtonName";
  public static final String sentButtonName = "sentButtonName";
  public static final String draftsButtonName = "draftsButtonName";
  public static final String newLetterButtonName = "newLetterButtonName";
  public static final String newLetterOnMailinglistButtonName = "newLetterOnMailinglistButtonName";
  public static final String mailinglistSettingsButtonName = "mailinglistSettingsButtonName";
  public static final String mailinglistDropDownMenuName = "mailinglistDropDownMenuName";
  public static final String submitParameterValue = "submitParameterValue";
  public static final String submitParameterName = "submitParameterName";
  public static final String emailSelectionBoxName = "emailSelectionBoxName";
  public static String chosen = new String();


  SelectionBox emailSelectionBox = new SelectionBox(emailSelectionBoxName);

  SubmitButton inboxButton = new SubmitButton(inboxButtonName, "Inbox");
  SubmitButton sentButton = new SubmitButton(sentButtonName, "Sent");
  SubmitButton draftsButton = new SubmitButton(draftsButtonName, "Drafts");
  SubmitButton newLetterButton = new SubmitButton(newLetterButtonName, "New Letter");
  SubmitButton mailinglistSettingsButton = new SubmitButton(mailinglistSettingsButtonName, "Edit Mailinglist");
  SubmitButton newLetterOnMailinglistButton = new SubmitButton(newLetterOnMailinglistButtonName, "Write to Selected");
  Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
  DropdownMenu mailinglistMenu = new DropdownMenu( mailinglistDropDownMenuName);
  Table sideTable = new Table(2,9);
  EmailLetterData letters = ((com.idega.block.mailinglist.data.EmailLetterDataHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetterData.class)).createLegacy();
  //InboxData inboxLetters = new InboxData();

  public EmailProgramSideTable(){

  }

  public void setSelectionBox(IWContext modinfo) throws SQLException{
   sideTable.add(emailSelectionBox(modinfo),1,7);
  }

  public void setShowNumberOfLetters(IWContext iwc) throws SQLException{
    EmailLetterData[] unSentLetters;
    EmailLetterData[] sentLetters;

    sentLetters = (EmailLetterData[]) letters.findAllByColumn(com.idega.block.mailinglist.data.EmailLetterDataBMPBean.EMAIL_LETTER_DATA_SENT, "Y");
    unSentLetters = (EmailLetterData[]) letters.findAllByColumn(com.idega.block.mailinglist.data.EmailLetterDataBMPBean.EMAIL_LETTER_DATA_SENT, "N");

    try {
      sideTable.add(String.valueOf(EmailServiceHandler.countMessages(iwc)), 2, 1);
    }
    catch (MessagingException ex) {

    }

    if (sentLetters != null) sideTable.add(String.valueOf(sentLetters.length), 2, 2);
    if (unSentLetters != null) sideTable.add(String.valueOf(unSentLetters.length), 2, 3);
  }

  public SelectionBox emailSelectionBox(IWContext modinfo) throws SQLException{

    Mailinglist chosenMailinglist;
    chosenMailinglist = getChosenMailinglist(modinfo);
    MailAccount email = ((com.idega.block.mailinglist.data.MailAccountHome)com.idega.data.IDOLookup.getHomeLegacy(MailAccount.class)).createLegacy();
    List emailList;
    emailList = (List) EntityFinder.findRelatedOrdered(chosenMailinglist, email, com.idega.block.mailinglist.data.MailinglistBMPBean.EMAIL, true);
    emailSelectionBox.addMenuElements(emailList, com.idega.block.mailinglist.data.MailAccountBMPBean.EMAIL);
    if (emailList != null){
      Iterator emailIterator = emailList.iterator();
      while (emailIterator.hasNext()) {
           emailSelectionBox.setSelectedElement( String.valueOf( ((MailAccount) emailIterator.next()).getID()));
      }
    }
    return (emailSelectionBox);
  }

  public Mailinglist getChosenMailinglist(IWContext modinfo) throws SQLException{
    chosen = modinfo.getParameter(mailinglistDropDownMenuName);
    System.err.println("Jamm þetta sem ven fær gildi!!! chosen = "+chosen);
    if((chosen != null) && !("".equalsIgnoreCase(chosen))){
      return( ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).findByPrimaryKeyLegacy(Integer.parseInt(chosen)));
    }
    else{
      //return( ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).findByPrimaryKeyLegacy(1));  SETJA AFTUR INN!!!! LAGA
        return ( ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy());
    }
  }

  public void main(IWContext modinfo) throws SQLException{

    mailinglistMenu.keepStatusOnAction();
//    mailinglistMenu.addSeparator();
    mailinglistMenu.addMenuElements(EntityFinder.findAllOrdered(mailinglist,com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME));
    mailinglistMenu.setToSubmit();
    System.err.println("modinfo.getParameter(mailinglistDropDownMenuName = "+modinfo.getParameter(mailinglistDropDownMenuName));
    sideTable.add(inboxButton,1,1);
    sideTable.add(sentButton,1,2);
    sideTable.add(draftsButton,1,3);
    sideTable.add(mailinglistMenu,1,4);
    //sideTable.add(new HiddenInput(mailinglistSettingsButtonName, "Edit Mailinglist"));
    sideTable.add(newLetterOnMailinglistButton,1,8);
    sideTable.add(newLetterButton,1,9);
    sideTable.add(mailinglistSettingsButton,1,5);
    //Form testForm = new Form();
    //testForm.add(sideTable);
    add(sideTable);
  }
}
