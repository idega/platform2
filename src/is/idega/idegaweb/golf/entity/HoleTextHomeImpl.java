package is.idega.idegaweb.golf.entity;


public class HoleTextHomeImpl extends com.idega.data.IDOFactory implements HoleTextHome
{
 protected Class getEntityInterfaceClass(){
  return HoleText.class;
 }

 public HoleText create() throws javax.ejb.CreateException{
  return (HoleText) super.idoCreate();
 }

 public HoleText createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public HoleText findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (HoleText) super.idoFindByPrimaryKey(id);
 }

 public HoleText findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (HoleText) super.idoFindByPrimaryKey(pk);
 }

 public HoleText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }
 
 public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HoleTextBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


}