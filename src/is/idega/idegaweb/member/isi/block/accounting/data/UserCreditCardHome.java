package is.idega.idegaweb.member.isi.block.accounting.data;


public interface UserCreditCardHome extends com.idega.data.IDOHome
{
 public UserCreditCard create() throws javax.ejb.CreateException;
 public UserCreditCard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}