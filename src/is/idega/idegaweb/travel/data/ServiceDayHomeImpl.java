package is.idega.idegaweb.travel.data;


public class ServiceDayHomeImpl extends com.idega.data.IDOFactory implements ServiceDayHome
{
 protected Class getEntityInterfaceClass(){
  return ServiceDay.class;
 }


 public ServiceDay create() throws javax.ejb.CreateException{
  return (ServiceDay) super.createIDO();
 }


 public ServiceDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ServiceDay) super.findByPrimaryKeyIDO(pk);
 }


public boolean deleteService(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ServiceDayBMPBean)entity).ejbHomeDeleteService(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public boolean getIfDay(int p0,int p1){
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ServiceDayBMPBean)entity).ejbHomeGetIfDay(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public boolean setServiceWithNoDays(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	boolean theReturn = ((ServiceDayBMPBean)entity).ejbHomeSetServiceWithNoDays(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}