package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;


public class ApartmentCategoryHomeImpl extends com.idega.data.IDOFactory implements ApartmentCategoryHome
{
	
 protected Class getEntityInterfaceClass(){
  return ApartmentCategory.class;
 }


 public ApartmentCategory create() throws javax.ejb.CreateException{
  return (ApartmentCategory) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentCategoryBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


 public ApartmentCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentCategory) super.findByPrimaryKeyIDO(pk);
 }

 	/* (non-Javadoc)
	 * @see com.idega.block.building.data.ApartmentCategoryHome#findByComplex(java.lang.Integer)
	 */
	public Collection findByComplex(Integer complexID) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ApartmentCategoryBMPBean)entity).ejbFindByComplex(complexID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}


}