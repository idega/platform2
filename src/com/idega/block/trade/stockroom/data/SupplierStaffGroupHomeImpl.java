package com.idega.block.trade.stockroom.data;


public class SupplierStaffGroupHomeImpl extends com.idega.data.IDOFactory implements SupplierStaffGroupHome
{
 protected Class getEntityInterfaceClass(){
  return SupplierStaffGroup.class;
 }

 public SupplierStaffGroup create() throws javax.ejb.CreateException{
  return (SupplierStaffGroup) super.idoCreate();
 }

 public SupplierStaffGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public SupplierStaffGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (SupplierStaffGroup) super.idoFindByPrimaryKey(id);
 }

 public SupplierStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SupplierStaffGroup) super.idoFindByPrimaryKey(pk);
 }

 public SupplierStaffGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}