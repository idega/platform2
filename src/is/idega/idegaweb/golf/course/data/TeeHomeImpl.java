package is.idega.idegaweb.golf.course.data;


public class TeeHomeImpl extends com.idega.data.IDOFactory implements TeeHome
{
 protected Class getEntityInterfaceClass(){
  return Tee.class;
 }


 public Tee create() throws javax.ejb.CreateException{
  return (Tee) super.createIDO();
 }


public Tee findAllByCourse(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((TeeBMPBean)entity).ejbFindAllByCourse(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findAllByCourse(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TeeBMPBean)entity).ejbFindAllByCourse(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tee) super.findByPrimaryKeyIDO(pk);
 }



}