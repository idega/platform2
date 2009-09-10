package com.idega.block.email.data;


public class MailGroupHomeImpl extends com.idega.data.IDOFactory implements MailGroupHome
{
 protected Class getEntityInterfaceClass(){
  return MailGroup.class;
 }

 public MailGroup create() throws javax.ejb.CreateException{
  return (MailGroup) super.idoCreate();
 }

 public MailGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MailGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MailGroup) super.idoFindByPrimaryKey(id);
 }

 public MailGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailGroup) super.idoFindByPrimaryKey(pk);
 }

 public MailGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}