package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportImportClubAccountRecordHomeImpl extends com.idega.data.IDOFactory implements WorkReportImportClubAccountRecordHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportImportClubAccountRecord.class;
 }


 public WorkReportImportClubAccountRecord create() throws javax.ejb.CreateException{
  return (WorkReportImportClubAccountRecord) super.createIDO();
 }


public java.util.Collection findAllRecordsByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportImportClubAccountRecordBMPBean)entity).ejbFindAllRecordsByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public WorkReportImportClubAccountRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportImportClubAccountRecord) super.findByPrimaryKeyIDO(pk);
 }



}