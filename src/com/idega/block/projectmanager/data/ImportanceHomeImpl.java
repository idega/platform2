package com.idega.block.projectmanager.data;


public class ImportanceHomeImpl extends com.idega.data.IDOFactory implements ImportanceHome
{
 protected Class getEntityInterfaceClass(){
  return Importance.class;
 }

 public Importance create() throws javax.ejb.CreateException{
  return (Importance) super.idoCreate();
 }

 public Importance createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Importance findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Importance) super.idoFindByPrimaryKey(id);
 }

 public Importance findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Importance) super.idoFindByPrimaryKey(pk);
 }

 public Importance findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}