package is.idega.idegaweb.travel.service.tour.data;


public class TourHomeImpl extends com.idega.data.IDOFactory implements TourHome
{
 protected Class getEntityInterfaceClass(){
  return Tour.class;
 }


 public Tour create() throws javax.ejb.CreateException{
  return (Tour) super.createIDO();
 }


public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4, String p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((TourBMPBean)entity).ejbFind(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tour) super.findByPrimaryKeyIDO(pk);
 }



}