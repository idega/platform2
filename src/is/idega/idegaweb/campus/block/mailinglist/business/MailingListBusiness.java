package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.mailinglist.data.*;
import com.idega.core.data.Email;
import java.sql.SQLException;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityBulkUpdater;
import java.util.List;
import com.idega.data.EntityFinder;
import com.idega.util.SendMail;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map;
import java.util.Hashtable;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class MailingListBusiness {

  public static String CATEGORYTYPE = "cam_mail";

  public static MailingList createMailingList(int iCategoryId,String name){
    return saveMailingList(iCategoryId,-1,name);
  }

  public static void removeEmail(MailingList mlist,int email_id){
    try {
      new Email(email_id).removeFrom(mlist);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public static MailingList updateMailingList(int iCategoryId,int iMailingListId,String name){
    return saveMailingList(iCategoryId,iMailingListId,name);
  }

  public static MailingList getMailingList(int id){
    try {
      return new MailingList(id);
    }
    catch (Exception ex) {

    }
    return null;
  }

   public static EmailLetter getEmailLetter(int id){
    try {
      return new EmailLetter(id);
    }
    catch (Exception ex) {

    }
    return null;
  }

  public static MailingList saveMailingList(int iCategoryId,int MailingListId,String name){
    try {
      boolean update = false;
      MailingList mlist = new MailingList();
      if(MailingListId > 0){
        mlist = new MailingList(MailingListId);
        update = true;
      }
      mlist.setName(name);
      mlist.setCreated(idegaTimestamp.getTimestampRightNow());
      if(iCategoryId > 0)
        mlist.setCategoryId(iCategoryId);
      if(update)
        mlist.update();
      else
        mlist.insert();
      return mlist;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static boolean addEmail(int iMailingListId,String address){
    try {
      Email email = new Email();
      email.setEmailAddress(address);
      email.insert();
      MailingList mlist = new MailingList(iMailingListId);
      email.addTo(mlist);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public static boolean addEmail(MailingList mlist,String address){
    if(mlist !=null){
    try {
      Email email = new Email();
      email.setEmailAddress(address);
      email.insert();
      email.addTo(mlist);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    }
    return false;
  }

  public static boolean addEmail(int iMailingListId,List emails){

    try {
      MailingList mlist = new MailingList(iMailingListId);
      EntityBulkUpdater bulk = new EntityBulkUpdater(mlist);
      bulk.addAll(emails,bulk.addto);

      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public static boolean sendMail(int letterId,EntityHolder holder){
    try {
      EmailLetter letter = new EmailLetter(letterId);
       return sendMail(letter,holder);
    }
    catch (Exception ex) {

    }
    return false;

  }

  public static boolean processMailEvent(int iContractId,String type){
    return processMailEvent(new EntityHolder(iContractId),type);
  }

  public static boolean processMailEvent(EntityHolder holder,String type){
    try {
      System.err.println("Sending email of type : "+type);
      List letters = EntityFinder.findAllByColumn(new EmailLetter(),EmailLetter.TYPE,type);
      if(letters !=null){
        System.err.println("Number of letters : "+letters.size());
        java.util.Iterator iter = letters.iterator();
        EmailLetter letter;
        while(iter.hasNext()){
          letter = (EmailLetter) iter.next();
          sendMail(letter,holder);
        }
        return true;
      }
      else
        System.err.println("no letters to send");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return true;
  }

  /**
   *  Parses an email letter and sends it to all recipients
   */
  public static boolean sendMail(EmailLetter letter,EntityHolder holder){
    try {
      String Body = letter.getBody();
      List holderEmails = null;
      if(holder !=null && letter.getParse()){
        LetterParser parser = new LetterParser(holder);
        Body = new ContentParser().parse(parser,Body);
        holderEmails = holder.getEmails();
      }
      String subject = letter.getSubject();

      List emails = new Vector();

      if(holderEmails!=null)
        emails.addAll(holderEmails);

      List lists =  EntityFinder.findRelated(letter,new MailingList());
      MailingList mlist;
      if(!letter.getOnlyUser() && lists!=null){
        Iterator mIter = lists.iterator();
        List temp;
        while (mIter.hasNext()) {
          mlist = (MailingList) mIter.next();
          temp = EntityFinder.findRelated(mlist,new Email());
          if(temp!=null)
            emails.addAll(temp);
        }
      }
      if(emails !=null){
        Iterator eIter = emails.iterator();
        Email email;
        while (eIter.hasNext()) {
          email = (Email) eIter.next();
          System.err.println("Sending letter to "+email.getEmailAddress());
          SendMail.send(letter.getFrom(),email.getEmailAddress(),"","",letter.getHost(),subject,Body);
        }

      }
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }


  public static String getEmailSubject(EmailLetter letter,IWResourceBundle iwrb){
    String subject = iwrb.getLocalizedString(letter.getSubjectKey());
    if(subject ==null)
      return "";
    else
      return subject;
  }

  public static void setEmailSubject(EmailLetter letter,IWResourceBundle iwrb,String subject){
    iwrb.setString(letter.getSubjectKey(),subject);
    //iwrb.storeState();
  }

  public static String getEmailBody(EmailLetter letter,IWResourceBundle iwrb){
    String subject = iwrb.getLocalizedString(letter.getEmailKey());
    if(subject ==null)
      return "";
    else
      return subject;
  }

  public static void setEmailBody(EmailLetter letter,IWResourceBundle iwrb,String body){
    iwrb.setString(letter.getEmailKey(),body);

    //iwrb.storeState();
  }

  public static EmailLetter saveEmailLetter(int iEmailLetterId, String sHost,String sFrom,String subject,String body,boolean Parse,boolean OnlyUser,String type){
    EmailLetter letter = null;
    try {
      boolean update = false;
      letter = new EmailLetter();
      if(iEmailLetterId > 0){
        letter = new EmailLetter(iEmailLetterId);
        update = true;
      }
      letter.setSubject(subject);
      letter.setBody(body);
      letter.setHost(sHost);
      letter.setFrom(sFrom);
      letter.setParse(Parse);
      letter.setOnlyUser(OnlyUser);
      letter.setType(type);
      if(update)
        letter.update();
      else{
        letter.insert();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return letter;
  }

  public static EmailLetter createEmailLetter(String sHost,String sFrom,String sSubject,String sBody,boolean bParse,boolean bOnlyUser,String type){
    return saveEmailLetter(-1,sHost,sFrom,sSubject,sBody,bParse,bOnlyUser,type);
  }

  public static EmailLetter createEmailLetter(EmailLetter emailletter,String sHost,String sFrom,String sSubject,String sBody,boolean bParse,boolean onlyUser,String type){
    int id = emailletter!=null ? emailletter.getID():-1;
    return saveEmailLetter(id,sHost,sFrom,sSubject,sBody,bParse,onlyUser,type);
  }

  public static void deleteEmailLetter(EmailLetter letter){
    try{
      letter.delete();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void deleteMailingList(MailingList list){
    if(list !=null){
      try {
        list.removeFrom(new Email());
        list.delete();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

  }

  public static List listOfEmailLetter(){
    try{
      return EntityFinder.findAllOrdered(new EmailLetter(),EmailLetter.TYPE);
    }
    catch(SQLException ex){}
    return null;
  }

  public static List listOfMailingList(){
    try{
      return EntityFinder.findAll(new MailingList());
    }
    catch(SQLException ex){}
    return null;
  }

  public static Map mapOfMailingList(EmailLetter letter){
    try{
      List L = EntityFinder.findRelated(letter,new MailingList());
      if(L !=null){
        MailingList mlist;
        Iterator I = L.iterator();
        Hashtable H =new Hashtable(L.size());
        while(I.hasNext()){
          mlist = (MailingList) I.next();
          H.put(new Integer(mlist.getID()),mlist);
        }
        return H;
      }
    }
    catch(SQLException ex){}
    return null;
  }

   public static void saveEmailLetterMailingLists(EmailLetter letter,int[] newIds,int[] oldIds){
    try{
      for (int j = 0; j < oldIds.length; j++) {
        letter.removeFrom(MailingList.class,oldIds[j]);
      }
      for (int i = 0; i < newIds.length; i++) {
        letter.addTo(MailingList.class,newIds[i]);
      }
    }
    catch(SQLException ex){ex.printStackTrace();}

  }


  public static List listOfEmails(MailingList mlist){
   try{
    return EntityFinder.findRelated(mlist,new Email());
    }
    catch(SQLException ex){}
    return null;
  }

}