package is.idega.idegaweb.travel.service.tour.data;


public class TourBookingHomeImpl extends com.idega.data.IDOFactory implements TourBookingHome
{
 protected Class getEntityInterfaceClass(){
  return TourBooking.class;
 }

 public TourBooking create() throws javax.ejb.CreateException{
  return (TourBooking) super.idoCreate();
 }

 public TourBooking createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TourBooking findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TourBooking) super.idoFindByPrimaryKey(id);
 }

 public TourBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TourBooking) super.idoFindByPrimaryKey(pk);
 }

 public TourBooking findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}