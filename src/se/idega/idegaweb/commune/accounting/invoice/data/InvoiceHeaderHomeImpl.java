package se.idega.idegaweb.commune.accounting.invoice.data;


public class InvoiceHeaderHomeImpl extends com.idega.data.IDOFactory implements InvoiceHeaderHome
{
 protected Class getEntityInterfaceClass(){
  return InvoiceHeader.class;
 }


 public InvoiceHeader create() throws javax.ejb.CreateException{
  return (InvoiceHeader) super.createIDO();
 }


public InvoiceHeader findByCustodian(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((InvoiceHeaderBMPBean)entity).ejbFindByCustodian(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByMonth(java.sql.Date p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByMonthAndSchoolCategory(java.sql.Date p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByMonthAndSchoolCategory(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

    public java.util.Collection findByCustodianOrChild (com.idega.user.data.User user, java.util.Date fromPeriod, java.util.Date toPeriod) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByCustodianOrChild (user, fromPeriod, toPeriod);
        idoCheckInPooledEntity(entity);
        return getEntityCollectionForPrimaryKeys(ids);
    }

 public InvoiceHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceHeader) super.findByPrimaryKeyIDO(pk);
 }



}
