package com.idega.block.finance.data;


public class TariffHomeImpl extends com.idega.data.IDOFactory implements TariffHome
{
 protected Class getEntityInterfaceClass(){
  return Tariff.class;
 }


 public Tariff create() throws javax.ejb.CreateException{
  return (Tariff) super.createIDO();
 }


public java.util.Collection findAllByPrimaryKeyArray(java.lang.String[] p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffBMPBean)entity).ejbFindAllByPrimaryKeyArray(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByColumn(java.lang.String p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffBMPBean)entity).ejbFindAllByColumn(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByColumnOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffBMPBean)entity).ejbFindAllByColumnOrdered(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByColumnOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffBMPBean)entity).ejbFindAllByColumnOrdered(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByColumn(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TariffBMPBean)entity).ejbFindAllByColumn(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Tariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tariff) super.findByPrimaryKeyIDO(pk);
 }



}