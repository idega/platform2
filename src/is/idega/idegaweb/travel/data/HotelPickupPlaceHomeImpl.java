package is.idega.idegaweb.travel.data;


public class HotelPickupPlaceHomeImpl extends com.idega.data.IDOFactory implements HotelPickupPlaceHome
{
 protected Class getEntityInterfaceClass(){
  return HotelPickupPlace.class;
 }

 public HotelPickupPlace create() throws javax.ejb.CreateException{
  return (HotelPickupPlace) super.idoCreate();
 }

 public HotelPickupPlace createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public HotelPickupPlace findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (HotelPickupPlace) super.idoFindByPrimaryKey(id);
 }

 public HotelPickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (HotelPickupPlace) super.idoFindByPrimaryKey(pk);
 }

 public HotelPickupPlace findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}