package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportImportDivisionBoardHome extends com.idega.data.IDOHome
{
 public WorkReportImportDivisionBoard create() throws javax.ejb.CreateException;
 public WorkReportImportDivisionBoard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportDivisionBoardByWorkReportId(int p0)throws javax.ejb.FinderException;

}