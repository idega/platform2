package com.idega.block.contract.data;


public class ContractTextHomeImpl extends com.idega.data.IDOFactory implements ContractTextHome
{
 protected Class getEntityInterfaceClass(){
  return ContractText.class;
 }

 public ContractText create() throws javax.ejb.CreateException{
  return (ContractText) super.idoCreate();
 }

 public ContractText createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ContractText findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ContractText) super.idoFindByPrimaryKey(id);
 }

 public ContractText findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractText) super.idoFindByPrimaryKey(pk);
 }

 public ContractText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}