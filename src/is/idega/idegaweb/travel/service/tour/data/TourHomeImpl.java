package is.idega.idegaweb.travel.service.tour.data;


public class TourHomeImpl extends com.idega.data.IDOFactory implements TourHome
{
 protected Class getEntityInterfaceClass(){
  return Tour.class;
 }

 public Tour create() throws javax.ejb.CreateException{
  return (Tour) super.idoCreate();
 }

 public Tour createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Tour findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Tour) super.idoFindByPrimaryKey(id);
 }

 public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tour) super.idoFindByPrimaryKey(pk);
 }

 public Tour findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}