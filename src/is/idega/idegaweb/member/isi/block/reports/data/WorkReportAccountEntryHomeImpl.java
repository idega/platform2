package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportAccountEntryHomeImpl extends com.idega.data.IDOFactory implements WorkReportAccountEntryHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportAccountEntry.class;
 }


 public WorkReportAccountEntry create() throws javax.ejb.CreateException{
  return (WorkReportAccountEntry) super.createIDO();
 }


public java.util.Collection findAllWorkReportAccountEntriesByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportAccountEntryBMPBean)entity).ejbFindAllWorkReportAccountEntriesByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public WorkReportAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportAccountEntry) super.findByPrimaryKeyIDO(pk);
 }



}