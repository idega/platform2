package com.idega.block.reports.data;


public class ReportInfoHomeImpl extends com.idega.data.IDOFactory implements ReportInfoHome
{
 protected Class getEntityInterfaceClass(){
  return ReportInfo.class;
 }

 public ReportInfo create() throws javax.ejb.CreateException{
  return (ReportInfo) super.idoCreate();
 }

 public ReportInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReportInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReportInfo) super.idoFindByPrimaryKey(id);
 }

 public ReportInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportInfo) super.idoFindByPrimaryKey(pk);
 }

 public ReportInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}