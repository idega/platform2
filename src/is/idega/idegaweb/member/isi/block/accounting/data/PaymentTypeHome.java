package is.idega.idegaweb.member.isi.block.accounting.data;


public interface PaymentTypeHome extends com.idega.data.IDOHome
{
 public PaymentType create() throws javax.ejb.CreateException;
 public PaymentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllPaymentTypes()throws javax.ejb.FinderException;

}