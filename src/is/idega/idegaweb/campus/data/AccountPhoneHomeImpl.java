package is.idega.idegaweb.campus.data;


public class AccountPhoneHomeImpl extends com.idega.data.IDOFactory implements AccountPhoneHome
{
 protected Class getEntityInterfaceClass(){
  return AccountPhone.class;
 }


 public AccountPhone create() throws javax.ejb.CreateException{
  return (AccountPhone) super.createIDO();
 }


 public AccountPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountPhone) super.findByPrimaryKeyIDO(pk);
 }



}