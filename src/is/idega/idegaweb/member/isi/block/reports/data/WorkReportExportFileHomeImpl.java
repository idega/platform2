package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportExportFileHomeImpl extends com.idega.data.IDOFactory implements WorkReportExportFileHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportExportFile.class;
 }


 public WorkReportExportFile create() throws javax.ejb.CreateException{
  return (WorkReportExportFile) super.createIDO();
 }


public java.util.Collection findWorkReportExportFileByUnionIdAndYear(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportExportFileBMPBean)entity).ejbFindWorkReportExportFileByUnionIdAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public WorkReportExportFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportExportFile) super.findByPrimaryKeyIDO(pk);
 }



}