package se.idega.idegaweb.commune.childcare.data;


public interface AfterSchoolChoiceHome extends com.idega.data.IDOHome
{
 public AfterSchoolChoice create() throws javax.ejb.CreateException;
 public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AfterSchoolChoice findActiveApplicationByChild(java.lang.Integer p0)throws javax.ejb.FinderException;
 public AfterSchoolChoice findActiveApplicationByChildAndStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndNotInStatus(java.lang.Integer p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndNotInStatus(java.lang.Integer p0,int p1,java.sql.Date p2,java.sql.Date p3,java.lang.String[] p4,int p5,int p6)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(java.lang.Integer p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(java.lang.Integer p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatusNotRejected(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllChildCasesByProvider(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findApplicationByChild(java.lang.Integer p0)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndChoiceNumber(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndChoiceNumberInStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndChoiceNumberNotInStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndChoiceNumberWithStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationByChildAndNotInStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndProvider(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findApplicationByChildAndProviderAndStatus(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndDate(java.lang.Integer p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(java.lang.Integer p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException;
 public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndSeason(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findNewestApplication(java.lang.Integer p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findOldestApplication(java.lang.Integer p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public int getNumberOfActiveApplications(java.lang.Integer p0)throws com.idega.data.IDOException;
 public int getNumberOfApplications(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getNumberOfApplications(java.lang.Integer p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getNumberOfApplications(java.lang.Integer p0,java.lang.String[] p1,int p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException;
 public int getNumberOfApplicationsByProviderAndChoiceNumber(java.lang.Integer p0,java.lang.Integer p1)throws com.idega.data.IDOException;
 public int getNumberOfApplicationsForChild(java.lang.Integer p0)throws com.idega.data.IDOException;
 public int getNumberOfApplicationsForChild(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getNumberOfApplicationsForChildNotInStatus(java.lang.Integer p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getNumberOfPlacedApplications(java.lang.Integer p0,java.lang.Integer p1,java.lang.String[] p2)throws com.idega.data.IDOException;
 public int getPositionInQueue(java.sql.Date p0,java.lang.Integer p1,java.lang.String[] p2)throws com.idega.data.IDOException;
 public int getPositionInQueue(java.sql.Date p0,java.lang.Integer p1,java.lang.String p2)throws com.idega.data.IDOException;
 public int getPositionInQueueByDate(java.lang.Integer p0,java.sql.Date p1,java.lang.Integer p2,java.lang.String[] p3)throws com.idega.data.IDOException;
 public int getPositionInQueueByDate(java.lang.Integer p0,java.sql.Date p1,java.lang.Integer p2,java.lang.String p3)throws com.idega.data.IDOException;
 public int getQueueSizeByAreaInStatus(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getQueueSizeByAreaNotInStatus(java.lang.Integer p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getQueueSizeInStatus(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getQueueSizeNotInStatus(java.lang.Integer p0,java.lang.String[] p1)throws com.idega.data.IDOException;

}