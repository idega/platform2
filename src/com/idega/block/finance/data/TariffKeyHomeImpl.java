package com.idega.block.finance.data;


public class TariffKeyHomeImpl extends com.idega.data.IDOFactory implements TariffKeyHome
{
 protected Class getEntityInterfaceClass(){
  return TariffKey.class;
 }

 public TariffKey create() throws javax.ejb.CreateException{
  return (TariffKey) super.idoCreate();
 }

 public TariffKey createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TariffKey findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TariffKey) super.idoFindByPrimaryKey(id);
 }

 public TariffKey findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TariffKey) super.idoFindByPrimaryKey(pk);
 }

 public TariffKey findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}