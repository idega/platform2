package com.idega.block.finance.data;


public class AccountKeyHomeImpl extends com.idega.data.IDOFactory implements AccountKeyHome
{
 protected Class getEntityInterfaceClass(){
  return AccountKey.class;
 }

 public AccountKey create() throws javax.ejb.CreateException{
  return (AccountKey) super.idoCreate();
 }

 public AccountKey createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountKey findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountKey) super.idoFindByPrimaryKey(id);
 }

 public AccountKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountKey) super.idoFindByPrimaryKey(pk);
 }

 public AccountKey findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}