package se.idega.idegaweb.commune.accounting.invoice.data;


public class RegularInvoiceEntryHomeImpl extends com.idega.data.IDOFactory implements RegularInvoiceEntryHome
{
 protected Class getEntityInterfaceClass(){
  return RegularInvoiceEntry.class;
 }


 public RegularInvoiceEntry create() throws javax.ejb.CreateException{
  return (RegularInvoiceEntry) super.createIDO();
 }


public java.util.Collection findRegularInvoicesForPeriodAndCategoryExceptType(java.sql.Date p0,java.lang.String p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindRegularInvoicesForPeriodAndCategoryExceptType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findRegularInvoicesForPeriodAndChildAndCategory(java.sql.Date p0,java.sql.Date p1,int p2,java.lang.String p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindRegularInvoicesForPeriodAndChildAndCategory(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findRegularInvoicesForPeriodAndChildAndCategoryAndRegSpecType(java.sql.Date p0,int p1,java.lang.String p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindRegularInvoicesForPeriodAndChildAndCategoryAndRegSpecType(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public RegularInvoiceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RegularInvoiceEntry) super.findByPrimaryKeyIDO(pk);
 }



}