package com.idega.block.building.data;


public class ComplexTypeViewHomeImpl extends com.idega.data.IDOFactory implements ComplexTypeViewHome
{
 protected Class getEntityInterfaceClass(){
  return ComplexTypeView.class;
 }


public ComplexTypeView create(com.idega.block.building.data.ComplexTypeViewKey p0)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ComplexTypeViewBMPBean)entity).ejbCreate(p0);
	this.idoCheckInPooledEntity(entity);
	try{
		return this.findByPrimaryKey(pk);
	}
	catch(javax.ejb.FinderException fe){
		throw new com.idega.data.IDOCreateException(fe);
	}
	catch(Exception e){
		throw new com.idega.data.IDOCreateException(e);
	}
}

 public ComplexTypeView create() throws javax.ejb.CreateException{
  return (ComplexTypeView) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplexTypeViewBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ComplexTypeViewBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public ComplexTypeView findByPrimaryKey(com.idega.block.building.data.ComplexTypeViewKey p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ComplexTypeViewBMPBean)entity).ejbFindByPrimaryKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ComplexTypeView findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ComplexTypeView) super.findByPrimaryKeyIDO(pk);
 }



}