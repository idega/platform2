package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportDivisionBoardHomeImpl extends com.idega.data.IDOFactory implements WorkReportDivisionBoardHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportDivisionBoard.class;
 }


 public WorkReportDivisionBoard create() throws javax.ejb.CreateException{
  return (WorkReportDivisionBoard) super.createIDO();
 }


public java.util.Collection findAllWorkReportDivisionBoardByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportDivisionBoardBMPBean)entity).ejbFindAllWorkReportDivisionBoardByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportDivisionBoard findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportDivisionBoardBMPBean)entity).ejbFindWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportDivisionBoard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportDivisionBoard) super.findByPrimaryKeyIDO(pk);
 }



}