package is.idega.idegaweb.golf.handicap.business;


public interface HandicapBusiness extends com.idega.business.IBOService
{
 public int getCourseHandicap(float p0,is.idega.idegaweb.golf.course.data.Tee p1) throws java.rmi.RemoteException;
 public int getCourseHandicap(float p0,int p1,float p2,int p3) throws java.rmi.RemoteException;
 public int getCourseHandicap(float p0,int p1,float p2,int p3,int p4) throws java.rmi.RemoteException;
 public int getCourseHandicap(float p0,is.idega.idegaweb.golf.course.data.Tee p1,int p2) throws java.rmi.RemoteException;
 public int getPointsForHole(int p0,int p1,int p2,int p3) throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.handicap.data.Scorecard getScorecard(java.lang.Object p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getScorecardsByUser(java.lang.Object p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getStrokes(java.lang.Object p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void updateScorecard(java.lang.Object p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void updateScorecard(is.idega.idegaweb.golf.handicap.data.Scorecard p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
