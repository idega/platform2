package com.idega.block.email.data;


public class MailLetterHomeImpl extends com.idega.data.IDOFactory implements MailLetterHome
{
 protected Class getEntityInterfaceClass(){
  return MailLetter.class;
 }

 public MailLetter create() throws javax.ejb.CreateException{
  return (MailLetter) super.idoCreate();
 }

 public MailLetter createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MailLetter findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MailLetter) super.idoFindByPrimaryKey(id);
 }

 public MailLetter findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailLetter) super.idoFindByPrimaryKey(pk);
 }

 public MailLetter findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}