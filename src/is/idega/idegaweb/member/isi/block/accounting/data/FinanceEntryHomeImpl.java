package is.idega.idegaweb.member.isi.block.accounting.data;


public class FinanceEntryHomeImpl extends com.idega.data.IDOFactory implements FinanceEntryHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceEntry.class;
 }


 public FinanceEntry create() throws javax.ejb.CreateException{
  return (FinanceEntry) super.createIDO();
 }


public java.util.Collection findAllByAssessmentRound(is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllByUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(com.idega.user.data.Group p0,java.sql.Date p1,java.sql.Date p2,java.util.Collection p3,java.util.Collection p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FinanceEntryBMPBean)entity).ejbFindAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceEntry) super.findByPrimaryKeyIDO(pk);
 }



}