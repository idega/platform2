package is.idega.idegaweb.travel.service.hotel.data;


public class HotelHomeImpl extends com.idega.data.IDOFactory implements HotelHome
{
 protected Class getEntityInterfaceClass(){
  return Hotel.class;
 }


 public Hotel create() throws javax.ejb.CreateException{
  return (Hotel) super.createIDO();
 }


 public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Hotel) super.findByPrimaryKeyIDO(pk);
 }



}