package is.idega.idegaweb.travel.data;

import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;


public class GeneralBookingHomeImpl extends com.idega.data.IDOFactory implements GeneralBookingHome
{
 protected Class getEntityInterfaceClass(){
  return GeneralBooking.class;
 }


 public GeneralBooking create() throws javax.ejb.CreateException{
  return (GeneralBooking) super.createIDO();
 }


public java.util.Collection findAllByCode(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindAllByCode(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByReferenceNumber(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindAllByReferenceNumber(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,java.lang.String p8,boolean p9)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3,p4,p5,p6,p7,p8,p9);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,boolean p8)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,java.lang.String p8)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,com.idega.block.trade.stockroom.data.TravelAddress p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.lang.String p3,com.idega.block.trade.stockroom.data.TravelAddress p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookings(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.block.trade.stockroom.data.TravelAddress p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookings(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBookingsByDateOfBooking(int[] p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int[] p3,java.lang.String p4,java.lang.String p5,com.idega.block.trade.stockroom.data.TravelAddress p6,java.lang.String p7,boolean p8)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((GeneralBookingBMPBean)entity).ejbFindBookingsByDateOfBooking(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GeneralBooking) super.findByPrimaryKeyIDO(pk);
 }


public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5,boolean p6,boolean p7,java.lang.String p8){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCount(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3,boolean p4,java.lang.String p5){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCount(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5,boolean p6){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCount(p0,p1,p2,p3,p4,p5,p6);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getBookingsTotalCountByDateOfBooking(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3,int[] p4,java.util.Collection p5){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetBookingsTotalCountByDateOfBooking(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection getMultibleBookings(is.idega.idegaweb.travel.data.GeneralBooking p0)throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetMultibleBookings(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfBookings(int[] p0,int p1,com.idega.util.IWTimestamp p2,java.util.Collection p3){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetNumberOfBookings(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfBookings(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2,int p3){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((GeneralBookingBMPBean)entity).ejbHomeGetNumberOfBookings(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public GeneralBooking findByAuthorizationNumber(String number, IWTimestamp stamp) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((GeneralBookingBMPBean)entity).ejbFindByAuthorizationNumber(number, stamp);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}
}