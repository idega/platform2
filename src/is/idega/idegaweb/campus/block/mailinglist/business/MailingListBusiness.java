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

  private static String LETTER_KEY = "email_";
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

  public static boolean sendMail(int letterId,EntityHolder holder,IWResourceBundle iwrb){
    try {
      EmailLetter letter = new EmailLetter(letterId);
       return sendMail(letter,holder,iwrb);
    }
    catch (Exception ex) {

    }
    return false;

  }
  public static boolean sendMail(EmailLetter letter,EntityHolder holder,IWResourceBundle iwrb){
    try {
      String Body = getEmailBody(letter,iwrb);
      if(letter.getParse()){
        LetterParser parser = new LetterParser(holder);
        Body = new ContentParser().parse(parser,Body);
      }

      String subject = getEmailSubject(letter,iwrb);
      List lists =  EntityFinder.findRelated(letter,new MailingList());
      if(lists!=null){
        Iterator mIter = lists.iterator();
        Iterator eIter;
        MailingList mlist;
        List emails = new Vector();
        List temp;
        Email email;
        while (mIter.hasNext()) {
          mlist = (MailingList) mIter.next();
          temp = EntityFinder.findRelated(mlist,new Email());
          if(temp !=null){
            eIter = temp.iterator();
            while (eIter.hasNext()) {
              email = (Email) eIter.next();
              SendMail.send(letter.getFrom(),email.getEmailAddress(),"","",letter.getHost(),subject,Body);
            }

          }

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

  public static EmailLetter saveEmailLetter(int iEmailLetterId, String sHost,String sFrom,boolean Parse,String type){
    EmailLetter letter = null;
    try {
      boolean update = false;
      letter = new EmailLetter();
      if(iEmailLetterId > 0){
        letter = new EmailLetter(iEmailLetterId);
        update = true;
      }
      letter.setEmailKey(LETTER_KEY+iEmailLetterId);
      letter.setHost(sHost);
      letter.setFrom(sFrom);
      letter.setParse(Parse);
      letter.setType(type);
      if(update)
        letter.update();
      else{
        letter.insert();
        letter.setEmailKey(LETTER_KEY+iEmailLetterId);
        letter.update();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return letter;
  }

  public static EmailLetter createEmailLetter(String sHost,String sFrom,String sSubject,String sBody,boolean bParse,String type,IWResourceBundle iwrb){
    EmailLetter letter = saveEmailLetter(-1,sHost,sFrom,bParse,type);
    if(letter!=null){
      setEmailBody(letter,iwrb,sBody);
      setEmailSubject(letter,iwrb,sSubject);
      return letter;
    }
    else
      return null;
  }

  public static EmailLetter createEmailLetter(EmailLetter emailletter,String sHost,String sFrom,String sSubject,String sBody,boolean bParse,String type,IWResourceBundle iwrb){
    int id = emailletter!=null ? emailletter.getID():-1;
    System.err.println("letter id = "+id);
    EmailLetter letter = saveEmailLetter(id,sHost,sFrom,bParse,type);
    if(letter!=null){
      setEmailBody(letter,iwrb,sBody);
      setEmailSubject(letter,iwrb,sSubject);
      return letter;
    }
    else{
      System.err.println("letter is null");
      return null;
    }
  }

  public static void deleteEmailLetter(EmailLetter letter,IWResourceBundle iwrb){
    try {
      iwrb.getIWBundleParent().removeLocalizableString(letter.getEmailKey());
      iwrb.getIWBundleParent().removeLocalizableString(letter.getSubjectKey());
      letter.removeFrom(MailingList.class);
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