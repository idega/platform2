package is.idega.idegaweb.travel.data;


public class BookingEntryHomeImpl extends com.idega.data.IDOFactory implements BookingEntryHome
{
 protected Class getEntityInterfaceClass(){
  return BookingEntry.class;
 }

 public BookingEntry create() throws javax.ejb.CreateException{
  return (BookingEntry) super.idoCreate();
 }

 public BookingEntry createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public BookingEntry findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (BookingEntry) super.idoFindByPrimaryKey(id);
 }

 public BookingEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BookingEntry) super.idoFindByPrimaryKey(pk);
 }

 public BookingEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}