package se.idega.idegaweb.commune.accounting.invoice.data;


public class PaymentHeaderHomeImpl extends com.idega.data.IDOFactory implements PaymentHeaderHome
{
 protected Class getEntityInterfaceClass(){
  return PaymentHeader.class;
 }


 public PaymentHeader create() throws javax.ejb.CreateException{
  return (PaymentHeader) super.createIDO();
 }


public java.util.Collection findBySchoolAndSchoolCategoryPKAndStatus(java.lang.Object p0,java.lang.Object p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolAndSchoolCategoryPKAndStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategoryAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolCategoryAndPeriodForPrivate(com.idega.block.school.data.SchoolCategory p0,java.sql.Date p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategoryAndPeriodForPrivate(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolCategoryAndSchoolAndPeriod(java.lang.String p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategoryAndSchoolAndPeriod(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PaymentHeader findBySchoolCategorySchoolPeriod(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1,java.sql.Date p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategorySchoolPeriod(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findBySchoolCategoryStatusInCommuneWithCommunalManagement(java.lang.String p0,char p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategoryStatusInCommuneWithCommunalManagement(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(java.lang.String p0,char p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStatusAndSchoolId(char p0,int p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PaymentHeaderBMPBean)entity).ejbFindByStatusAndSchoolId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PaymentHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PaymentHeader) super.findByPrimaryKeyIDO(pk);
 }


public int getPlacementCountForSchoolAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentHeaderBMPBean)entity).ejbHomeGetPlacementCountForSchoolAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getProviderCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((PaymentHeaderBMPBean)entity).ejbHomeGetProviderCountForSchoolCategoryAndPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}