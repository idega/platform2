package com.idega.block.mailinglist.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.text.Link;
import com.idega.presentation.Table;
import com.idega.presentation.IWContext;
import com.idega.block.reports.presentation.ContentViewer;
import com.idega.block.reports.business.Content;
import com.idega.data.EntityFinder;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.sql.SQLException;

import com.idega.block.mailinglist.data.*;
import com.idega.block.mailinglist.business.InboxManager;
import com.idega.block.mailinglist.business.EmailServiceHandler;

import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Flags;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class EmailProgramListLetterTable extends Block {

  public static final String deleteButtonName = "deleteButtonName";
  public static final String viewMyLetterLinkParameterName = "viewMyLetterLinkParameterName";
  public static final String viewInboxLetterLinkParameterName = "viewInboxLetterLinkParameterName";
  public static final String checkBoxName = "checkBoxName";

  Table letterListTable;
  CheckBox checkBoxes;
  Link viewLetterLink;
  SubmitButton deleteButton = new SubmitButton(deleteButtonName, "Delete Marked");

  public EmailProgramListLetterTable() {
  }

  private Vector getDraftsContentVector() throws SQLException{
    EmailLetterData letters = ((com.idega.block.mailinglist.data.EmailLetterDataHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetterData.class)).createLegacy();
    EmailLetterData[] unSentLetters;
    unSentLetters = (EmailLetterData[]) letters.findAllByColumn(com.idega.block.mailinglist.data.EmailLetterDataBMPBean.EMAIL_LETTER_DATA_SENT, "N");

    Vector draftContentVector = new Vector();
    Object[] contents;
    CheckBox checkBoxes;
    Link viewLetterLink;
    int unSentLettersCurrentID;
    Content content;
    int numberOfLetters;
    if (unSentLetters == null) numberOfLetters = 0;
    else numberOfLetters = unSentLetters.length;
    for (int i = 0; i < numberOfLetters; i++) {
      unSentLettersCurrentID = unSentLetters[i].getID();
      contents = new Object[6];
      contents[0] = unSentLetters[i].getDate();
      contents[1] = unSentLetters[i].getSubject();
      contents[2] = unSentLetters[i].getToEmail();
      contents[3] = unSentLetters[i].getFromEmail();
      checkBoxes = new CheckBox(checkBoxName, String.valueOf(unSentLettersCurrentID));
      contents[4] = checkBoxes;
      viewLetterLink = new Link("View");
      viewLetterLink.addParameter(viewMyLetterLinkParameterName, unSentLettersCurrentID);
      contents[5] = viewLetterLink;

      content = new Content(contents);
      draftContentVector.add(content);
    }
    return  draftContentVector;
  }

  public void setShowDrafts(IWContext iwc) throws SQLException{
    String[] titles = new String[6];
    titles[0] = "Creation Date";
    titles[1] = "Subject";
    titles[2] = "To";
    titles[3] = "From";
    titles[4] = "Delete";
    titles[5] = "View Letter";

    ContentViewer contentViewer = new ContentViewer();

    //To diffrentiate from the other contentviewers in the page
    contentViewer.setICObjectID(1);
    System.err.println("DraftsContentVector SIZE !!! ARRG "+this.getDraftsContentVector().size());
    try {
      contentViewer = new ContentViewer( titles, new Vector());
      contentViewer.setContent(this.getDraftsContentVector());
    }
    catch (Exception ex) {
      System.err.println("VILLA ARRRGG!!!  ");
      ex.printStackTrace(System.err);
    }

    letterListTable = new Table(1,3);
    letterListTable.add("DRAFTS",1,1);
    letterListTable.add(contentViewer,1,2);
    letterListTable.setAlignment(1,1,"center");
    letterListTable.setAlignment(1,3,"right");
    letterListTable.add(deleteButton,1,3);
  }

  private Vector getSentContentVector() throws SQLException{
    EmailLetterData letters = ((com.idega.block.mailinglist.data.EmailLetterDataHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetterData.class)).createLegacy();
    EmailLetterData[] sentLetters;
    sentLetters = (EmailLetterData[]) letters.findAllByColumn(com.idega.block.mailinglist.data.EmailLetterDataBMPBean.EMAIL_LETTER_DATA_SENT, "Y");

    Vector sentContentVector = new Vector();
    Object[] sentContents;
    CheckBox checkBoxes;
    Link viewLetterLink;
    int sentLettersCurrentID;
    Content sentContent;
    int numberOfLetters;
    if (sentLetters == null) numberOfLetters = 0;
    else numberOfLetters = sentLetters.length;
    for (int i = 0; i < numberOfLetters; i++) {
      sentLettersCurrentID = sentLetters[i].getID();
      sentContents = new Object[6];
      sentContents[0] = sentLetters[i].getDate();
      sentContents[1] = sentLetters[i].getSubject();
      sentContents[2] = sentLetters[i].getToEmail();
      sentContents[3] = sentLetters[i].getFromEmail();
      checkBoxes = new CheckBox(checkBoxName, String.valueOf(sentLettersCurrentID));
      sentContents[4] = checkBoxes;
      viewLetterLink = new Link("View");
      viewLetterLink.addParameter(viewMyLetterLinkParameterName, sentLettersCurrentID);
      sentContents[5] = viewLetterLink;


      sentContent = new Content(sentContents);
      sentContentVector.add(sentContent);
    }
    return  sentContentVector;
  }

  public void setShowSentLetters(IWContext iwc) throws SQLException{
    String[] titles = new String[6];
    titles[0] = "Creation Date";
    titles[1] = "Subject";
    titles[2] = "To";
    titles[3] = "From";
    titles[4] = "Delete";
    titles[5] = "View Letter";

    ContentViewer sentContentViewer = new ContentViewer();

    //To diffrentiate from the other contentviewers in the page
    sentContentViewer.setICObjectID(2);
    System.err.println(""+this.getSentContentVector().size());
    try {
      sentContentViewer = new ContentViewer( titles, this.getSentContentVector());
    }
    catch (Exception ex) {
      System.err.println("VILLA ARRRGG!!!  ");
      ex.printStackTrace(System.err);
    }

    letterListTable = new Table(1,3);
    letterListTable.add("SENT LETTERS",1,1);
    letterListTable.add(sentContentViewer,1,2);
    letterListTable.add(sentContentViewer,1,2);
    letterListTable.setAlignment(1,1,"center");
    letterListTable.setAlignment(1,3,"right");
    letterListTable.add(deleteButton,1,3);
  }

  private Vector getInboxLettersContentVector(IWContext iwc) throws MessagingException{

    Message[] inboxLetters;
    inboxLetters = EmailServiceHandler.getMessages(iwc);

    Vector inboxContentVector = new Vector();
    Object[] inboxContents;
    CheckBox checkBoxes;
    Link viewLetterLink;
    int inboxLettersCurrentID;
    Content inboxContent;
    int numberOfLetters;
    if (inboxLetters == null) numberOfLetters = 0;
    else numberOfLetters = inboxLetters.length;
    for (int i = 0; i < numberOfLetters; i++) {
      if (!inboxLetters[i].isSet(Flags.Flag.DELETED)){
        inboxLettersCurrentID = inboxLetters[i].getMessageNumber();
        inboxContents = new Object[6];
        inboxContents[0] = inboxLetters[i].getSentDate();
        inboxContents[1] = inboxLetters[i].getSubject();
        inboxContents[2] = EmailServiceHandler.getStringAddresses(inboxLetters[i].getRecipients(Message.RecipientType.TO));
        inboxContents[3] = EmailServiceHandler.getStringAddresses(inboxLetters[i].getFrom());
        checkBoxes = new CheckBox(checkBoxName, String.valueOf(inboxLettersCurrentID));
        inboxContents[4] = checkBoxes;
        viewLetterLink = new Link("View");
        viewLetterLink.addParameter(viewMyLetterLinkParameterName, inboxLettersCurrentID);
        inboxContents[5] = viewLetterLink;

        inboxContent = new Content(inboxContents);
        inboxContentVector.add(inboxContent);
      }
    }
    return  inboxContentVector;
  }

  public void setShowInboxLetters(IWContext iwc) throws SQLException, MessagingException{

    String[] titles = new String[6];
    titles[0] = "Creation Date";
    titles[1] = "Subject";
    titles[2] = "To";
    titles[3] = "From";
    titles[4] = "Delete";
    titles[5] = "View Letter";

    ContentViewer sentContentViewer = new ContentViewer();

    //To diffrentiate from the other contentviewers in the page
    sentContentViewer.setICObjectID(2);
    System.err.println(""+this.getSentContentVector().size());
    try {
      sentContentViewer = new ContentViewer( titles, this.getSentContentVector());
    }
    catch (Exception ex) {
      System.err.println("VILLA ARRRGG!!!  ");
      ex.printStackTrace(System.err);
    }

    letterListTable = new Table(1,3);
    letterListTable.add("SENT LETTERS",1,1);
    letterListTable.add(sentContentViewer,1,2);
    letterListTable.setAlignment(1,1,"center");
    letterListTable.setAlignment(1,3,"right");
    letterListTable.add(deleteButton,1,3);

  }

  public void main(IWContext modinfo){
  /*  letterListTable.add("date",1,1);
    letterListTable.add("subject",2,1);
    letterListTable.add("attachments",5,1);*/

    add(letterListTable);
  }
}
