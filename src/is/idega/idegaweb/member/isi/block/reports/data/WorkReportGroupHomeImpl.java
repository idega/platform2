package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportGroupHomeImpl extends com.idega.data.IDOFactory implements WorkReportGroupHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportGroup.class;
 }


 public WorkReportGroup create() throws javax.ejb.CreateException{
  return (WorkReportGroup) super.createIDO();
 }


 public WorkReportGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportGroup) super.findByPrimaryKeyIDO(pk);
 }



}