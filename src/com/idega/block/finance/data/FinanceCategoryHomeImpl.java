package com.idega.block.finance.data;


public class FinanceCategoryHomeImpl extends com.idega.data.IDOFactory implements FinanceCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceCategory.class;
 }

 public FinanceCategory create() throws javax.ejb.CreateException{
  return (FinanceCategory) super.idoCreate();
 }

 public FinanceCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public FinanceCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (FinanceCategory) super.idoFindByPrimaryKey(id);
 }

 public FinanceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceCategory) super.idoFindByPrimaryKey(pk);
 }

 public FinanceCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}