package se.idega.idegaweb.commune.accounting.userinfo.data;


public interface InvoiceReceiverHome extends com.idega.data.IDOHome
{
 public InvoiceReceiver create() throws javax.ejb.CreateException;
 public InvoiceReceiver findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public InvoiceReceiver findByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public InvoiceReceiver findByUser(int p0)throws javax.ejb.FinderException;

}