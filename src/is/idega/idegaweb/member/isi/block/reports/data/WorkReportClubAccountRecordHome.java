package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportClubAccountRecordHome extends com.idega.data.IDOHome
{
 public WorkReportClubAccountRecord create() throws javax.ejb.CreateException;
 public WorkReportClubAccountRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}