package com.idega.block.school.data;


public class SchoolCategoryHomeImpl extends com.idega.data.IDOFactory implements SchoolCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolCategory.class;
 }


 public SchoolCategory create() throws javax.ejb.CreateException{
  return (SchoolCategory) super.createIDO();
 }


public java.util.Collection findAllCategories()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolCategoryBMPBean)entity).ejbFindAllCategories();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolCategory) super.findByPrimaryKeyIDO(pk);
 }



}