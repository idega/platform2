package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportGroupHome extends com.idega.data.IDOHome
{
 public WorkReportGroup create() throws javax.ejb.CreateException;
 public WorkReportGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportGroupsByGroupTypeAndYear(java.lang.String p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportGroupsByYear(int p0)throws javax.ejb.FinderException;
 public WorkReportGroup findWorkReportGroupByGroupIdAndYear(int p0,int p1)throws javax.ejb.FinderException;
 public WorkReportGroup findWorkReportGroupByNameAndYear(java.lang.String p0,int p1)throws javax.ejb.FinderException;
 public WorkReportGroup findWorkReportGroupByShortNameAndYear(java.lang.String p0,int p1)throws javax.ejb.FinderException;

}