package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportImportClubAccountRecordHome extends com.idega.data.IDOHome
{
 public WorkReportImportClubAccountRecord create() throws javax.ejb.CreateException;
 public WorkReportImportClubAccountRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllRecordsByWorkReportId(int p0)throws javax.ejb.FinderException;

}