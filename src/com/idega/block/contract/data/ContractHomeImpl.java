package com.idega.block.contract.data;


public class ContractHomeImpl extends com.idega.data.IDOFactory implements ContractHome
{
 protected Class getEntityInterfaceClass(){
  return Contract.class;
 }


 public Contract create() throws javax.ejb.CreateException{
  return (Contract) super.createIDO();
 }


 public Contract createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findAllByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindAllByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCategoryAndStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindAllByCategoryAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUser(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractBMPBean)entity).ejbFindAllByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Contract) super.findByPrimaryKeyIDO(pk);
 }


 public Contract findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Contract) super.findByPrimaryKeyIDO(id);
 }


 public Contract findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


public com.idega.block.contract.data.Contract create(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,java.lang.String p3,java.util.Map p4){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.block.contract.data.Contract theReturn = ((ContractBMPBean)entity).ejbHomeCreate(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public com.idega.block.contract.data.Contract create(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4,java.lang.String p5){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.block.contract.data.Contract theReturn = ((ContractBMPBean)entity).ejbHomeCreate(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public com.idega.block.contract.data.Contract create(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4,java.util.Map p5){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.block.contract.data.Contract theReturn = ((ContractBMPBean)entity).ejbHomeCreate(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection findFiles(int p0)throws javax.ejb.FinderException,com.idega.data.IDORelationshipException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((ContractBMPBean)entity).ejbHomeFindFiles(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountByCategory(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ContractBMPBean)entity).ejbHomeGetCountByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public boolean setStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException,com.idega.data.IDOLookupException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ContractBMPBean)entity).ejbHomeSetStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}