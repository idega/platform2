package is.idega.idegaweb.golf.course.data;


public class HoleHomeImpl extends com.idega.data.IDOFactory implements HoleHome
{
 protected Class getEntityInterfaceClass(){
  return Hole.class;
 }


 public Hole create() throws javax.ejb.CreateException{
  return (Hole) super.createIDO();
 }


public java.util.Collection findAllByCourse(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HoleBMPBean)entity).ejbFindAllByCourse(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCourse(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HoleBMPBean)entity).ejbFindAllByCourse(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Hole findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Hole) super.findByPrimaryKeyIDO(pk);
 }



}