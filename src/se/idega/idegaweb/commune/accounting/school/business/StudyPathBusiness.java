package se.idega.idegaweb.commune.accounting.school.business;


public interface StudyPathBusiness extends com.idega.business.IBOService
{
 public void deleteStudyPath(java.lang.String p0)throws se.idega.idegaweb.commune.accounting.school.business.StudyPathException, java.rmi.RemoteException;
 public java.util.Collection findAllStudyPaths() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolStudyPath getStudyPath(java.lang.String p0)throws se.idega.idegaweb.commune.accounting.school.business.StudyPathException, java.rmi.RemoteException;
 public void saveStudyPath(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws se.idega.idegaweb.commune.accounting.school.business.StudyPathException, java.rmi.RemoteException;
}
