package se.idega.idegaweb.commune.accounting.invoice.data;


public class InvoiceRecordHomeImpl extends com.idega.data.IDOFactory implements InvoiceRecordHome
{
 protected Class getEntityInterfaceClass(){
  return InvoiceRecord.class;
 }


 public InvoiceRecord create() throws javax.ejb.CreateException{
  return (InvoiceRecord) super.createIDO();
 }


public java.util.Collection findByInvoiceHeader(se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceRecordBMPBean)entity).ejbFindByInvoiceHeader(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPaymentRecord(se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceRecordBMPBean)entity).ejbFindByPaymentRecord(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public InvoiceRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceRecord) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfHandledChildrenForSchoolTypesAndMonth(java.util.Collection p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceRecordBMPBean)entity).ejbHomeGetNumberOfHandledChildrenForSchoolTypesAndMonth(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getPlacementCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceRecordBMPBean)entity).ejbHomeGetPlacementCountForSchoolCategoryAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public double getTotalAmountForSchoolTypesAndMonth(java.util.Collection p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	double theReturn = ((InvoiceRecordBMPBean)entity).ejbHomeGetTotalAmountForSchoolTypesAndMonth(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}