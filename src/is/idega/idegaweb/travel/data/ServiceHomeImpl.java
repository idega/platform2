package is.idega.idegaweb.travel.data;


public class ServiceHomeImpl extends com.idega.data.IDOFactory implements ServiceHome
{
 protected Class getEntityInterfaceClass(){
  return Service.class;
 }


 public Service create() throws javax.ejb.CreateException{
  return (Service) super.createIDO();
 }


 public Service createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Service findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Service) super.findByPrimaryKeyIDO(pk);
 }


 public Service findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Service) super.findByPrimaryKeyIDO(id);
 }


 public Service findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}