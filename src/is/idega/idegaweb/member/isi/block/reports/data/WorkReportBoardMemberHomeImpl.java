package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportBoardMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportBoardMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportBoardMember.class;
 }


 public WorkReportBoardMember create() throws javax.ejb.CreateException{
  return (WorkReportBoardMember) super.createIDO();
 }


 public WorkReportBoardMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportBoardMember) super.findByPrimaryKeyIDO(pk);
 }



}