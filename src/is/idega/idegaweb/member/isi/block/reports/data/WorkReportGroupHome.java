package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportGroupHome extends com.idega.data.IDOHome
{
 public WorkReportGroup create() throws javax.ejb.CreateException;
 public WorkReportGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}