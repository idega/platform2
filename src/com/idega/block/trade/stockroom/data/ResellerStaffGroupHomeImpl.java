package com.idega.block.trade.stockroom.data;


public class ResellerStaffGroupHomeImpl extends com.idega.data.IDOFactory implements ResellerStaffGroupHome
{
 protected Class getEntityInterfaceClass(){
  return ResellerStaffGroup.class;
 }

 public ResellerStaffGroup create() throws javax.ejb.CreateException{
  return (ResellerStaffGroup) super.idoCreate();
 }

 public ResellerStaffGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ResellerStaffGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ResellerStaffGroup) super.idoFindByPrimaryKey(id);
 }

 public ResellerStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ResellerStaffGroup) super.idoFindByPrimaryKey(pk);
 }

 public ResellerStaffGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}