package com.idega.block.email.data;


public class MailTopicHomeImpl extends com.idega.data.IDOFactory implements MailTopicHome
{
 protected Class getEntityInterfaceClass(){
  return MailTopic.class;
 }

 public MailTopic create() throws javax.ejb.CreateException{
  return (MailTopic) super.idoCreate();
 }

 public MailTopic createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MailTopic findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MailTopic) super.idoFindByPrimaryKey(id);
 }

 public MailTopic findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MailTopic) super.idoFindByPrimaryKey(pk);
 }

 public MailTopic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}