package com.idega.block.building.data;


public class ComplexHomeImpl extends com.idega.data.IDOFactory implements ComplexHome
{
 protected Class getEntityInterfaceClass(){
  return Complex.class;
 }


 public Complex create() throws javax.ejb.CreateException{
  return (Complex) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplexBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Complex findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Complex) super.findByPrimaryKeyIDO(pk);
 }



}