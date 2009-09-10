package com.idega.block.reports.data;


public class ReportHomeImpl extends com.idega.data.IDOFactory implements ReportHome
{
 protected Class getEntityInterfaceClass(){
  return Report.class;
 }

 public Report create() throws javax.ejb.CreateException{
  return (Report) super.idoCreate();
 }

 public Report createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Report findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Report) super.idoFindByPrimaryKey(id);
 }

 public Report findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Report) super.idoFindByPrimaryKey(pk);
 }

 public Report findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}