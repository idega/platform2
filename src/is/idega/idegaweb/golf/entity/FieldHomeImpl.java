package is.idega.idegaweb.golf.entity;


public class FieldHomeImpl extends com.idega.data.IDOFactory implements FieldHome
{
 protected Class getEntityInterfaceClass(){
  return Field.class;
 }


 public Field create() throws javax.ejb.CreateException{
  return (Field) super.createIDO();
 }


 public Field createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findByUnion(is.idega.idegaweb.golf.entity.Union p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FieldBMPBean)entity).ejbFindByUnion(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Field findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Field) super.findByPrimaryKeyIDO(pk);
 }


 public Field findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Field) super.findByPrimaryKeyIDO(id);
 }


 public Field findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}