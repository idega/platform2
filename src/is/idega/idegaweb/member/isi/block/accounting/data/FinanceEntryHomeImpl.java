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

 public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceEntry) super.findByPrimaryKeyIDO(pk);
 }



}