package com.idega.block.tpos.data;


public class TPosAuthorisationEntriesBeanHomeImpl extends com.idega.data.IDOFactory implements TPosAuthorisationEntriesBeanHome
{
 protected Class getEntityInterfaceClass(){
  return TPosAuthorisationEntriesBean.class;
 }

 public TPosAuthorisationEntriesBean create() throws javax.ejb.CreateException{
  return (TPosAuthorisationEntriesBean) super.idoCreate();
 }

 public TPosAuthorisationEntriesBean createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TPosAuthorisationEntriesBean findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TPosAuthorisationEntriesBean) super.idoFindByPrimaryKey(id);
 }

 public TPosAuthorisationEntriesBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TPosAuthorisationEntriesBean) super.idoFindByPrimaryKey(pk);
 }

 public TPosAuthorisationEntriesBean findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}