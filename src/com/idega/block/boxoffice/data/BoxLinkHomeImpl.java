package com.idega.block.boxoffice.data;


public class BoxLinkHomeImpl extends com.idega.data.IDOFactory implements BoxLinkHome
{
 protected Class getEntityInterfaceClass(){
  return BoxLink.class;
 }

 public BoxLink create() throws javax.ejb.CreateException{
  return (BoxLink) super.idoCreate();
 }

 public BoxLink createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public BoxLink findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BoxLink) super.idoFindByPrimaryKey(id);
 }

 public BoxLink findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BoxLink) super.idoFindByPrimaryKey(pk);
 }

 public BoxLink findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}