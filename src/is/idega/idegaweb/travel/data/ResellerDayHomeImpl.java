package is.idega.idegaweb.travel.data;


public class ResellerDayHomeImpl extends com.idega.data.IDOFactory implements ResellerDayHome
{
 protected Class getEntityInterfaceClass(){
  return ResellerDay.class;
 }


 public ResellerDay create() throws javax.ejb.CreateException{
  return (ResellerDay) super.createIDO();
 }


public java.util.Collection findResellerDays(com.idega.block.trade.stockroom.data.Reseller p0,is.idega.idegaweb.travel.data.Service p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ResellerDayBMPBean)entity).ejbFindResellerDays(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
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


}