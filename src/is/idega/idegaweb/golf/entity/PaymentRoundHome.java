package is.idega.idegaweb.golf.entity;


public interface PaymentRoundHome extends com.idega.data.IDOHome
{
 public PaymentRound create() throws javax.ejb.CreateException;
 public PaymentRound createLegacy();
 public PaymentRound findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PaymentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PaymentRound findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}