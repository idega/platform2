package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportClubMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportClubMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportClubMember.class;
 }


 public WorkReportClubMember create() throws javax.ejb.CreateException{
  return (WorkReportClubMember) super.createIDO();
 }


 public WorkReportClubMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportClubMember) super.findByPrimaryKeyIDO(pk);
 }



}