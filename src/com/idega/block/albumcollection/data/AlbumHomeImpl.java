package com.idega.block.albumcollection.data;


public class AlbumHomeImpl extends com.idega.data.IDOFactory implements AlbumHome
{
 protected Class getEntityInterfaceClass(){
  return Album.class;
 }

 public Album create() throws javax.ejb.CreateException{
  return (Album) super.idoCreate();
 }

 public Album createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Album findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Album) super.idoFindByPrimaryKey(id);
 }

 public Album findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Album) super.idoFindByPrimaryKey(pk);
 }

 public Album findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}