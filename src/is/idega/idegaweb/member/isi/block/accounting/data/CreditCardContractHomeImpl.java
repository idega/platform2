package is.idega.idegaweb.member.isi.block.accounting.data;


public class CreditCardContractHomeImpl extends com.idega.data.IDOFactory implements CreditCardContractHome
{
 protected Class getEntityInterfaceClass(){
  return CreditCardContract.class;
 }


 public CreditCardContract create() throws javax.ejb.CreateException{
  return (CreditCardContract) super.createIDO();
 }


public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CreditCardContractBMPBean)entity).ejbFindAllByClub(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CreditCardContract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CreditCardContract) super.findByPrimaryKeyIDO(pk);
 }



}