package is.idega.idegaweb.golf.business;


public interface StatisticsBusiness extends com.idega.business.IBOService
{
 public java.lang.String getDecimalText(double p0) throws java.rmi.RemoteException;
 public int getHolesPlayedByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfBirdiesByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfBogeysByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfDoubleBogeysByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfEaglesByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfHolesPlayedByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfParsByMember(int p0) throws java.rmi.RemoteException;
 public int getNumberOfRoundsByMember(int p0) throws java.rmi.RemoteException;
 public double getNumberOnFairwayByMember(int p0) throws java.rmi.RemoteException;
 public double getNumberOnFairwayByTeeID(java.util.Collection p0) throws java.rmi.RemoteException;
 public double getNumberOnGreenByMember(int p0) throws java.rmi.RemoteException;
 public double getNumberOnGreenByTeeID(java.util.Collection p0) throws java.rmi.RemoteException;
 public java.lang.String getPercentText(double p0) throws java.rmi.RemoteException;
 public double getPuttAverageByMember(int p0) throws java.rmi.RemoteException;
 public double getPuttAverageByTeeID(java.util.Collection p0) throws java.rmi.RemoteException;
 public java.util.Collection getStatisticsByTeeID(java.util.Collection p0) throws java.rmi.RemoteException;
 public int getSumOfPuttsByMember(int p0) throws java.rmi.RemoteException;
 public int getSumOfStrokesByMember(int p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.TeeColor getTeeColor(is.idega.idegaweb.golf.entity.Tee p0) throws java.rmi.RemoteException;
 public java.util.Collection getTeeFromFieldIDAndHoleNumber(int p0,int p1) throws java.rmi.RemoteException;
}
