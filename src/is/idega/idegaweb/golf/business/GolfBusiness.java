package is.idega.idegaweb.golf.business;


public interface GolfBusiness extends com.idega.business.IBOService
{
 public is.idega.idegaweb.golf.course.business.CourseBusiness getCourseBusiness() throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.handicap.business.HandicapBusiness getHandicapBusiness() throws java.rmi.RemoteException;
}
