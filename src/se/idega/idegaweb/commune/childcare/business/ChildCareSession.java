package se.idega.idegaweb.commune.childcare.business;


public interface ChildCareSession extends com.idega.business.IBOSession
{
 public int getApplicationID() throws java.rmi.RemoteException;
 public int getCheckID() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.business.ChildCareBusiness getChildCareBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChildCareID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChildID() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getFromTimestamp() throws java.rmi.RemoteException;
 public int getGroupID() throws java.rmi.RemoteException;
 public java.lang.String getParameterApplicationID() throws java.rmi.RemoteException;
 public java.lang.String getParameterCheckID() throws java.rmi.RemoteException;
 public java.lang.String getParameterChildCareID() throws java.rmi.RemoteException;
 public java.lang.String getParameterFrom() throws java.rmi.RemoteException;
 public java.lang.String getParameterGroupID() throws java.rmi.RemoteException;
 public java.lang.String getParameterSchoolTypeID() throws java.rmi.RemoteException;
 public java.lang.String getParameterSeasonID() throws java.rmi.RemoteException;
 public java.lang.String getParameterSortBy() throws java.rmi.RemoteException;
 public java.lang.String getParameterTo() throws java.rmi.RemoteException;
 public java.lang.String getParameterUserID() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getProvider()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getSchoolTypeID() throws java.rmi.RemoteException;
 public int getSeasonID() throws java.rmi.RemoteException;
 public int getSortBy() throws java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getToTimestamp() throws java.rmi.RemoteException;
 public int getUserID() throws java.rmi.RemoteException;
 public boolean hasOutdatedPrognosis() throws java.rmi.RemoteException;
 public boolean hasPrognosis()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setApplicationID(int p0) throws java.rmi.RemoteException;
 public void setCheckID(int p0) throws java.rmi.RemoteException;
 public void setChildCareID(int p0) throws java.rmi.RemoteException;
 public void setChildID(int p0) throws java.rmi.RemoteException;
 public void setFromTimestamp(java.lang.String p0) throws java.rmi.RemoteException;
 public void setGroupID(int p0) throws java.rmi.RemoteException;
 public void setHasOutdatedPrognosis(boolean p0) throws java.rmi.RemoteException;
 public void setHasPrognosis(boolean p0) throws java.rmi.RemoteException;
 public void setSchoolTypeID(int p0) throws java.rmi.RemoteException;
 public void setSeasonID(int p0) throws java.rmi.RemoteException;
 public void setSortBy(int p0) throws java.rmi.RemoteException;
 public void setToTimestamp(java.lang.String p0) throws java.rmi.RemoteException;
 public void setUserID(int p0) throws java.rmi.RemoteException;
}
