package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportBoardMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportBoardMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportBoardMember.class;
 }


 public WorkReportBoardMember create() throws javax.ejb.CreateException{
  return (WorkReportBoardMember) super.createIDO();
 }


public java.util.Collection findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportBoardMemberBMPBean)entity).ejbFindAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportBoardMember findWorkReportBoardMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportBoardMemberBMPBean)entity).ejbFindWorkReportBoardMemberByUserIdAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportBoardMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportBoardMember) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getFemaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportBoardMemberBMPBean)entity).ejbHomeGetFemaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getMaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportBoardMemberBMPBean)entity).ejbHomeGetMaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}