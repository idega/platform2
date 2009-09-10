package com.idega.block.poll.data;


public class PollAnswerHomeImpl extends com.idega.data.IDOFactory implements PollAnswerHome
{
 protected Class getEntityInterfaceClass(){
  return PollAnswer.class;
 }

 public PollAnswer create() throws javax.ejb.CreateException{
  return (PollAnswer) super.idoCreate();
 }

 public PollAnswer createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PollAnswer findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PollAnswer) super.idoFindByPrimaryKey(id);
 }

 public PollAnswer findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollAnswer) super.idoFindByPrimaryKey(pk);
 }

 public PollAnswer findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}