package is.idega.idegaweb.campus.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.sql.Date;

public interface ApartmentTypeRentHome extends IDOHome {
	public ApartmentTypeRent create() throws CreateException;

	public ApartmentTypeRent findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByType(int apartmentTypeId) throws FinderException;

	public ApartmentTypeRent findByTypeAndValidity(int aprtTypeId,
			Date dateToCheck) throws FinderException;
}