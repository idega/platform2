package is.idega.idegaweb.golf.handicap.data;


public class StrokesHomeImpl extends com.idega.data.IDOFactory implements StrokesHome
{
 protected Class getEntityInterfaceClass(){
  return Strokes.class;
 }


 public Strokes create() throws javax.ejb.CreateException{
  return (Strokes) super.createIDO();
 }


public java.util.Collection findAllByScorecard(java.lang.Object p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((StrokesBMPBean)entity).ejbFindAllByScorecard(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Strokes findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Strokes) super.findByPrimaryKeyIDO(pk);
 }



}