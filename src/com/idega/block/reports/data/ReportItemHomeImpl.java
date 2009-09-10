package com.idega.block.reports.data;


public class ReportItemHomeImpl extends com.idega.data.IDOFactory implements ReportItemHome
{
 protected Class getEntityInterfaceClass(){
  return ReportItem.class;
 }

 public ReportItem create() throws javax.ejb.CreateException{
  return (ReportItem) super.idoCreate();
 }

 public ReportItem createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReportItem findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReportItem) super.idoFindByPrimaryKey(id);
 }

 public ReportItem findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportItem) super.idoFindByPrimaryKey(pk);
 }

 public ReportItem findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}