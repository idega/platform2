package is.idega.idegaweb.member.isi.block.accounting.data;


public class UserCreditCardHomeImpl extends com.idega.data.IDOFactory implements UserCreditCardHome
{
 protected Class getEntityInterfaceClass(){
  return UserCreditCard.class;
 }


 public UserCreditCard create() throws javax.ejb.CreateException{
  return (UserCreditCard) super.createIDO();
 }


 public UserCreditCard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserCreditCard) super.findByPrimaryKeyIDO(pk);
 }



}