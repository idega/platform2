package is.idega.idegaweb.member.isi.block.accounting.data;


public interface UserCreditCardHome extends com.idega.data.IDOHome
{
 public UserCreditCard create() throws javax.ejb.CreateException;
 public UserCreditCard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException;

}