package is.idega.idegaweb.golf.handicap.business;


public interface HandicapBusiness extends com.idega.business.IBOService
{
 public int getCourseHandicap(float p0,int p1,float p2,int p3) throws java.rmi.RemoteException;
 public int getCourseHandicap(float p0,is.idega.idegaweb.golf.course.data.Tee p1) throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.handicap.data.Scorecard getScorecard(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getScorecardsByUser(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getStrokes(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
}
