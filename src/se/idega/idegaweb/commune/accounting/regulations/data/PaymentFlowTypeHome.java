package se.idega.idegaweb.commune.accounting.regulations.data;


public interface PaymentFlowTypeHome extends com.idega.data.IDOHome
{
 public PaymentFlowType create() throws javax.ejb.CreateException;
 public PaymentFlowType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}