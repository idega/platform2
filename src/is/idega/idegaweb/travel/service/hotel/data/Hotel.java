package is.idega.idegaweb.travel.service.hotel.data;

import javax.ejb.*;

public interface Hotel extends com.idega.data.IDOEntity
{
 public void setPrimaryKey(java.lang.Object p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setNumberOfUnits(int p0) throws java.rmi.RemoteException;
 public int getNumberOfUnits() throws java.rmi.RemoteException;
}
