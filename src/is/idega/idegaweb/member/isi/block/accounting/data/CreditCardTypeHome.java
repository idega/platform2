package is.idega.idegaweb.member.isi.block.accounting.data;


public interface CreditCardTypeHome extends com.idega.data.IDOHome
{
 public CreditCardType create() throws javax.ejb.CreateException;
 public CreditCardType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}