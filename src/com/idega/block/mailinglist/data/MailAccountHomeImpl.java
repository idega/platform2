package com.idega.block.mailinglist.data;


public class MailAccountHomeImpl extends com.idega.data.IDOFactory implements MailAccountHome
{
 protected Class getEntityInterfaceClass(){
  return MailAccount.class;
 }

 public MailAccount create() throws javax.ejb.CreateException{
  return (MailAccount) super.idoCreate();
 }

 public MailAccount createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MailAccount findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MailAccount) super.idoFindByPrimaryKey(id);
 }

 public MailAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailAccount) super.idoFindByPrimaryKey(pk);
 }

 public MailAccount findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}