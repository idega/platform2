package is.idega.idegaweb.campus.block.mailinglist.data;

import com.idega.core.contact.data.Email;
import com.idega.data.CategoryEntity;


public interface MailingList extends CategoryEntity
{
 public void addEmail(Email p0)throws com.idega.data.IDORelationshipException;
 public void addEmail(java.util.Collection p0)throws java.rmi.RemoteException;
 public java.sql.Timestamp getCreated();
 public java.util.Collection getEmails()throws java.rmi.RemoteException;
 public java.lang.String getName();
 public void initializeAttributes();
 public void removeEmail(Email p0)throws java.rmi.RemoteException;
 public void removeEmails()throws java.rmi.RemoteException;
 public void setCreated(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
}
