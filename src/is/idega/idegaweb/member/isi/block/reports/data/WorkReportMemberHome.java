package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportMemberHome extends com.idega.data.IDOHome
{
 public WorkReportMember create() throws javax.ejb.CreateException;
 public WorkReportMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportMembersByWorkReportIdAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllWorkReportMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException;
 public WorkReportMember findWorkReportMemberBySocialSecurityNumberAndWorkReportId(java.lang.String p0,int p1)throws javax.ejb.FinderException;
 public WorkReportMember findWorkReportMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException;
 public int getCountOfFemaleMembersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0);
 public int getCountOfFemaleMembersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1);
 public int getCountOfFemaleMembersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1);
 public int getCountOfFemalePlayersByWorkReportAndWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1);
 public int getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2);
 public int getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2);
 public int getCountOfMaleMembersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0);
 public int getCountOfMaleMembersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1);
 public int getCountOfMaleMembersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1);
 public int getCountOfMalePlayersByWorkReportAndWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1);
 public int getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2);
 public int getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2);
 public int getCountOfMembersByWorkReport(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0);
 public int getCountOfMembersEqualOrOlderThanAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1);
 public int getCountOfMembersOfYoungerAgeByWorkReport(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1);
 public int getCountOfPlayersByWorkReportAndWorkReportGroup(is.idega.idegaweb.member.isi.block.reports.data.WorkReport p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p1);
 public int getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2);
 public int getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int p0,is.idega.idegaweb.member.isi.block.reports.data.WorkReport p1,is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup p2);
 public java.lang.String getFemaleGenderString();
 public java.lang.String getMaleGenderString();

}