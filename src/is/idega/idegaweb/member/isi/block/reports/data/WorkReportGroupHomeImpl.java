package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportGroupHomeImpl extends com.idega.data.IDOFactory implements WorkReportGroupHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReportGroup.class;
 }


 public WorkReportGroup create() throws javax.ejb.CreateException{
  return (WorkReportGroup) super.createIDO();
 }


public java.util.Collection findAllWorkReportGroupsByGroupTypeAndYear(java.lang.String p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportGroupBMPBean)entity).ejbFindAllWorkReportGroupsByGroupTypeAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWorkReportGroupsByYear(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportGroupBMPBean)entity).ejbFindAllWorkReportGroupsByYear(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReportGroup findWorkReportGroupByGroupIdAndYear(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportGroupBMPBean)entity).ejbFindWorkReportGroupByGroupIdAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public WorkReportGroup findWorkReportGroupByNameAndYear(java.lang.String p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportGroupBMPBean)entity).ejbFindWorkReportGroupByNameAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public WorkReportGroup findWorkReportGroupByShortNameAndYear(java.lang.String p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportGroupBMPBean)entity).ejbFindWorkReportGroupByShortNameAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReportGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReportGroup) super.findByPrimaryKeyIDO(pk);
 }



}