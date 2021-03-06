package is.idega.idegaweb.campus.block.mailinglist.data;


public interface EmailLetter extends com.idega.data.IDOEntity,com.idega.block.email.business.EmailLetter
{
 public void addMailingList(is.idega.idegaweb.campus.block.mailinglist.data.MailingList p0)throws java.rmi.RemoteException;
 public java.lang.String getBody();
 public java.lang.String getEmailKey();
 public java.lang.String getFrom();
 public java.lang.String getHost();
 public java.util.Collection getMailingLists()throws java.rmi.RemoteException;
 public boolean getOnlyUser();
 public boolean getParse();
 public java.lang.String getSubject();
 public java.lang.String getSubjectKey();
 public java.lang.String getMailType();
 public void initializeAttributes();
 public void removeMailingList(is.idega.idegaweb.campus.block.mailinglist.data.MailingList p0)throws java.rmi.RemoteException;
 public void removeMailingLists()throws java.rmi.RemoteException;
 public void setBody(java.lang.String p0);
 public void setEmailKey(java.lang.String p0);
 public void setFrom(java.lang.String p0);
 public void setHost(java.lang.String p0);
 public void setOnlyUser(boolean p0);
 public void setParse(boolean p0);
 public void setSubject(java.lang.String p0);
 public void setType(java.lang.String p0);
}
