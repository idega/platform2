package is.idega.idegaweb.member.isi.block.reports.data;


public class WorkReportHomeImpl extends com.idega.data.IDOFactory implements WorkReportHome
{
 protected Class getEntityInterfaceClass(){
  return WorkReport.class;
 }


 public WorkReport create() throws javax.ejb.CreateException{
  return (WorkReport) super.createIDO();
 }


public java.util.Collection findAllWorkReportsByYearAndRegionalUnionGroupsOrderedByRegionalUnionNameAndClubName(int p0,java.util.Collection p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportBMPBean)entity).ejbFindAllWorkReportsByYearAndRegionalUnionGroupsOrderedByRegionalUnionNameAndClubName(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWorkReportsByYearOrderedByGroupType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportBMPBean)entity).ejbFindAllWorkReportsByYearOrderedByGroupType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWorkReportsByYearOrderedByRegionalUnionNumberAndGroupNumber(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportBMPBean)entity).ejbFindAllWorkReportsByYearOrderedByRegionalUnionNumberAndGroupNumber(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWorkReportsByYearRegionalUnionsAndClubsOrderedByRegionalUnionNameAndClubName(int p0,java.util.Collection p1,java.util.Collection p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((WorkReportBMPBean)entity).ejbFindAllWorkReportsByYearRegionalUnionsAndClubsOrderedByRegionalUnionNameAndClubName(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public WorkReport findWorkReportByGroupIdAndYearOfReport(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((WorkReportBMPBean)entity).ejbFindWorkReportByGroupIdAndYearOfReport(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public WorkReport findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WorkReport) super.findByPrimaryKeyIDO(pk);
 }


public int getCountOfWorkReportsByStatusAndYear(java.lang.String p0,int p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportBMPBean)entity).ejbHomeGetCountOfWorkReportsByStatusAndYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTotalCountOfMembersForWorkReportYear(int p0) {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((WorkReportBMPBean) entity).ejbHomeGetTotalCountOfMembersForWorkReportYear(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
	}

public int getTotalCountOfPlayersForWorkReportYearWithMainboardExcluded(int p0, java.lang.Integer p1, boolean p2) {
    com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
    int theReturn = ((WorkReportBMPBean) entity).ejbHomeGetTotalCountOfPlayersForWorkReportYearWithMainboardExcluded(p0, p1, p2);
    this.idoCheckInPooledEntity(entity);
    return theReturn;
}

public int getTotalCountOfCompetitorsForWorkReportYear(int p0) {
    com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
    int theReturn = ((WorkReportBMPBean) entity).ejbHomeGetTotalCountOfCompetitorsForWorkReportYear(p0);
    this.idoCheckInPooledEntity(entity);
    return theReturn;
}

}