package com.idega.block.finance.data;


public class AccountTypeHomeImpl extends com.idega.data.IDOFactory implements AccountTypeHome
{
 protected Class getEntityInterfaceClass(){
  return AccountType.class;
 }

 public AccountType create() throws javax.ejb.CreateException{
  return (AccountType) super.idoCreate();
 }

 public AccountType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountType) super.idoFindByPrimaryKey(id);
 }

 public AccountType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountType) super.idoFindByPrimaryKey(pk);
 }

 public AccountType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}