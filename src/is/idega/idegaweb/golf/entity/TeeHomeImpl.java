package is.idega.idegaweb.golf.entity;


public class TeeHomeImpl extends com.idega.data.IDOFactory implements TeeHome
{
 protected Class getEntityInterfaceClass(){
  return Tee.class;
 }


 public Tee create() throws javax.ejb.CreateException{
  return (Tee) super.createIDO();
 }


 public Tee createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findByFieldAndHoleNumber(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TeeBMPBean)entity).ejbFindByFieldAndHoleNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Tee findByFieldAndTeeColorAndHoleNumber(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((TeeBMPBean)entity).ejbFindByFieldAndTeeColorAndHoleNumber(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tee) super.findByPrimaryKeyIDO(pk);
 }


 public Tee findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Tee) super.findByPrimaryKeyIDO(id);
 }


 public Tee findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}