package com.idega.block.poll.data;


public class PollQuestionHomeImpl extends com.idega.data.IDOFactory implements PollQuestionHome
{
 protected Class getEntityInterfaceClass(){
  return PollQuestion.class;
 }

 public PollQuestion create() throws javax.ejb.CreateException{
  return (PollQuestion) super.idoCreate();
 }

 public PollQuestion createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PollQuestion findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PollQuestion) super.idoFindByPrimaryKey(id);
 }

 public PollQuestion findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollQuestion) super.idoFindByPrimaryKey(pk);
 }

 public PollQuestion findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}