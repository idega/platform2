package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportAccountKeyHome extends com.idega.data.IDOHome
{
 public WorkReportAccountKey create() throws javax.ejb.CreateException;
 public WorkReportAccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}