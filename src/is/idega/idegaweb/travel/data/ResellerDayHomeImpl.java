package is.idega.idegaweb.travel.data;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;


public class ResellerDayHomeImpl extends com.idega.data.IDOFactory implements ResellerDayHome
{
 protected Class getEntityInterfaceClass(){
  return ResellerDay.class;
 }


public ResellerDay create(is.idega.idegaweb.travel.data.ResellerDayPK p0)throws javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ResellerDayBMPBean)entity).ejbCreate(p0);
	//((ResellerDayBMPBean)entity).ejbPostCreate(p0);
	this.idoCheckInPooledEntity(entity);
	try{
		return this.findByPrimaryKey(pk);
	}
	catch(javax.ejb.FinderException fe){
		throw new com.idega.data.IDOCreateException(fe);
	}
	catch(Exception e){
		throw new com.idega.data.IDOCreateException(e);
	}
}

 public ResellerDay create() throws javax.ejb.CreateException{
  return (ResellerDay) super.createIDO();
 }


public ResellerDay findByPrimaryKey(is.idega.idegaweb.travel.data.ResellerDayPK p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ResellerDayBMPBean)entity).ejbFindByPrimaryKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ResellerDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ResellerDay) super.findByPrimaryKeyIDO(pk);
 }


public boolean getIfDay(int p0,int p1,int p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ResellerDayBMPBean)entity).ejbHomeGetIfDay(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}
/*
public int[] getResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int[] theReturn = ((ResellerDayBMPBean)entity).ejbHomeGetResellerDays(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}
*/
public Collection getDaysOfWeek(int resellerId, int serviceId) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection theReturn = ((ResellerDayBMPBean)entity).ejbHomeGetDaysOfWeek(resellerId, serviceId);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public void removeResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws RemoveException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	((ResellerDayBMPBean)entity).ejbHomeRemoveResellerDays(p0,p1);
	this.idoCheckInPooledEntity(entity);
}

public int[] getDaysOfWeekInt(int resellerId, int serviceId) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int[] theReturn = ((ResellerDayBMPBean)entity).ejbHomeGetDaysOfWeekInt(resellerId, serviceId);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}
	
}