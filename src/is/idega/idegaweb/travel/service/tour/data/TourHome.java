package is.idega.idegaweb.travel.service.tour.data;


public interface TourHome extends com.idega.data.IDOHome
{
 public Tour create() throws javax.ejb.CreateException;
 public Tour createLegacy();
 public Tour findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Tour findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}