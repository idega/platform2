package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportDivisionBoardHome extends com.idega.data.IDOHome
{
 public WorkReportDivisionBoard create() throws javax.ejb.CreateException;
 public WorkReportDivisionBoard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportDivisionBoardByWorkReportId(int p0)throws javax.ejb.FinderException;

}