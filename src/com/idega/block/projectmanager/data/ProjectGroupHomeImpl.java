package com.idega.block.projectmanager.data;


public class ProjectGroupHomeImpl extends com.idega.data.IDOFactory implements ProjectGroupHome
{
 protected Class getEntityInterfaceClass(){
  return ProjectGroup.class;
 }

 public ProjectGroup create() throws javax.ejb.CreateException{
  return (ProjectGroup) super.idoCreate();
 }

 public ProjectGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ProjectGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ProjectGroup) super.idoFindByPrimaryKey(id);
 }

 public ProjectGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProjectGroup) super.idoFindByPrimaryKey(pk);
 }

 public ProjectGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}