package com.idega.block.tpos.data;


public class TPosAuthorisationEntriesBeanHomeImpl extends com.idega.data.IDOFactory implements TPosAuthorisationEntriesBeanHome
{
 protected Class getEntityInterfaceClass(){
  return TPosAuthorisationEntriesBean.class;
 }


 public TPosAuthorisationEntriesBean create() throws javax.ejb.CreateException{
  return (TPosAuthorisationEntriesBean) super.createIDO();
 }


 public TPosAuthorisationEntriesBean createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public TPosAuthorisationEntriesBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TPosAuthorisationEntriesBean) super.findByPrimaryKeyIDO(pk);
 }


 public TPosAuthorisationEntriesBean findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TPosAuthorisationEntriesBean) super.findByPrimaryKeyIDO(id);
 }


 public TPosAuthorisationEntriesBean findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


public java.util.Collection findByAuthorisationIdRsp(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((TPosAuthorisationEntriesBeanBMPBean)entity).ejbHomeFindByAuthorisationIdRsp(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}