package com.idega.block.school.data;


public class SchoolSeasonHomeImpl extends com.idega.data.IDOFactory implements SchoolSeasonHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolSeason.class;
 }


 public SchoolSeason create() throws javax.ejb.CreateException{
  return (SchoolSeason) super.createIDO();
 }


public java.util.Collection findAllPreviousSchoolSeasons(com.idega.block.school.data.SchoolSeason p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolSeasonBMPBean)entity).ejbFindAllPreviousSchoolSeasons(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllSchoolSeasons()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolSeasonBMPBean)entity).ejbFindAllSchoolSeasons();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findSchoolSeasonsActiveInTimePeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolSeasonBMPBean)entity).ejbFindSchoolSeasonsActiveInTimePeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolSeason findSeasonByDate(java.sql.Date p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolSeasonBMPBean)entity).ejbFindSeasonByDate(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolSeason) super.findByPrimaryKeyIDO(pk);
 }



}