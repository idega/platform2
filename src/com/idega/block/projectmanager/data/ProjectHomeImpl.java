package com.idega.block.projectmanager.data;


public class ProjectHomeImpl extends com.idega.data.IDOFactory implements ProjectHome
{
 protected Class getEntityInterfaceClass(){
  return Project.class;
 }

 public Project create() throws javax.ejb.CreateException{
  return (Project) super.idoCreate();
 }

 public Project createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Project findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Project) super.idoFindByPrimaryKey(id);
 }

 public Project findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Project) super.idoFindByPrimaryKey(pk);
 }

 public Project findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}