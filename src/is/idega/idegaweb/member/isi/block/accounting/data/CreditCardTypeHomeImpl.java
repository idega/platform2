package is.idega.idegaweb.member.isi.block.accounting.data;


public class CreditCardTypeHomeImpl extends com.idega.data.IDOFactory implements CreditCardTypeHome
{
 protected Class getEntityInterfaceClass(){
  return CreditCardType.class;
 }


 public CreditCardType create() throws javax.ejb.CreateException{
  return (CreditCardType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CreditCardTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CreditCardType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CreditCardType) super.findByPrimaryKeyIDO(pk);
 }



}