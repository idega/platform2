package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportClubMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportClubMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportClubMember.class;
 }


 public WorkReportClubMember create() throws javax.ejb.CreateException{
  return (WorkReportClubMember) super.createIDO();
 }


public java.util.Collection findAllClubMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportClubMemberBMPBean)entity).ejbFindAllClubMembersByWorkReportIdOrderedByMemberName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportClubMember findClubMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportClubMemberBMPBean)entity).ejbFindClubMemberByUserIdAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportClubMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportClubMember) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getFemaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportClubMemberBMPBean)entity).ejbHomeGetFemaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getMaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportClubMemberBMPBean)entity).ejbHomeGetMaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}