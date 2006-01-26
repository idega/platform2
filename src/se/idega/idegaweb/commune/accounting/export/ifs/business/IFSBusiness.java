package se.idega.idegaweb.commune.accounting.export.ifs.business;


public interface IFSBusiness extends com.idega.business.IBOService
{
 public void createFiles(java.lang.String p0,com.idega.util.IWTimestamp p1,java.lang.String p2,com.idega.user.data.User p3,java.util.Locale p4) throws java.rmi.RemoteException;
 public void deleteFiles(java.lang.String p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.export.business.ExportBusiness getExportBusiness() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader getIFSCheckHeaderBySchoolCategory(com.idega.block.school.data.SchoolCategory p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader getIFSCheckHeaderBySchoolCategory(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection getIFSCheckRecordByHeaderId(int p0) throws java.rmi.RemoteException;
 public java.util.Collection getJournalLog() throws java.rmi.RemoteException;
 public java.util.Collection getJournalLogBySchoolCategory(com.idega.block.school.data.SchoolCategory p0) throws java.rmi.RemoteException;
 public java.util.Collection getJournalLogBySchoolCategory(java.lang.String p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness getPostingBusiness() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.school.business.ProviderBusiness getProviderBusiness() throws java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserBusiness() throws java.rmi.RemoteException;
 public void moveFiles(java.lang.String p0)throws se.idega.idegaweb.commune.accounting.export.business.MoveFileException, java.rmi.RemoteException;
}
