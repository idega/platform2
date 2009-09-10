package com.idega.block.trade.stockroom.data;

import java.sql.Date;


public class DayInfoHomeImpl extends com.idega.data.IDOFactory implements DayInfoHome
{
 protected Class getEntityInterfaceClass(){
  return DayInfo.class;
 }


 public DayInfo create() throws javax.ejb.CreateException{
  return (DayInfo) super.createIDO();
 }


 public DayInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DayInfo) super.findByPrimaryKeyIDO(pk);
 }
 
 public DayInfo findBySupplyPoolIdAndDate(int supplyPoolId, Date date) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((DayInfoBMPBean)entity).ejbFindBySupplyPoolIdAndDate(supplyPoolId,date);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
 }



}