package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportMember.class;
 }


 public WorkReportMember create() throws javax.ejb.CreateException{
  return (WorkReportMember) super.createIDO();
 }


public java.util.Collection findAllWorkReportMembersByWorkReportIdOrderedByMemberName(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportMemberBMPBean)entity).ejbFindAllWorkReportMembersByWorkReportIdOrderedByMemberName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportMember findWorkReportMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportMemberBMPBean)entity).ejbFindWorkReportMemberByUserIdAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportMember) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getFemaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetFemaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getMaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportMemberBMPBean)entity).ejbHomeGetMaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}