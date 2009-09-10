package com.idega.block.mailinglist.data;


public class MailinglistHomeImpl extends com.idega.data.IDOFactory implements MailinglistHome
{
 protected Class getEntityInterfaceClass(){
  return Mailinglist.class;
 }

 public Mailinglist create() throws javax.ejb.CreateException{
  return (Mailinglist) super.idoCreate();
 }

 public Mailinglist createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Mailinglist findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Mailinglist) super.idoFindByPrimaryKey(id);
 }

 public Mailinglist findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Mailinglist) super.idoFindByPrimaryKey(pk);
 }

 public Mailinglist findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}