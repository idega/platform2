package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportHome extends com.idega.data.IDOHome
{
 public WorkReport create() throws javax.ejb.CreateException;
 public WorkReport findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportsByYearAndRegionalUnionGroupsOrderedByRegionalUnionNameAndClubName(int p0,java.util.Collection p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportsByYearOrderedByGroupType(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportsByYearOrderedByRegionalUnionNumberAndGroupNumber(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportsByYearRegionalUnionsAndClubsOrderedByRegionalUnionNameAndClubName(int p0,java.util.Collection p1,java.util.Collection p2)throws javax.ejb.FinderException;
 public WorkReport findWorkReportByGroupIdAndYearOfReport(int p0,int p1)throws javax.ejb.FinderException;
 public int getCountOfWorkReportsByStatusAndYear(java.lang.String p0,int p1);
 public int getTotalCountOfMembersForWorkReportYear(int p0);
 public int getTotalCountOfPlayersForWorkReportYearWithMainboardExcluded(int p0, java.lang.Integer p1);

}