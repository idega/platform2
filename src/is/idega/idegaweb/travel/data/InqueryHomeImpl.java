package is.idega.idegaweb.travel.data;


public class InqueryHomeImpl extends com.idega.data.IDOFactory implements InqueryHome
{
 protected Class getEntityInterfaceClass(){
  return Inquery.class;
 }


 public Inquery create() throws javax.ejb.CreateException{
  return (Inquery) super.createIDO();
 }


public java.util.Collection findInqueries(int p0,com.idega.util.IWTimeStamp p1,int p2,boolean p3,com.idega.block.trade.stockroom.data.TravelAddress p4,java.lang.String p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InqueryBMPBean)entity).ejbFindInqueries(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findInqueries(int p0,com.idega.util.IWTimeStamp p1,int p2,boolean p3,java.lang.String p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InqueryBMPBean)entity).ejbFindInqueries(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Inquery findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Inquery) super.findByPrimaryKeyIDO(pk);
 }


public int getInqueredSeats(int p0,com.idega.util.IWTimeStamp p1,int p2,boolean p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InqueryBMPBean)entity).ejbHomeGetInqueredSeats(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}