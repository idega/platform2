package com.idega.block.contract.data;


public class ContractCategoryHomeImpl extends com.idega.data.IDOFactory implements ContractCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ContractCategory.class;
 }


 public ContractCategory create() throws javax.ejb.CreateException{
  return (ContractCategory) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractCategoryBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByObjectInstance(com.idega.core.component.data.ICObjectInstance p0)throws javax.ejb.FinderException,com.idega.data.IDORelationshipException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractCategoryBMPBean)entity).ejbFindByObjectInstance(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ContractCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractCategory) super.findByPrimaryKeyIDO(pk);
 }


public com.idega.block.contract.data.ContractCategory create(int p0,int p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.block.contract.data.ContractCategory theReturn = ((ContractCategoryBMPBean)entity).ejbHomeCreate(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public boolean updateDescription(int p0,java.lang.String p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ContractCategoryBMPBean)entity).ejbHomeUpdateDescription(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}