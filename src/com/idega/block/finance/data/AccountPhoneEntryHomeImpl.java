package com.idega.block.finance.data;


public class AccountPhoneEntryHomeImpl extends com.idega.data.IDOFactory implements AccountPhoneEntryHome
{
 protected Class getEntityInterfaceClass(){
  return AccountPhoneEntry.class;
 }

 public AccountPhoneEntry create() throws javax.ejb.CreateException{
  return (AccountPhoneEntry) super.idoCreate();
 }

 public AccountPhoneEntry createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AccountPhoneEntry findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AccountPhoneEntry) super.idoFindByPrimaryKey(id);
 }

 public AccountPhoneEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountPhoneEntry) super.idoFindByPrimaryKey(pk);
 }

 public AccountPhoneEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}