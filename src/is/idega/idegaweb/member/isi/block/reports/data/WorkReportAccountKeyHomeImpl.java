package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportAccountKeyHomeImpl extends com.idega.data.IDOFactory implements WorkReportAccountKeyHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportAccountKey.class;
 }


 public WorkReportAccountKey create() throws javax.ejb.CreateException{
  return (WorkReportAccountKey) super.createIDO();
 }


 public WorkReportAccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportAccountKey) super.findByPrimaryKeyIDO(pk);
 }



}