package com.idega.block.albumcollection.data;


public class PerformerHomeImpl extends com.idega.data.IDOFactory implements PerformerHome
{
 protected Class getEntityInterfaceClass(){
  return Performer.class;
 }

 public Performer create() throws javax.ejb.CreateException{
  return (Performer) super.idoCreate();
 }

 public Performer createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Performer findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Performer) super.idoFindByPrimaryKey(id);
 }

 public Performer findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Performer) super.idoFindByPrimaryKey(pk);
 }

 public Performer findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}