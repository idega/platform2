package se.idega.idegaweb.commune.provider.business;


public interface ProviderSession extends com.idega.business.IBOSession
{
 public java.lang.String getParameterSeasonID() throws java.rmi.RemoteException;
 public java.lang.String getParameterStudyPathID() throws java.rmi.RemoteException;
 public java.lang.String getParameterYearID() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getProvider()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getProviderID() throws java.rmi.RemoteException;
 public int getSeasonID() throws java.rmi.RemoteException;
 public int getStudyPathID() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getYearID() throws java.rmi.RemoteException;
 public void setProviderID(int p0) throws java.rmi.RemoteException;
 public void setSeasonID(int p0) throws java.rmi.RemoteException;
 public void setStudyPathID(int p0) throws java.rmi.RemoteException;
 public void setYearID(int p0) throws java.rmi.RemoteException;
}
