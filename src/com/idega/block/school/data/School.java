package com.idega.block.school.data;

import javax.ejb.*;

public interface School extends com.idega.data.IDOEntity
{
 public void setSchoolInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection findRelatedSchoolYears()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public void addSchoolYearsRemoveOther(int[] p0) throws java.rmi.RemoteException;
 public void addSchoolTypes(int[] p0) throws java.rmi.RemoteException;
 public void setHeadmasterGroupId(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findRelatedSchoolTypes()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public void setSchoolLongitude(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolPhone() throws java.rmi.RemoteException;
 public java.lang.String getSchoolKeyCode() throws java.rmi.RemoteException;
 public void setSchoolZipCode(java.lang.String p0) throws java.rmi.RemoteException;
 public void addSchoolTypesRemoveOther(int[] p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolZipArea() throws java.rmi.RemoteException;
 public java.lang.String getSchoolLatitude() throws java.rmi.RemoteException;
 public void setSchoolAddress(java.lang.String p0) throws java.rmi.RemoteException;
 public int getSchoolTypeId() throws java.rmi.RemoteException;
 public void setSchoolName(java.lang.String p0) throws java.rmi.RemoteException;
 public int getHeadmasterGroupId() throws java.rmi.RemoteException;
 public int getSchoolAreaId() throws java.rmi.RemoteException;
 public java.lang.String getSchoolInfo() throws java.rmi.RemoteException;
 public void setSchoolTypeId(int p0) throws java.rmi.RemoteException;
 public void setSchoolKeyCode(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setSchoolAreaId(int p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolZipCode() throws java.rmi.RemoteException;
 public void setSchoolLatitude(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolAddress() throws java.rmi.RemoteException;
 public void setSchoolZipArea(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolLongitude() throws java.rmi.RemoteException;
 public void setSchoolPhone(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSchoolName() throws java.rmi.RemoteException;
 public void addSchoolYears(int[] p0) throws java.rmi.RemoteException;
}
