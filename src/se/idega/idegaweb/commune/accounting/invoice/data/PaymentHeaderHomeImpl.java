package se.idega.idegaweb.commune.accounting.invoice.data;


public class PaymentHeaderHomeImpl extends com.idega.data.IDOFactory implements PaymentHeaderHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentHeader.class;
 }


 public PaymentHeader create() throws javax.ejb.CreateException{
  return (PaymentHeader) super.createIDO();
 }


public java.util.Collection findBySchoolCategoryAndPeriodForPrivate(com.idega.block.school.data.SchoolCategory p0,java.sql.Date p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategoryAndPeriodForPrivate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PaymentHeader findBySchoolCategorySchoolPeriod(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategorySchoolPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public PaymentHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentHeader) super.findByPrimaryKeyIDO(pk);
 }



}