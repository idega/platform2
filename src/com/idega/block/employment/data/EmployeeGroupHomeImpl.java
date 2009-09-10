package com.idega.block.employment.data;


public class EmployeeGroupHomeImpl extends com.idega.data.IDOFactory implements EmployeeGroupHome
{
 protected Class getEntityInterfaceClass(){
  return EmployeeGroup.class;
 }

 public EmployeeGroup create() throws javax.ejb.CreateException{
  return (EmployeeGroup) super.idoCreate();
 }

 public EmployeeGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public EmployeeGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (EmployeeGroup) super.idoFindByPrimaryKey(id);
 }

 public EmployeeGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (EmployeeGroup) super.idoFindByPrimaryKey(pk);
 }

 public EmployeeGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}