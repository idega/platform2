package com.idega.block.finance.data;


public class TariffIndexHomeImpl extends com.idega.data.IDOFactory implements TariffIndexHome
{
 protected Class getEntityInterfaceClass(){
  return TariffIndex.class;
 }

 public TariffIndex create() throws javax.ejb.CreateException{
  return (TariffIndex) super.idoCreate();
 }

 public TariffIndex createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TariffIndex findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TariffIndex) super.idoFindByPrimaryKey(id);
 }

 public TariffIndex findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TariffIndex) super.idoFindByPrimaryKey(pk);
 }

 public TariffIndex findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}