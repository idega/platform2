package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportExportFileHome extends com.idega.data.IDOHome
{
 public WorkReportExportFile create() throws javax.ejb.CreateException;
 public WorkReportExportFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public WorkReportExportFile findWorkReportExportFileByGroupIdAndYear(int p0,int p1)throws javax.ejb.FinderException;

}