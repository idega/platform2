package com.idega.block.news.data;


public class NwNewsHomeImpl extends com.idega.data.IDOFactory implements NwNewsHome
{
 protected Class getEntityInterfaceClass(){
  return NwNews.class;
 }

 public NwNews create() throws javax.ejb.CreateException{
  return (NwNews) super.idoCreate();
 }

 public NwNews createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public NwNews findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (NwNews) super.idoFindByPrimaryKey(id);
 }

 public NwNews findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (NwNews) super.idoFindByPrimaryKey(pk);
 }

 public NwNews findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}