package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportImportDivisionBoardHomeImpl extends com.idega.data.IDOFactory implements WorkReportImportDivisionBoardHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportImportDivisionBoard.class;
 }


 public WorkReportImportDivisionBoard create() throws javax.ejb.CreateException{
  return (WorkReportImportDivisionBoard) super.createIDO();
 }


public java.util.Collection findAllWorkReportDivisionBoardByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportImportDivisionBoardBMPBean)entity).ejbFindAllWorkReportDivisionBoardByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public WorkReportImportDivisionBoard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportImportDivisionBoard) super.findByPrimaryKeyIDO(pk);
 }



}