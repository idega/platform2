package com.idega.block.finance.data;


public class TariffHomeImpl extends com.idega.data.IDOFactory implements TariffHome
{
 protected Class getEntityInterfaceClass(){
  return Tariff.class;
 }

 public Tariff create() throws javax.ejb.CreateException{
  return (Tariff) super.idoCreate();
 }

 public Tariff createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Tariff findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Tariff) super.idoFindByPrimaryKey(id);
 }

 public Tariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tariff) super.idoFindByPrimaryKey(pk);
 }

 public Tariff findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}