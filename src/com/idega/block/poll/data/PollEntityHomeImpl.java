package com.idega.block.poll.data;


public class PollEntityHomeImpl extends com.idega.data.IDOFactory implements PollEntityHome
{
 protected Class getEntityInterfaceClass(){
  return PollEntity.class;
 }

 public PollEntity create() throws javax.ejb.CreateException{
  return (PollEntity) super.idoCreate();
 }

 public PollEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PollEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PollEntity) super.idoFindByPrimaryKey(id);
 }

 public PollEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollEntity) super.idoFindByPrimaryKey(pk);
 }

 public PollEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}