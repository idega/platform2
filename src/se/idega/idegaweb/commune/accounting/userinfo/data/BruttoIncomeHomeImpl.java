package se.idega.idegaweb.commune.accounting.userinfo.data;


public class BruttoIncomeHomeImpl extends com.idega.data.IDOFactory implements BruttoIncomeHome
{
 protected Class getEntityInterfaceClass(){
  return BruttoIncome.class;
 }


 public BruttoIncome create() throws javax.ejb.CreateException{
  return (BruttoIncome) super.createIDO();
 }


public java.util.Collection findByUser(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BruttoIncomeBMPBean)entity).ejbFindByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public BruttoIncome findLatestByUser(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((BruttoIncomeBMPBean)entity).ejbFindLatestByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public BruttoIncome findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BruttoIncome) super.findByPrimaryKeyIDO(pk);
 }



}