package se.idega.idegaweb.commune.accounting.invoice.data;


public class RegularInvoiceEntryHomeImpl extends com.idega.data.IDOFactory implements RegularInvoiceEntryHome
{
 protected Class getEntityInterfaceClass(){
  return RegularInvoiceEntry.class;
 }


 public RegularInvoiceEntry create() throws javax.ejb.CreateException{
  return (RegularInvoiceEntry) super.createIDO();
 }


public java.util.Collection findRegularInvoicesForPeriodeAbdCategory(java.sql.Date p0,java.lang.String p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindRegularInvoicesForPeriodeAbdCategory(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findRegularInvoicesForPeriodeUserAndCategory(java.sql.Date p0,java.sql.Date p1,int p2,java.lang.String p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularInvoiceEntryBMPBean)entity).ejbFindRegularInvoicesForPeriodeUserAndCategory(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public RegularInvoiceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RegularInvoiceEntry) super.findByPrimaryKeyIDO(pk);
 }



}