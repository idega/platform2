package com.idega.block.reports.data;


public class ReportColumnInfoHomeImpl extends com.idega.data.IDOFactory implements ReportColumnInfoHome
{
 protected Class getEntityInterfaceClass(){
  return ReportColumnInfo.class;
 }

 public ReportColumnInfo create() throws javax.ejb.CreateException{
  return (ReportColumnInfo) super.idoCreate();
 }

 public ReportColumnInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReportColumnInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReportColumnInfo) super.idoFindByPrimaryKey(id);
 }

 public ReportColumnInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportColumnInfo) super.idoFindByPrimaryKey(pk);
 }

 public ReportColumnInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}