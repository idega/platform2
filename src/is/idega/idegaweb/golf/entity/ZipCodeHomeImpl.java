package is.idega.idegaweb.golf.entity;


public class ZipCodeHomeImpl extends com.idega.data.IDOFactory implements ZipCodeHome
{
 protected Class getEntityInterfaceClass(){
  return ZipCode.class;
 }

 public ZipCode create() throws javax.ejb.CreateException{
  return (ZipCode) super.idoCreate();
 }

 public ZipCode createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ZipCode findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ZipCode) super.idoFindByPrimaryKey(id);
 }

 public ZipCode findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ZipCode) super.idoFindByPrimaryKey(pk);
 }

 public ZipCode findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}