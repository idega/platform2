package is.idega.idegaweb.travel.data;


public class GeneralBookingHomeImpl extends com.idega.data.IDOFactory implements GeneralBookingHome
{
 protected Class getEntityInterfaceClass(){
  return GeneralBooking.class;
 }

 public GeneralBooking create() throws javax.ejb.CreateException{
  return (GeneralBooking) super.idoCreate();
 }

 public GeneralBooking createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public GeneralBooking findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (GeneralBooking) super.idoFindByPrimaryKey(id);
 }

 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GeneralBooking) super.idoFindByPrimaryKey(pk);
 }

 public GeneralBooking findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}