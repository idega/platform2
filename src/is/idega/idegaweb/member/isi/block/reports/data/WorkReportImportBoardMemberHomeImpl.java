package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportImportBoardMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportImportBoardMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportImportBoardMember.class;
 }


 public WorkReportImportBoardMember create() throws javax.ejb.CreateException{
  return (WorkReportImportBoardMember) super.createIDO();
 }


public java.util.Collection findAllWorkReportBoardMembersByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportImportBoardMemberBMPBean)entity).ejbFindAllWorkReportBoardMembersByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportImportBoardMember findWorkReportBoardMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportImportBoardMemberBMPBean)entity).ejbFindWorkReportBoardMemberByUserIdAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportImportBoardMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportImportBoardMember) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getFemaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportImportBoardMemberBMPBean)entity).ejbHomeGetFemaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getMaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportImportBoardMemberBMPBean)entity).ejbHomeGetMaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}