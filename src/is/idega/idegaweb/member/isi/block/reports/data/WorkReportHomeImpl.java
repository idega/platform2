package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportHomeImpl extends com.idega.data.IDOFactory implements WorkReportHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReport.class;
 }


 public WorkReport create() throws javax.ejb.CreateException{
  return (WorkReport) super.createIDO();
 }


public WorkReport findWorkReportByGroupIdAndYearOfReport(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportBMPBean)entity).ejbFindWorkReportByGroupIdAndYearOfReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReport findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReport) super.findByPrimaryKeyIDO(pk);
 }



}