package is.idega.idegaweb.travel.business;


public interface Assigner extends com.idega.business.IBOService
{
 public is.idega.idegaweb.travel.data.Contract[] getContracts(com.idega.block.trade.stockroom.data.Product p0) throws java.rmi.RemoteException;
 public int getNumberOfAssignedSeats(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1) throws java.rmi.RemoteException;
 public int getNumberOfAssignedSeats(int p0,com.idega.util.IWTimestamp p1) throws java.rmi.RemoteException;
 public int getNumberOfAssignedSeats(int p0,int p1,com.idega.util.IWTimestamp p2) throws java.rmi.RemoteException;
 public int getNumberOfAssignedSeatsByContract(int p0,int p1,com.idega.util.IWTimestamp p2,is.idega.idegaweb.travel.data.Contract p3,java.sql.Connection p4) throws java.rmi.RemoteException;
}
