package com.idega.block.school.data;

import java.util.Collection;


public class SchoolSubAreaHomeImpl extends com.idega.data.IDOFactory implements SchoolSubAreaHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolSubArea.class;
 }


 public SchoolSubArea create() throws javax.ejb.CreateException{
  return (SchoolSubArea) super.createIDO();
 }


public Collection findAllByArea(SchoolArea p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolSubAreaBMPBean)entity).ejbFindAllByArea(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolSubArea findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolSubArea) super.findByPrimaryKeyIDO(pk);
 }

 public java.util.Collection findAllSchoolSubAreas()throws javax.ejb.FinderException{
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((SchoolSubAreaBMPBean)entity).ejbFindAllSchoolSubAreas();
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }


}