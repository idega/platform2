package com.idega.block.email.data;


public class MailListHomeImpl extends com.idega.data.IDOFactory implements MailListHome
{
 protected Class getEntityInterfaceClass(){
  return MailList.class;
 }

 public MailList create() throws javax.ejb.CreateException{
  return (MailList) super.idoCreate();
 }

 public MailList createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MailList findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MailList) super.idoFindByPrimaryKey(id);
 }

 public MailList findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailList) super.idoFindByPrimaryKey(pk);
 }

 public MailList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}