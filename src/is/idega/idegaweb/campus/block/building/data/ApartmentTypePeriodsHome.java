package is.idega.idegaweb.campus.block.building.data;


public interface ApartmentTypePeriodsHome extends com.idega.data.IDOHome
{
 public ApartmentTypePeriods create() throws javax.ejb.CreateException;
 public ApartmentTypePeriods findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByApartmentType(java.lang.Integer p0)throws javax.ejb.FinderException;

}