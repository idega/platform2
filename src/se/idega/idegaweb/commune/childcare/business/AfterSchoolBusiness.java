package se.idega.idegaweb.commune.childcare.business;


public interface AfterSchoolBusiness extends ChildCareBusiness
{
 public boolean acceptAfterSchoolChoice(java.lang.Object p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public java.util.List createAfterSchoolChoices(com.idega.user.data.User p0,java.lang.Integer p1,java.lang.Integer[] p2,java.lang.String p3,java.lang.String[] p4,com.idega.block.school.data.SchoolSeason p5,java.lang.String p6,java.lang.String p7)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public boolean denyAfterSchoolChoice(java.lang.Object p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(java.lang.Integer p0,int p1,java.lang.Integer p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.AfterSchoolChoice findChoicesByChildAndProviderAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoicesByProvider(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findChoicesByProvider(int p0, String p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.AfterSchoolChoice getAfterSchoolChoice(java.lang.Object p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
}
