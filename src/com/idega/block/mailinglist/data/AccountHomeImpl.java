package com.idega.block.mailinglist.data;


public class AccountHomeImpl extends com.idega.data.IDOFactory implements AccountHome
{
 protected Class getEntityInterfaceClass(){
  return Account.class;
 }

 public Account create() throws javax.ejb.CreateException{
  return (Account) super.idoCreate();
 }

 public Account createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Account findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Account) super.idoFindByPrimaryKey(id);
 }

 public Account findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Account) super.idoFindByPrimaryKey(pk);
 }

 public Account findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}