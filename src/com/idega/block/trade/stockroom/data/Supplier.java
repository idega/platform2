package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface Supplier extends com.idega.data.IDOLegacyEntity
{
 public com.idega.core.data.Address getAddress()throws java.sql.SQLException;
 public java.util.List getAddresses()throws java.sql.SQLException;
 public java.lang.String getDescription();
 public com.idega.core.data.Email getEmail()throws java.sql.SQLException;
 public java.util.List getEmails()throws java.sql.SQLException;
 public java.util.List getFaxPhone()throws java.sql.SQLException;
 public int getGroupId();
 public java.util.List getHomePhone()throws java.sql.SQLException;
 public boolean getIsValid();
 public java.util.List getMobilePhone()throws java.sql.SQLException;
 public java.lang.String getName();
 public java.util.List getPhones()throws java.sql.SQLException;
 public java.util.List getPhones(int p0)throws java.sql.SQLException;
 public java.util.List getWorkPhone()throws java.sql.SQLException;
 public void setDefaultValues();
 public void setDescription(java.lang.String p0);
 public void setGroupId(int p0);
 public void setIsValid(boolean p0);
 public void setName(java.lang.String p0);
}
