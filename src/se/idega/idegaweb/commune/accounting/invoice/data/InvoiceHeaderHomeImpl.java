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

public InvoiceHeader findByCustodianAndMonth(com.idega.user.data.User p0,com.idega.util.CalendarMonth p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((InvoiceHeaderBMPBean)entity).ejbFindByCustodianAndMonth(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public InvoiceHeader findByCustodianID(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((InvoiceHeaderBMPBean)entity).ejbFindByCustodianID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByCustodianOrChild(java.lang.String p0,com.idega.user.data.User p1,java.util.Collection p2,java.util.Date p3,java.util.Date p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByCustodianOrChild(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByMonth(com.idega.util.CalendarMonth p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByMonthAndSchoolCategory(com.idega.util.CalendarMonth p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByMonthAndSchoolCategory(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndCategory(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((InvoiceHeaderBMPBean)entity).ejbFindByStatusAndCategory(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public InvoiceHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (InvoiceHeader) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfChildrenForCurrentMonth()throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetNumberOfChildrenForCurrentMonth();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfChildrenForMonth(com.idega.util.CalendarMonth p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetNumberOfChildrenForMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfInvoicesForCurrentMonth()throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetNumberOfInvoicesForCurrentMonth();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfInvoicesForMonth(com.idega.util.CalendarMonth p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetNumberOfInvoicesForMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfInvoicesForSchoolCategoryAndMonth(java.lang.String p0,com.idega.util.CalendarMonth p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetNumberOfInvoicesForSchoolCategoryAndMonth(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTotalInvoiceRecordAmountForCurrentMonth()throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetTotalInvoiceRecordAmountForCurrentMonth();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTotalInvoiceRecordAmountForMonth(com.idega.util.CalendarMonth p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((InvoiceHeaderBMPBean)entity).ejbHomeGetTotalInvoiceRecordAmountForMonth(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}