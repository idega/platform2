package is.idega.idegaweb.golf.entity;


public class ZipCodeHomeImpl extends com.idega.data.IDOFactory implements ZipCodeHome
{
 protected Class getEntityInterfaceClass(){
  return ZipCode.class;
 }


 public ZipCode create() throws javax.ejb.CreateException{
  return (ZipCode) super.createIDO();
 }


 public ZipCode createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public ZipCode findByCode(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((ZipCodeBMPBean)entity).ejbFindByCode(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public ZipCode findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ZipCode) super.findByPrimaryKeyIDO(pk);
 }


 public ZipCode findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ZipCode) super.findByPrimaryKeyIDO(id);
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