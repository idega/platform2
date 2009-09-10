package com.idega.block.reports.data;


public class ReportEntityHomeImpl extends com.idega.data.IDOFactory implements ReportEntityHome
{
 protected Class getEntityInterfaceClass(){
  return ReportEntity.class;
 }

 public ReportEntity create() throws javax.ejb.CreateException{
  return (ReportEntity) super.idoCreate();
 }

 public ReportEntity createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReportEntity findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReportEntity) super.idoFindByPrimaryKey(id);
 }

 public ReportEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportEntity) super.idoFindByPrimaryKey(pk);
 }

 public ReportEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}