package com.idega.block.school.data;

import javax.ejb.*;

public interface SchoolUser extends com.idega.data.IDOEntity
{
 public int getUserId();
 public void setUserId(int p0);
 public void setSchoolId(int p0);
 public void initializeAttributes();
 public int getSchoolId();
 public com.idega.block.school.data.SchoolUserHome getHome()throws java.rmi.RemoteException;
 public void setUserType(int p0);
 public int getUserType();
}
