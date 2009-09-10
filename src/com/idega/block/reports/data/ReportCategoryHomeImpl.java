package com.idega.block.reports.data;


public class ReportCategoryHomeImpl extends com.idega.data.IDOFactory implements ReportCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return ReportCategory.class;
 }

 public ReportCategory create() throws javax.ejb.CreateException{
  return (ReportCategory) super.idoCreate();
 }

 public ReportCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReportCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReportCategory) super.idoFindByPrimaryKey(id);
 }

 public ReportCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportCategory) super.idoFindByPrimaryKey(pk);
 }

 public ReportCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}