package is.idega.idegaweb.campus.block.application.data;


public class CampusApplicationHomeImpl extends com.idega.data.IDOFactory implements CampusApplicationHome
{
 protected Class getEntityInterfaceClass(){
  return CampusApplication.class;
 }

 public CampusApplication create() throws javax.ejb.CreateException{
  return (CampusApplication) super.idoCreate();
 }

 public CampusApplication createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public CampusApplication findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (CampusApplication) super.idoFindByPrimaryKey(id);
 }

 public CampusApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CampusApplication) super.idoFindByPrimaryKey(pk);
 }

 public CampusApplication findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }

public java.util.Collection findAllByApplicationId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CampusApplicationBMPBean)entity).ejbFindAllByApplicationId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


}