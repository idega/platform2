package is.idega.idegaweb.campus.data;


public interface BuildingApartmentHome extends com.idega.data.IDOHome
{
 public BuildingApartment create() throws javax.ejb.CreateException;
 public BuildingApartment createLegacy();
 public BuildingApartment findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BuildingApartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BuildingApartment findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}