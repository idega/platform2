package com.idega.block.finance.data;


public class RoundInfoHomeImpl extends com.idega.data.IDOFactory implements RoundInfoHome
{
 protected Class getEntityInterfaceClass(){
  return RoundInfo.class;
 }

 public RoundInfo create() throws javax.ejb.CreateException{
  return (RoundInfo) super.idoCreate();
 }

 public RoundInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public RoundInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (RoundInfo) super.idoFindByPrimaryKey(id);
 }

 public RoundInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoundInfo) super.idoFindByPrimaryKey(pk);
 }

 public RoundInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}