package com.idega.block.albumcollection.data;


public class LyricHomeImpl extends com.idega.data.IDOFactory implements LyricHome
{
 protected Class getEntityInterfaceClass(){
  return Lyric.class;
 }

 public Lyric create() throws javax.ejb.CreateException{
  return (Lyric) super.idoCreate();
 }

 public Lyric createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Lyric findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Lyric) super.idoFindByPrimaryKey(id);
 }

 public Lyric findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Lyric) super.idoFindByPrimaryKey(pk);
 }

 public Lyric findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}