package se.idega.idegaweb.commune.accounting.invoice.data;


public class RegularPaymentEntryHomeImpl extends com.idega.data.IDOFactory implements RegularPaymentEntryHome
{
 protected Class getEntityInterfaceClass(){
  return RegularPaymentEntry.class;
 }


 public RegularPaymentEntry create() throws javax.ejb.CreateException{
  return (RegularPaymentEntry) super.createIDO();
 }


public java.util.Collection findByPeriodeAndProvider(java.sql.Date p0,java.sql.Date p1,com.idega.block.school.data.School p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindByPeriodeAndProvider(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPeriodeAndUser(java.sql.Date p0,java.sql.Date p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindByPeriodeAndUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findOngoingByUserAndProviderAndDate(com.idega.user.data.User p0,com.idega.block.school.data.School p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindOngoingByUserAndProviderAndDate(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findRegularPaymentForPeriodeCategory(java.sql.Date p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindRegularPaymentForPeriodeCategory(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findRegularPaymentForPeriodeCategoryExcludingRegSpecType(java.sql.Date p0,java.lang.String p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegularPaymentEntryBMPBean)entity).ejbFindRegularPaymentForPeriodeCategoryExcludingRegSpecType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public RegularPaymentEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RegularPaymentEntry) super.findByPrimaryKeyIDO(pk);
 }



}