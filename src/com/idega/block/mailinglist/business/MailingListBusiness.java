package com.idega.block.mailinglist.business;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */
import com.idega.block.mailinglist.data.*;
import com.idega.block.mailinglist.presentation.EmailProgramViewTable;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimeStamp;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.idega.core.user.data.User;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.SimpleQuerier;

public class MailingListBusiness {

  public MailingListBusiness() {
  }

  /**Adds an email to a mailinglist.
   * Only the first email account found (if any previous accounts exist)
   * is connected with a mailinglist.
   */
  public static int addEmailBusiness(IWContext iwc, String[] selctionBoxChoices, String inputEmail) throws SQLException{

    int reply = 1;
    MailAccount emaillist = null;
    MailAccount[] list = null;

    list = (MailAccount[]) (com.idega.data.GenericEntity.getStaticInstance("com.idega.block.mailinglist.data.MailAccount").findAllByColumn(com.idega.block.mailinglist.data.MailAccountBMPBean.EMAIL, inputEmail));

    if( list.length > 0 ){
      emaillist = list[0];
      reply = 5;
    }
    else{
      createSimpleEmailAccount(iwc, inputEmail);
      reply = 7;
    }

    for (int i = 0; i < selctionBoxChoices.length; i++) {
      Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).findByPrimaryKeyLegacy(Integer.parseInt(selctionBoxChoices[i]));

      try {
        //Notice that only the first email found (if any accounts exist) is connected with a mailinglist.
        emaillist.addTo(mailinglist);
        reply = reply * 2;
      }
      catch (Exception ex) {
        reply = reply * 3;
      }
    //  }
    }
    return (reply);
  }


  public static int removeEmailBusiness(IWContext modinfo, String[] selctionBoxChoices, String removeEmail) throws SQLException{

    int reply = 1;
    MailAccount[] list;
    list = (MailAccount[]) (com.idega.data.GenericEntity.getStaticInstance("com.idega.block.mailinglist.data.MailAccount").findAllByColumn(com.idega.block.mailinglist.data.MailAccountBMPBean.EMAIL, removeEmail));

    if(list.length > 0){

      Mailinglist removeMailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
      Mailinglist[] removeMailinglistArray = null;
      removeMailinglistArray = (Mailinglist[]) removeMailinglist.constructArray(selctionBoxChoices);

      if (removeMailinglistArray.length > 0) {
        for (int i = 0; i < list.length; i++) {
          for (int j = 0; j < removeMailinglistArray.length; j++) {
            list[i].removeFrom(removeMailinglistArray[j]);
          }
        }

        Mailinglist postList = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
        Mailinglist[] allRelatedLeftoverMailinglist = (Mailinglist[]) list[0].findReverseRelated(postList);

        if (allRelatedLeftoverMailinglist.length < 1 ) {
          MailAccount mailingList =  ((com.idega.block.mailinglist.data.MailAccountHome)com.idega.data.IDOLookup.getHomeLegacy(MailAccount.class)).createLegacy();
          mailingList.deleteMultiple(com.idega.block.mailinglist.data.MailAccountBMPBean.EMAIL, removeEmail);
          reply = 17;
        }
      }
      reply = reply * 11;
    }
    else {
      reply = reply *13;
    }
    return(reply);
  }

  public static void createSimpleEmailAccount(IWContext iwc, String email){
    try {
      User user = iwc.getUser();
      createEmailAccount(user.getID(), user.getName(), email, email, "", -1, "", "",
                         "", -1, "", "", IWTimeStamp.getTimestampRightNow());
    }
    catch (SQLException ex) {}
  }

  public static void createEmailAccount(int iUserId, String name, String email, String replyEmail,
                       String smtpHost, int smtpPort, String smtpLogin, String smtpPassword,
                       String pop3Host, int pop3Port, String pop3Login, String pop3Password,
                       Timestamp creationDate) throws SQLException
  {
    MailAccount mailAccount = ((com.idega.block.mailinglist.data.MailAccountHome)com.idega.data.IDOLookup.getHomeLegacy(MailAccount.class)).createLegacy();
    mailAccount.setCreationDate(creationDate);
    mailAccount.setEmail(email);
    mailAccount.setPOP3Host(pop3Host);
    mailAccount.setPOP3LoginName(pop3Login);
    mailAccount.setPOP3Password(pop3Password);
    mailAccount.setPOP3Port(pop3Port);
    mailAccount.setReplyEmail(replyEmail);
    mailAccount.setSMTPHost(smtpHost);
    mailAccount.setSMTPLoginName(smtpLogin);
    mailAccount.setSMTPPassword(smtpPassword);
    mailAccount.setSMTPPort(smtpPort);
    mailAccount.setUserID(iUserId);
    mailAccount.setUserName(name);
    mailAccount.insert();
  }

  public static void updateEmailAccount(MailAccount account, int iUserId, String name, String email, String replyEmail,
                       String smtpHost, int smtpPort, String smtpLogin, String smtpPassword,
                       String pop3Host, int pop3Port, String pop3Login, String pop3Password,
                       Timestamp creationDate) throws SQLException
  {
    account.setCreationDate(creationDate);
    account.setEmail(email);
    account.setPOP3Host(pop3Host);
    account.setPOP3LoginName(pop3Login);
    account.setPOP3Password(pop3Password);
    account.setPOP3Port(pop3Port);
    account.setReplyEmail(replyEmail);
    account.setSMTPHost(smtpHost);
    account.setSMTPLoginName(smtpLogin);
    account.setSMTPPassword(smtpPassword);
    account.setSMTPPort(smtpPort);
    account.setUserID(iUserId);
    account.setUserName(name);
    account.update();

  }


  public static int addMailinglistBusiness(IWContext modinfo, String inputMailinglistName) throws SQLException{
    Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
    Mailinglist[] mailinglistArray;
    mailinglistArray = (Mailinglist[]) mailinglist.findAllByColumn(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME, inputMailinglistName);
    if (mailinglistArray.length < 1){
      mailinglist.setMailinglistName(inputMailinglistName);
      mailinglist.setCreationDate(IWTimeStamp.getTimestampRightNow());
      mailinglist.insert();
    }
    return(1);
  }

  public static int addMailinglistBusiness(IWContext modinfo, String inputMailinglistName, String email, String mailServer) throws SQLException{
    Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
    Mailinglist[] mailinglistArray;
    mailinglistArray = (Mailinglist[]) mailinglist.findAllByColumn(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME, inputMailinglistName);
    //System.err.println( "1 stig");
    if (mailinglistArray.length < 1){
    //System.err.println( "2 stig");
      mailinglist.setMailinglistName(inputMailinglistName);
      mailinglist.setEmail(email);
      mailinglist.setPOP3Host(mailServer);
      mailinglist.setCreationDate(IWTimeStamp.getTimestampRightNow());
      mailinglist.insert();
    }
    else{
/*I think I should maybe use update here instead of inserting again!!!!  Also maybe
  use the update method here below*/
      for (int i = 0; i < mailinglistArray.length; i++) {
        mailinglistArray[i].delete();
      }
      mailinglist.setMailinglistName(inputMailinglistName);
      mailinglist.setEmail(email);
      mailinglist.setPOP3Host(mailServer);
      mailinglist.setCreationDate(IWTimeStamp.getTimestampRightNow());
      mailinglist.insert();
    }

    return(1);
  }

  public static int updateMailinglistBusiness(IWContext modinfo, String inputMailinglistName, String email, String mailServer) throws SQLException{
    //Even though I use an Array I assume that for every name there is only one mailinglist!
    Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
    Mailinglist[] mailinglistArray;
    mailinglistArray = (Mailinglist[]) mailinglist.findAllByColumn(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME, inputMailinglistName);
    if (mailinglistArray.length != 0){
      for (int i = 0; i < mailinglistArray.length; i++) {
        mailinglistArray[i].setMailinglistName(inputMailinglistName);
        mailinglistArray[i].setEmail(email);
        mailinglistArray[i].setPOP3Host(mailServer);
        mailinglistArray[i].setCreationDate(IWTimeStamp.getTimestampRightNow());
        mailinglistArray[i].update();
      }
    }
    return(1);
  }

  public static int removeMailinglistBusiness(IWContext modinfo, String[] checkedBoxChoices) throws SQLException {
    Mailinglist mailinglist = ((com.idega.block.mailinglist.data.MailinglistHome)com.idega.data.IDOLookup.getHomeLegacy(Mailinglist.class)).createLegacy();
    Mailinglist[] mailinglistArray;
    mailinglistArray = (Mailinglist[]) mailinglist.constructArray(checkedBoxChoices);

    if (mailinglistArray != null){
      if (mailinglistArray.length >0){
        for (int i = 0; i < mailinglistArray.length; i++) {
          System.err.println(checkedBoxChoices[i]);
          mailinglist.deleteMultiple(com.idega.block.mailinglist.data.MailinglistBMPBean.MAILINGLIST_NAME, mailinglistArray[i].getMailinglistName());
        }
      }
      else {

      }
    }

    return(1);
  }

  public static int addEmailLetterDataBusiness(IWContext iwc) throws SQLException{
    return addEmailLetterDataBusiness( iwc.getParameter(EmailProgramViewTable.subjectInputName),
                            iwc.getParameter(EmailProgramViewTable.letterTextAreaName), Boolean.TRUE /*ATH. TJEKKA BETUR*/,
                            iwc.getParameter(EmailProgramViewTable.addressInputName), " bjarni",// iwc.getParameter(adressFromInputName),
                            iwc.getParameter(EmailProgramViewTable.CCInputName), -1);
  }

  public static int addEmailLetterDataBusiness( String subject, String body, Boolean sent,
                                                String toEmail, String fromEmail, String ccEmail,
                                                int letterID) throws SQLException{

    EmailLetterData letters;
    System.err.println("NOTICE !! MAILINGLIST BUSINESS letterID = "+letterID);
    if (letterID == -1) letters = ((com.idega.block.mailinglist.data.EmailLetterDataHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetterData.class)).createLegacy();
    else {letters = ((com.idega.block.mailinglist.data.EmailLetterDataHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetterData.class)).findByPrimaryKeyLegacy(letterID);}
    letters.setBody(body);
    letters.setHasSent(sent);
    letters.setSubject(subject);
    letters.setToEmail(toEmail);
    letters.setFromEmail(fromEmail);
    letters.setDate(IWTimeStamp.getTimestampRightNow());
    letters.setCCEmail(ccEmail);
    System.err.println("letters.getID() = " +letters.getID());
    if (letterID == -1) {
      letters.insert();
      return letters.getID();
    }
    else {
      letters.update();
      return letterID;
    }
  }


  public static int removeEmailLetterDataBusiness(IWContext modinfo, String[] checkedBoxChoices) throws SQLException{
    EmailLetterData letters = ((com.idega.block.mailinglist.data.EmailLetterDataHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetterData.class)).createLegacy();
    EmailLetterData[] lettersArray;
    lettersArray = (EmailLetterData[]) letters.constructArray(checkedBoxChoices);

    if (lettersArray != null){
      if (lettersArray.length >0){
        for (int i = 0; i < lettersArray.length; i++) {
          System.err.println(checkedBoxChoices[i]);
          lettersArray[i].delete();
        }
      }
      else {

      }
    }
    return 1;
  }
}
