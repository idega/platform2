package com.idega.block.finance.data;


public class TariffGroupHomeImpl extends com.idega.data.IDOFactory implements TariffGroupHome
{
 protected Class getEntityInterfaceClass(){
  return TariffGroup.class;
 }

 public TariffGroup create() throws javax.ejb.CreateException{
  return (TariffGroup) super.idoCreate();
 }

 public TariffGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TariffGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TariffGroup) super.idoFindByPrimaryKey(id);
 }

 public TariffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TariffGroup) super.idoFindByPrimaryKey(pk);
 }

 public TariffGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}