package is.idega.idegaweb.travel.service.hotel.data;


public interface RoomTypeHome extends com.idega.data.IDOHome
{
 public RoomType create() throws javax.ejb.CreateException;
 public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}