package com.idega.block.building.data;


public class ComplexHomeImpl extends com.idega.data.IDOFactory implements ComplexHome
{
 protected Class getEntityInterfaceClass(){
  return Complex.class;
 }

 public Complex create() throws javax.ejb.CreateException{
  return (Complex) super.idoCreate();
 }

 public Complex createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Complex findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Complex) super.idoFindByPrimaryKey(id);
 }

 public Complex findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Complex) super.idoFindByPrimaryKey(pk);
 }

 public Complex findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}