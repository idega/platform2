package is.idega.idegaweb.golf.course.data;


public class TeeHomeImpl extends com.idega.data.IDOFactory implements TeeHome
{
 protected Class getEntityInterfaceClass(){
  return Tee.class;
 }


 public Tee create() throws javax.ejb.CreateException{
  return (Tee) super.createIDO();
 }


public java.util.Collection findAllByCourse(java.lang.Object p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TeeBMPBean)entity).ejbFindAllByCourse(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCourseAndDate(java.lang.Object p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TeeBMPBean)entity).ejbFindAllByCourseAndDate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Tee findTeeByCourse(java.lang.Object p0,java.lang.Object p1,java.lang.Object p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((TeeBMPBean)entity).ejbFindTeeByCourse(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public Tee findTeeByCourseAndDate(java.lang.Object p0,java.lang.Object p1,java.lang.Object p2,java.sql.Date p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((TeeBMPBean)entity).ejbFindTeeByCourseAndDate(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tee) super.findByPrimaryKeyIDO(pk);
 }



}