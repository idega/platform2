package se.idega.idegaweb.commune.account.provider.data;

import javax.ejb.*;

public interface ProviderApplication extends com.idega.data.IDOEntity,com.idega.block.process.data.Case,se.idega.idegaweb.commune.account.data.AccountApplication
{
 public void setEmail(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getEmail()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getEmailAddress() throws java.rmi.RemoteException;
 public void setSchoolArea(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfPlaces() throws java.rmi.RemoteException;
 public java.lang.String getAdditionalInfo() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setPostalCode(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAdditionalInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setPostalCode(com.idega.core.data.PostalCode p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getManagerName() throws java.rmi.RemoteException;
 public java.util.Collection getSchoolTypes() throws java.rmi.RemoteException;
 public java.lang.String getAddress() throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolArea getSchoolArea() throws java.rmi.RemoteException;
 public void setSchoolTypes(int[] p0) throws java.rmi.RemoteException;
 public void setNumberOfPlaces(int p0) throws java.rmi.RemoteException;
 public com.idega.core.data.PostalCode getPostalCode() throws java.rmi.RemoteException;
 public void setApplicantName(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setPhone(java.lang.String p0) throws java.rmi.RemoteException;
 public void addSchoolType(com.idega.block.school.data.SchoolType p0) throws java.rmi.RemoteException;
 public java.lang.String getPhone() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setEmailAddress(java.lang.String p0) throws java.rmi.RemoteException;
 public void setAddress(java.lang.String p0) throws java.rmi.RemoteException;
 public void setSchoolArea(com.idega.block.school.data.SchoolArea p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setManagerName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setSchoolTypes(java.util.Collection p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
 public java.lang.String getApplicantName()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
