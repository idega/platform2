package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportClubAccountRecordHomeImpl extends com.idega.data.IDOFactory implements WorkReportClubAccountRecordHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportClubAccountRecord.class;
 }


 public WorkReportClubAccountRecord create() throws javax.ejb.CreateException{
  return (WorkReportClubAccountRecord) super.createIDO();
 }


public java.util.Collection findAllRecordsByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportClubAccountRecordBMPBean)entity).ejbFindAllRecordsByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public WorkReportClubAccountRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportClubAccountRecord) super.findByPrimaryKeyIDO(pk);
 }



}