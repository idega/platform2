package is.idega.idegaweb.campus.data;


public class SerieAccountHomeImpl extends com.idega.data.IDOFactory implements SerieAccountHome
{
 protected Class getEntityInterfaceClass(){
  return SerieAccount.class;
 }

 public SerieAccount create() throws javax.ejb.CreateException{
  return (SerieAccount) super.createIDO();
 }

 public SerieAccount createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public SerieAccount findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (SerieAccount) super.idoFindByPrimaryKey(id);
 }

 public SerieAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SerieAccount) super.findByPrimaryKeyIDO(pk);
 }

 public SerieAccount findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}