package is.idega.idegaweb.travel.service.hotel.data;


public interface HotelTypeHome extends com.idega.data.IDOHome
{
 public HotelType create() throws javax.ejb.CreateException;
 public HotelType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public HotelType findByLocalizationKey(java.lang.String p0)throws javax.ejb.FinderException;

}