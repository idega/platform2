package com.idega.block.finance.data;


public class AccountInfoHomeImpl extends com.idega.data.IDOFactory implements AccountInfoHome
{
 protected Class getEntityInterfaceClass(){
  return AccountInfo.class;
 }

 public AccountInfo create() throws javax.ejb.CreateException{
  return (AccountInfo) super.idoCreate();
 }

 public AccountInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountInfo) super.idoFindByPrimaryKey(id);
 }

 public AccountInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountInfo) super.idoFindByPrimaryKey(pk);
 }

 public AccountInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}