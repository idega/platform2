package com.idega.block.email.data;

import javax.ejb.*;

import com.idega.block.email.business.EmailTopic;
import com.idega.data.CategoryEntity;
import com.idega.data.IDOEntity;

public interface MailTopic extends IDOEntity,EmailTopic,CategoryEntity
{
 public java.sql.Timestamp getCreated();
 public java.lang.String getDescription() ;
 public int getGroupId();
 public int getListId() ;
 public java.lang.String getName() ;
 public void initializeAttributes() ;
 public void setCreated(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public void setGroupId(int p0) throws java.rmi.RemoteException;
 public void setListId(int p0) throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) ;
}
