package is.idega.idegaweb.campus.block.building.data;


public interface ApartmentTypePeriodsHome extends com.idega.data.IDOHome
{
 public ApartmentTypePeriods create() throws javax.ejb.CreateException;
 public ApartmentTypePeriods createLegacy();
 public ApartmentTypePeriods findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ApartmentTypePeriods findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ApartmentTypePeriods findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}