package is.idega.idegaweb.golf.business;


public interface ScorecardBusiness extends com.idega.business.IBOService
{
 public is.idega.idegaweb.golf.entity.Scorecard getBestRoundAfterDate(int p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.Scorecard getLastPlayedRound(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getNumberOfRoundsAfterDate(int p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public double getPointsAverage(int p0) throws java.rmi.RemoteException;
}
