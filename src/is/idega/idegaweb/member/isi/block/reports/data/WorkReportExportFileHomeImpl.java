package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportExportFileHomeImpl extends com.idega.data.IDOFactory implements WorkReportExportFileHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportExportFile.class;
 }


 public WorkReportExportFile create() throws javax.ejb.CreateException{
  return (WorkReportExportFile) super.createIDO();
 }


public WorkReportExportFile findWorkReportExportFileByGroupIdAndYear(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportExportFileBMPBean)entity).ejbFindWorkReportExportFileByGroupIdAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportExportFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportExportFile) super.findByPrimaryKeyIDO(pk);
 }



}