package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolContentBusiness extends com.idega.business.IBOSession
{
 //public boolean hasEditPermission() throws java.rmi.RemoteException;
 public java.lang.String getParameterSchoolId() throws java.rmi.RemoteException;
}
