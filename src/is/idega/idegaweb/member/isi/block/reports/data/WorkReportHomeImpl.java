package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportHomeImpl extends com.idega.data.IDOFactory implements WorkReportHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReport.class;
 }


 public WorkReport create() throws javax.ejb.CreateException{
  return (WorkReport) super.createIDO();
 }


 public WorkReport findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReport) super.findByPrimaryKeyIDO(pk);
 }



}