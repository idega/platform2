package com.idega.block.school.data;


public interface SchoolUser extends com.idega.data.IDOEntity
{
 public com.idega.block.school.data.SchoolUserHome getHome()throws java.rmi.RemoteException;
 public boolean getMainHeadmaster();
 public int getSchoolId();
 public boolean getShowInContact();
 public com.idega.user.data.User getUser();
 public int getUserId();
 public int getUserType();
 public void initializeAttributes();
 public void setMainHeadmaster(boolean p0);
 public void setSchoolId(int p0);
 public void setShowInContact(boolean p0);
 public void setUserId(int p0);
 public void setUserType(int p0);
}
