package se.idega.idegaweb.commune.childcare.business;


public interface AfterSchoolBusiness extends com.idega.business.IBOService
{
 public boolean acceptAfterSchoolChoice(java.lang.Object p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.data.AfterSchoolChoice createAfterSchoolChoice(com.idega.user.data.User p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.sql.Timestamp p5,com.idega.block.process.data.CaseStatus p6,com.idega.block.process.data.Case p7,java.sql.Date p8,com.idega.block.school.data.SchoolSeason p9)throws javax.ejb.CreateException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List createAfterSchoolChoices(com.idega.user.data.User p0,java.lang.Integer p1,java.lang.Integer[] p2,java.lang.String p3,java.sql.Date p4,com.idega.block.school.data.SchoolSeason p5)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public boolean denyAfterSchoolChoice(java.lang.Object p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.data.AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(java.lang.Integer p0,int p1,java.lang.Integer p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoicesByProvider(int p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.data.AfterSchoolChoice getAfterSchoolChoice(java.lang.Object p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
}
