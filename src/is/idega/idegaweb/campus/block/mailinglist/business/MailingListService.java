package is.idega.idegaweb.campus.block.mailinglist.business;


public interface MailingListService extends com.idega.business.IBOService
{
 public boolean addEmail(int p0,java.lang.String p1)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDORelationshipException,javax.ejb.CreateException, java.rmi.RemoteException;
 public boolean addEmail(int p0,java.util.List p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean addEmail(is.idega.idegaweb.campus.block.mailinglist.data.MailingList p0,java.lang.String p1)throws javax.ejb.CreateException,java.rmi.RemoteException,com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter createEmailLetter(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,boolean p4,boolean p5,java.lang.String p6) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter createEmailLetter(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,boolean p6,java.lang.String p7) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.MailingList createMailingList(int p0,java.lang.String p1)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException, java.rmi.RemoteException;
 public java.lang.String getEmailBody(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,com.idega.idegaweb.IWResourceBundle p1) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter getEmailLetter(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getEmailLetters() throws java.rmi.RemoteException;
 public java.lang.String getEmailSubject(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,com.idega.idegaweb.IWResourceBundle p1) throws java.rmi.RemoteException;
 public java.util.Collection getEmails(is.idega.idegaweb.campus.block.mailinglist.data.MailingList p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.MailingList getMailingList(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getMailingLists() throws java.rmi.RemoteException;
 public java.util.Map mapOfMailingList(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0) throws java.rmi.RemoteException;
 public boolean processMailEvent(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public boolean processMailEvent(is.idega.idegaweb.campus.block.mailinglist.business.EntityHolder p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void removeEmail(is.idega.idegaweb.campus.block.mailinglist.data.MailingList p0,int p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeEmailLetter(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0) throws java.rmi.RemoteException;
 public void removeMailingList(is.idega.idegaweb.campus.block.mailinglist.data.MailingList p0) throws java.rmi.RemoteException;
 public boolean sendMail(int p0,is.idega.idegaweb.campus.block.mailinglist.business.EntityHolder p1) throws java.rmi.RemoteException;
 public boolean sendMail(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,is.idega.idegaweb.campus.block.mailinglist.business.EntityHolder p1) throws java.rmi.RemoteException;
 public void setEmailBody(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,com.idega.idegaweb.IWResourceBundle p1,java.lang.String p2) throws java.rmi.RemoteException;
 public void setEmailSubject(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,com.idega.idegaweb.IWResourceBundle p1,java.lang.String p2) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter storeEmailLetter(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,boolean p6,java.lang.String p7) throws java.rmi.RemoteException;
 public void storeEmailLetterMailingLists(is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter p0,int[] p1,int[] p2) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.mailinglist.data.MailingList storeMailingList(int p0,int p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
}
