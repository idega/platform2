package com.idega.block.trade.data;


public class CurrencyHomeImpl extends com.idega.data.IDOFactory implements CurrencyHome
{
 protected Class getEntityInterfaceClass(){
  return Currency.class;
 }


 public Currency create() throws javax.ejb.CreateException{
  return (Currency) super.createIDO();
 }


 public Currency createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Currency findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Currency) super.findByPrimaryKeyIDO(pk);
 }


 public Currency findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Currency) super.findByPrimaryKeyIDO(id);
 }


 public Currency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


public java.util.Collection getCurrenciesByAbbreviation(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((CurrencyBMPBean)entity).ejbHomeGetCurrenciesByAbbreviation(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public com.idega.block.trade.data.Currency getCurrencyByAbbreviation(java.lang.String p0)throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.block.trade.data.Currency theReturn = ((CurrencyBMPBean)entity).ejbHomeGetCurrencyByAbbreviation(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}