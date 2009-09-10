package com.idega.block.contract.data;


public class ContractTagHomeImpl extends com.idega.data.IDOFactory implements ContractTagHome
{
 protected Class getEntityInterfaceClass(){
  return ContractTag.class;
 }

 public ContractTag create() throws javax.ejb.CreateException{
  return (ContractTag) super.idoCreate();
 }

 public ContractTag createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }
 
 public ContractTag findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ContractTag) super.idoFindByPrimaryKey(id);
 }

 public ContractTag findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractTag) super.idoFindByPrimaryKey(pk);
 }

 public ContractTag findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }
 
 public java.util.Collection findAllByCategory(int p0) throws javax.ejb.FinderException{
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((ContractTagBMPBean)entity).ejbFindAllByCategory(p0);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }
 
 public java.util.Collection findAllByNameAndCategory(String name, int categoryId) throws javax.ejb.FinderException{
	 com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 java.util.Collection ids = ((ContractTagBMPBean)entity).ejbFindAllByNameAndCategory(name, categoryId);
	 this.idoCheckInPooledEntity(entity);
	 return this.getEntityCollectionForPrimaryKeys(ids);
 }
 


}