package com.idega.block.albumcollection.data;


public class AlbumTypeHomeImpl extends com.idega.data.IDOFactory implements AlbumTypeHome
{
 protected Class getEntityInterfaceClass(){
  return AlbumType.class;
 }

 public AlbumType create() throws javax.ejb.CreateException{
  return (AlbumType) super.idoCreate();
 }

 public AlbumType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AlbumType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AlbumType) super.idoFindByPrimaryKey(id);
 }

 public AlbumType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AlbumType) super.idoFindByPrimaryKey(pk);
 }

 public AlbumType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}