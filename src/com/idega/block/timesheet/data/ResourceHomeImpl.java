package com.idega.block.timesheet.data;


public class ResourceHomeImpl extends com.idega.data.IDOFactory implements ResourceHome
{
 protected Class getEntityInterfaceClass(){
  return Resource.class;
 }

 public Resource create() throws javax.ejb.CreateException{
  return (Resource) super.idoCreate();
 }

 public Resource createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Resource findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Resource) super.idoFindByPrimaryKey(id);
 }

 public Resource findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Resource) super.idoFindByPrimaryKey(pk);
 }

 public Resource findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}