package is.idega.idegaweb.campus.block.building.data;


public interface ApartmentTypeRentHome extends com.idega.data.IDOHome
{
 public ApartmentTypeRent create() throws javax.ejb.CreateException;
 public ApartmentTypeRent findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByType(int p0)throws javax.ejb.FinderException;
 public ApartmentTypeRent findByTypeAndValidity(int p0,java.sql.Date p1)throws javax.ejb.FinderException;

}