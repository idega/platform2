package is.idega.idegaweb.member.isi.block.accounting.data;


public class UserCreditCardHomeImpl extends com.idega.data.IDOFactory implements UserCreditCardHome
{
 protected Class getEntityInterfaceClass(){
  return UserCreditCard.class;
 }


 public UserCreditCard create() throws javax.ejb.CreateException{
  return (UserCreditCard) super.createIDO();
 }


public java.util.Collection findAllByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserCreditCardBMPBean)entity).ejbFindAllByUser(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public UserCreditCard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserCreditCard) super.findByPrimaryKeyIDO(pk);
 }



}