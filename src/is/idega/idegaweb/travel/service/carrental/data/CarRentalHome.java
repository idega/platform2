package is.idega.idegaweb.travel.service.carrental.data;


public interface CarRentalHome extends com.idega.data.IDOHome
{
 public CarRental create() throws javax.ejb.CreateException;
 public CarRental findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}