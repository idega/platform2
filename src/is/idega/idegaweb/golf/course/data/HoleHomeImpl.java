package is.idega.idegaweb.golf.course.data;


public class HoleHomeImpl extends com.idega.data.IDOFactory implements HoleHome
{
 protected Class getEntityInterfaceClass(){
  return Hole.class;
 }


 public Hole create() throws javax.ejb.CreateException{
  return (Hole) super.createIDO();
 }


public java.util.Collection findAllByCourse(java.lang.Object p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HoleBMPBean)entity).ejbFindAllByCourse(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCourseAndTeeColor(java.lang.Object p0,java.lang.Object p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HoleBMPBean)entity).ejbFindAllByCourseAndTeeColor(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Hole findHoleByCourseAndTeeColorAndNumber(java.lang.Object p0,java.lang.Object p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((HoleBMPBean)entity).ejbFindHoleByCourseAndTeeColorAndNumber(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public Hole findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Hole) super.findByPrimaryKeyIDO(pk);
 }


public int getCoursePar(java.lang.Object p0,java.lang.Object p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((HoleBMPBean)entity).ejbHomeGetCoursePar(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}