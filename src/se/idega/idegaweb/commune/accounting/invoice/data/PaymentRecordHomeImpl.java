package se.idega.idegaweb.commune.accounting.invoice.data;


public class PaymentRecordHomeImpl extends com.idega.data.IDOFactory implements PaymentRecordHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentRecord.class;
 }


 public PaymentRecord create() throws javax.ejb.CreateException{
  return (PaymentRecord) super.createIDO();
 }


public java.util.Collection findByMonth(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentRecordBMPBean)entity).ejbFindByMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPaymentHeader(se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentRecordBMPBean)entity).ejbFindByPaymentHeader(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPaymentHeaders(java.util.Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentRecordBMPBean)entity).ejbFindByPaymentHeaders(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PaymentRecord findByPostingStrings(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PaymentRecordBMPBean)entity).ejbFindByPostingStrings(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public PaymentRecord findByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,com.idega.util.CalendarMonth p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PaymentRecordBMPBean)entity).ejbFindByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public PaymentRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentRecord) super.findByPrimaryKeyIDO(pk);
 }


public int getCountForMonthAndStatusLH(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentRecordBMPBean)entity).ejbHomeGetCountForMonthAndStatusLH(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountForMonthCategoryAndStatusLH(java.sql.Date p0,java.lang.String p1)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentRecordBMPBean)entity).ejbHomeGetCountForMonthCategoryAndStatusLH(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getPlacementCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentRecordBMPBean)entity).ejbHomeGetPlacementCountForSchoolCategoryAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTotAmountForProviderAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentRecordBMPBean)entity).ejbHomeGetTotAmountForProviderAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTotAmountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentRecordBMPBean)entity).ejbHomeGetTotAmountForSchoolCategoryAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}