package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportAccountEntryHome extends com.idega.data.IDOHome
{
 public WorkReportAccountEntry create() throws javax.ejb.CreateException;
 public WorkReportAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportAccountEntriesByWorkReportId(int p0)throws javax.ejb.FinderException;

}