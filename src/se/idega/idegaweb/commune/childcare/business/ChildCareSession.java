package se.idega.idegaweb.commune.childcare.business;


public interface ChildCareSession extends com.idega.business.IBOSession
{
 public int getApplicationID();
 public se.idega.idegaweb.commune.childcare.business.ChildCareBusiness getChildCareBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChildCareID()throws java.rmi.RemoteException;
 public int getChildID();
 public int getCheckID();
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getFromTimestamp();
 public int getGroupID() throws java.rmi.RemoteException;
 public java.lang.String getParameterApplicationID();
 public java.lang.String getParameterChildCareID();
 public java.lang.String getParameterFrom();
 public java.lang.String getParameterGroupID();
 public java.lang.String getParameterSortBy();
 public java.lang.String getParameterTo();
 public java.lang.String getParameterUserID();
 public java.lang.String getParameterCheckID();
 public com.idega.block.school.data.School getProvider()throws java.rmi.RemoteException;
 public int getSortBy();
 public com.idega.util.IWTimestamp getToTimestamp();
 public int getUserID();
 public boolean hasOutdatedPrognosis();
 public boolean hasPrognosis()throws java.rmi.RemoteException;
 public void setApplicationID(int p0);
 public void setChildCareID(int p0);
 public void setChildID(int p0);
 public void setCheckID(int p0);
 public void setFromTimestamp(java.lang.String p0);
 public void setGroupID(int p0);
 public void setHasOutdatedPrognosis(boolean hasOutdatedPrognosis);
 public void setHasPrognosis(boolean p0);
 public void setSortBy(int p0);
 public void setToTimestamp(java.lang.String p0);
 public void setUserID(int p0);
}
