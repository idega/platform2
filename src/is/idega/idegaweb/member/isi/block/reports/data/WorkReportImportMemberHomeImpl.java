package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportImportMemberHomeImpl extends com.idega.data.IDOFactory implements WorkReportImportMemberHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportImportMember.class;
 }


 public WorkReportImportMember create() throws javax.ejb.CreateException{
  return (WorkReportImportMember) super.createIDO();
 }


public java.util.Collection findAllWorkReportMembersByWorkReportId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportImportMemberBMPBean)entity).ejbFindAllWorkReportMembersByWorkReportId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportImportMember findWorkReportMemberByUserIdAndWorkReportId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportImportMemberBMPBean)entity).ejbFindWorkReportMemberByUserIdAndWorkReportId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportImportMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportImportMember) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.String getFemaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportImportMemberBMPBean)entity).ejbHomeGetFemaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.String getMaleGenderString(){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.String theReturn = ((WorkReportImportMemberBMPBean)entity).ejbHomeGetMaleGenderString();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}