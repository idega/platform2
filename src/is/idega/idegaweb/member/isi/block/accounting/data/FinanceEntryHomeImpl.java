package is.idega.idegaweb.member.isi.block.accounting.data;


public class FinanceEntryHomeImpl extends com.idega.data.IDOFactory implements FinanceEntryHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceEntry.class;
 }


 public FinanceEntry create() throws javax.ejb.CreateException{
  return (FinanceEntry) super.createIDO();
 }


public java.util.Collection findAllAssessmentByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllAssessmentByUser(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllAssessmentByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllAssessmentByUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByAssessmentRound(is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllByUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(com.idega.user.data.Group p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3,java.util.Collection p4,java.util.Collection p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllFinanceEntriesByEntryDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(com.idega.user.data.Group p0,java.lang.String p1,java.sql.Date p2,java.util.Collection p3,java.util.Collection p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllFinanceEntriesByEntryDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(com.idega.user.data.Group p0,java.lang.String[] p1,java.util.Collection p2,java.util.Collection p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllOpenAssessmentByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllOpenAssessmentByUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllPaymentsByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllPaymentsByUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceEntry) super.findByPrimaryKeyIDO(pk);
 }



}