package se.idega.idegaweb.commune.accounting.invoice.data;


public interface BatchRunHome extends com.idega.data.IDOHome
{
 public BatchRun create() throws javax.ejb.CreateException;
 public BatchRun findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllOrderByStart()throws javax.ejb.FinderException;

}