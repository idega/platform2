package com.idega.block.school.business;

import javax.ejb.*;

public interface SchoolSeasonBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findAllPreviousSchoolSeasons(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllPreviousSchoolSeasons(com.idega.block.school.data.SchoolSeason p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolSeasons() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getSchoolSeason(java.lang.Object p0) throws java.rmi.RemoteException;
 public void removeSchoolSeason(int p0) throws java.rmi.RemoteException;
 public void storeSchoolSeason(int p0,java.lang.String p1,java.util.Date p2,java.util.Date p3,java.util.Date p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
