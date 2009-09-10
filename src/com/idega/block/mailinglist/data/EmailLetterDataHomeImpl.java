package com.idega.block.mailinglist.data;


public class EmailLetterDataHomeImpl extends com.idega.data.IDOFactory implements EmailLetterDataHome
{
 protected Class getEntityInterfaceClass(){
  return EmailLetterData.class;
 }

 public EmailLetterData create() throws javax.ejb.CreateException{
  return (EmailLetterData) super.idoCreate();
 }

 public EmailLetterData createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public EmailLetterData findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (EmailLetterData) super.idoFindByPrimaryKey(id);
 }

 public EmailLetterData findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EmailLetterData) super.idoFindByPrimaryKey(pk);
 }

 public EmailLetterData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}