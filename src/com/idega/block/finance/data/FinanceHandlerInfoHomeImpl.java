package com.idega.block.finance.data;


public class FinanceHandlerInfoHomeImpl extends com.idega.data.IDOFactory implements FinanceHandlerInfoHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceHandlerInfo.class;
 }

 public FinanceHandlerInfo create() throws javax.ejb.CreateException{
  return (FinanceHandlerInfo) super.idoCreate();
 }

 public FinanceHandlerInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public FinanceHandlerInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (FinanceHandlerInfo) super.idoFindByPrimaryKey(id);
 }

 public FinanceHandlerInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceHandlerInfo) super.idoFindByPrimaryKey(pk);
 }

 public FinanceHandlerInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}