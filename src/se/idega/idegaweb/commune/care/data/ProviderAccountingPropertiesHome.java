package se.idega.idegaweb.commune.care.data;


public interface ProviderAccountingPropertiesHome extends com.idega.data.IDOHome
{
 public ProviderAccountingProperties create() throws javax.ejb.CreateException;
 public ProviderAccountingProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
	public java.util.Collection findAllIdsByPaymentByInvoice (boolean p0)throws javax.ejb.FinderException;
}
