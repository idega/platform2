package com.idega.block.timesheet.data;


public class ProjectExtraHomeImpl extends com.idega.data.IDOFactory implements ProjectExtraHome
{
 protected Class getEntityInterfaceClass(){
  return ProjectExtra.class;
 }

 public ProjectExtra create() throws javax.ejb.CreateException{
  return (ProjectExtra) super.idoCreate();
 }

 public ProjectExtra createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ProjectExtra findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ProjectExtra) super.idoFindByPrimaryKey(id);
 }

 public ProjectExtra findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProjectExtra) super.idoFindByPrimaryKey(pk);
 }

 public ProjectExtra findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}