package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportBoardMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportBoardMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportBoardMember.class;
 }


 public WorkReportBoardMember create() throws javax.ejb.CreateException{
  return (WorkReportBoardMember) super.createIDO();
 }


public java.util.Collection findAllClubMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportBoardMemberBMPBean)entity).ejbFindAllClubMembersByWorkReportIdOrderedByMemberName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
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