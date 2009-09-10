package com.idega.block.reports.data;


public class ReportCategoryAttributeHomeImpl extends com.idega.data.IDOFactory implements ReportCategoryAttributeHome
{
 protected Class getEntityInterfaceClass(){
  return ReportCategoryAttribute.class;
 }

 public ReportCategoryAttribute create() throws javax.ejb.CreateException{
  return (ReportCategoryAttribute) super.idoCreate();
 }

 public ReportCategoryAttribute createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReportCategoryAttribute findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReportCategoryAttribute) super.idoFindByPrimaryKey(id);
 }

 public ReportCategoryAttribute findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportCategoryAttribute) super.idoFindByPrimaryKey(pk);
 }

 public ReportCategoryAttribute findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}