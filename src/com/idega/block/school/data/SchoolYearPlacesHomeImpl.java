package com.idega.block.school.data;


public class SchoolYearPlacesHomeImpl extends com.idega.data.IDOFactory implements SchoolYearPlacesHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolYearPlaces.class;
 }


 public SchoolYearPlaces create() throws javax.ejb.CreateException{
  return (SchoolYearPlaces) super.createIDO();
 }


public java.util.Collection findAllBySchool(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolYearPlacesBMPBean)entity).ejbFindAllBySchool(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolYearPlaces findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolYearPlaces) super.findByPrimaryKeyIDO(pk);
 }



}