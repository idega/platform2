package com.idega.block.mailinglist.data;

import javax.ejb.*;

public interface MailAccount extends com.idega.block.mailinglist.data.Account
{
 public java.lang.String getName();
 public int getUserID();
 public java.lang.String getUserName();
 public void setUserID(int p0);
 public void setUserName(java.lang.String p0);
}
