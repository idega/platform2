package is.idega.idegaweb.campus.data;


public class ContractAccountsHomeImpl extends com.idega.data.IDOFactory implements ContractAccountsHome
{
 protected Class getEntityInterfaceClass(){
  return ContractAccounts.class;
 }


 public ContractAccounts create() throws javax.ejb.CreateException{
  return (ContractAccounts) super.createIDO();
 }


public java.util.Collection findByPeriodOverLap(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ContractAccountsBMPBean)entity).ejbFindByPeriodOverLap(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ContractAccounts findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ContractAccounts) super.findByPrimaryKeyIDO(pk);
 }



}