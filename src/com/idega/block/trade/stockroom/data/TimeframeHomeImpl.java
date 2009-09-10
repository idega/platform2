package com.idega.block.trade.stockroom.data;


public class TimeframeHomeImpl extends com.idega.data.IDOFactory implements TimeframeHome
{
 protected Class getEntityInterfaceClass(){
  return Timeframe.class;
 }

 public Timeframe create() throws javax.ejb.CreateException{
  return (Timeframe) super.idoCreate();
 }

 public Timeframe createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Timeframe findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Timeframe) super.idoFindByPrimaryKey(id);
 }

 public Timeframe findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Timeframe) super.idoFindByPrimaryKey(pk);
 }

 public Timeframe findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}