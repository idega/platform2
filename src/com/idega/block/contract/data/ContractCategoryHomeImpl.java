package com.idega.block.contract.data;


public class ContractCategoryHomeImpl extends com.idega.data.IDOFactory implements ContractCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ContractCategory.class;
 }

 public ContractCategory create() throws javax.ejb.CreateException{
  return (ContractCategory) super.idoCreate();
 }

 public ContractCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ContractCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ContractCategory) super.idoFindByPrimaryKey(id);
 }

 public ContractCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractCategory) super.idoFindByPrimaryKey(pk);
 }

 public ContractCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}