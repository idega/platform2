package is.idega.idegaweb.travel.service.carrental.data;


public interface CarRentalHome extends com.idega.data.IDOHome
{
 public CarRental create() throws javax.ejb.CreateException;
 public CarRental findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3)throws javax.ejb.FinderException;

}