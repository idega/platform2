package se.idega.idegaweb.commune.childcare.business;


public interface AfterSchoolBusiness extends ChildCareBusiness
{
 public se.idega.idegaweb.commune.childcare.data.AfterSchoolChoice createAfterSchoolChoice(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.sql.Timestamp p5,com.idega.block.process.data.CaseStatus p6,com.idega.block.process.data.Case p7,java.sql.Date p8,com.idega.block.school.data.SchoolSeason p9)throws javax.ejb.CreateException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List createAfterSchoolChoices(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer[] p2,java.lang.String p3,java.sql.Date p4,com.idega.block.school.data.SchoolSeason p5)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.data.AfterSchoolChoiceHome getAfterSchoolChoiceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness getSchoolChoiceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
