package com.idega.block.school.data;


public class SchoolSeasonHomeImpl extends com.idega.data.IDOFactory implements SchoolSeasonHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolSeason.class;
 }


 public SchoolSeason create() throws javax.ejb.CreateException{
  return (SchoolSeason) super.createIDO();
 }


public java.util.Collection findAllSchoolSeasons()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolSeasonBMPBean)entity).ejbFindAllSchoolSeasons();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolSeason) super.findByPrimaryKeyIDO(pk);
 }



}