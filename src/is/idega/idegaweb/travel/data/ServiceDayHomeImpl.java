package is.idega.idegaweb.travel.data;


public class ServiceDayHomeImpl extends com.idega.data.IDOFactory implements ServiceDayHome
{
 protected Class getEntityInterfaceClass(){
  return ServiceDay.class;
 }

 public ServiceDay create() throws javax.ejb.CreateException{
  return (ServiceDay) super.idoCreate();
 }

 public ServiceDay createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ServiceDay findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ServiceDay) super.idoFindByPrimaryKey(id);
 }

 public ServiceDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ServiceDay) super.idoFindByPrimaryKey(pk);
 }

 public ServiceDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}