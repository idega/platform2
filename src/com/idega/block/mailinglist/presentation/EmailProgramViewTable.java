package com.idega.block.mailinglist.presentation;

import java.io.IOException;
import java.sql.SQLException;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.idega.block.mailinglist.business.EmailServiceHandler;
import com.idega.block.mailinglist.data.EmailLetterData;
import com.idega.block.mailinglist.data.MailAccount;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class EmailProgramViewTable extends Block {

  public static final String addressInputName = "addressInputName";
  public static final String subjectInputName = "subjectInputName";
  public static final String CCInputName = "CCInputName";
  public static final String BCCInputName = "BCCInputName";
  public static final String adressFromInputName = "adressFromInputName";
  public static final String sendLetterButtonName = "sendLetterButtonName";
  public static final String saveLetterButtonName = "saveLetterButtonName";
  public static final String forwardLetterButtonName = "forwardLetterButtonName";
  public static final String replyLetterButtonName = "replyLetterButtonName";
  public static final String editAttachmentsButtonName = "editAttachmentsButtonName";
  public static final String letterTextAreaName = "letterTextAreaName";
  public static final String emailSelectionBoxName = "emailSelectionBoxName";

  Table writeLetterViewTable = new Table(3,7);
  SubmitButton sendLetterButton = new SubmitButton(sendLetterButtonName, "Send");
  SubmitButton saveLetterButton = new SubmitButton(saveLetterButtonName, "Save");
  SubmitButton editAttachmentsButton = new SubmitButton(editAttachmentsButtonName, "Edit Attachments");

  TextInput addressInput = new TextInput(addressInputName);
  TextInput subjectInput = new TextInput(subjectInputName);
  TextInput CCInput = new TextInput(CCInputName);
  TextInput BCCInput = new TextInput(BCCInputName);
  TextArea letterTextArea = new TextArea(letterTextAreaName, 40,10);

  Table ccTable = new Table(4,1);


  public EmailProgramViewTable() {
  }

  //CLEAR NEW LETTTER
  public void setClearNewLetter(){

  }

  //NEW LETTER
  public void setNewLetterOnChosenEmails(IWContext modinfo) throws SQLException{
    String adress = new String(" ");
    String ccAdresses = new String(" ");

    String[] selectedIDStrings = null;
    selectedIDStrings = modinfo.getParameterValues(EmailProgramSideTable.emailSelectionBoxName);

    if (selectedIDStrings != null){
      int selectedIDStringsLength = selectedIDStrings.length;
      MailAccount mailList = ((com.idega.block.mailinglist.data.MailAccountHome)com.idega.data.IDOLookup.getHomeLegacy(MailAccount.class)).findByPrimaryKeyLegacy(Integer.parseInt(selectedIDStrings[0]));
      adress = mailList.getEmail();
      for (int i = 1; i < selectedIDStringsLength -1; i++) {
        mailList = ((com.idega.block.mailinglist.data.MailAccountHome)com.idega.data.IDOLookup.getHomeLegacy(MailAccount.class)).findByPrimaryKeyLegacy(Integer.parseInt(selectedIDStrings[i]));
        ccAdresses += mailList.getEmail()+", ";
      }
      if (selectedIDStringsLength > 1){
          mailList = ((com.idega.block.mailinglist.data.MailAccountHome)com.idega.data.IDOLookup.getHomeLegacy(MailAccount.class)).findByPrimaryKeyLegacy(Integer.parseInt(selectedIDStrings[selectedIDStringsLength - 1]));
          ccAdresses += mailList.getEmail();
      }
      else ccAdresses = " ";
    }
    addressInput.setValue(adress);
    CCInput.setValue(ccAdresses);
  }

     //VIEW INBOX LETTERS
  public void setViewInboxLetter(Message viewMessage) throws MessagingException, IOException{
    SubmitButton forwardLetterButton = new SubmitButton(forwardLetterButtonName, "Forward");
    SubmitButton replyLetterButton = new SubmitButton(replyLetterButtonName, "Reply");
    addressInput.setValue(EmailServiceHandler.getStringAddresses(viewMessage.getRecipients(Message.RecipientType.TO)));
    subjectInput.setValue(viewMessage.getSubject());
    CCInput.setValue(EmailServiceHandler.getStringAddresses(viewMessage.getRecipients(Message.RecipientType.CC)));
    BCCInput.setValue(EmailServiceHandler.getStringAddresses(viewMessage.getRecipients(Message.RecipientType.BCC)));
    letterTextArea.setContent(EmailServiceHandler.getStringBody(viewMessage));
    writeLetterViewTable.add(forwardLetterButton,2,7);
    writeLetterViewTable.add(replyLetterButton,3,7);

    //create attachments links to click????

  }

     //VIEW DRAFT LETTERS
  public void setViewDraftLetter(EmailLetterData letter){
   System.out.println("letter.getBody() = " +letter.getBody());
   // System.out.println("letter.getToEmail() = " +letter.getToEmail());
    addressInput.setValue(letter.getToEmail());
    subjectInput.setValue(letter.getSubject());
    CCInput.setValue(letter.getCCEmail());
    BCCInput.setValue(letter.getBCCEmail());
    letterTextArea.setContent(letter.getBody());
  }

  /** @todo FIX FORWARD A MESSAGE*/
  //FORWARD
  public void setForwardLetterView(/*InboxData letter){
    addressInput.setValue(letter.getToEmail());
    subjectInput.setValue(letter.getSubject());
    CCInput.setValue(letter.getCCEmail());
    BCCInput.setValue(letter.getBCCEmail());
    String replyBody = new String("----------Message Forwarded---------- \n" +letter.getFromEmail()+" Wrote on "+letter.getSentDate()+"\n");
    letterTextArea.setContent(replyBody+letter.getBody());*/){
  }

  /** @todo FIX REPLY A MESSAGE*/
  //REPLY
  public void setReplyLetterView(/*InboxData letter){
    addressInput.setValue(letter.getFromEmail());
    subjectInput.setValue(letter.getSubject());
    CCInput.setValue(letter.getCCEmail());
    BCCInput.setValue(letter.getBCCEmail());
    String replyBody = new String("------------Reply----------- \n"+letter.getFromEmail()+" Wrote on "+letter.getSentDate()+"\n");
    letterTextArea.setContent(replyBody+letter.getBody());*/){
  }

  public void main(IWContext modinfo){

    ccTable.add("Cc: ", 1,1);
    ccTable.add(CCInput,2,1);
    ccTable.add(" Bcc:",3,1);
    ccTable.add(BCCInput,4,1);
    writeLetterViewTable.mergeCells(2,2,3,2);
    writeLetterViewTable.mergeCells(2,3,3,3);
    writeLetterViewTable.mergeCells(1,4,3,4);
    writeLetterViewTable.mergeCells(1,5,3,5);
    writeLetterViewTable.add("Address: ",1,2);
    writeLetterViewTable.add(addressInput,2,2);
    writeLetterViewTable.add("Subject: ",1,3);
    writeLetterViewTable.add(subjectInput,2,3);
    writeLetterViewTable.add(ccTable,1,4);
    writeLetterViewTable.add(letterTextArea,1,5);
    writeLetterViewTable.add(sendLetterButton,1,6);
    writeLetterViewTable.add(saveLetterButton,2,6);
    writeLetterViewTable.add(editAttachmentsButton,3,6);

    add(writeLetterViewTable);
  }
}
