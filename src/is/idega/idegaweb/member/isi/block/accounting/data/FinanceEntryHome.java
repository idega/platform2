package is.idega.idegaweb.member.isi.block.accounting.data;


public interface FinanceEntryHome extends com.idega.data.IDOHome
{
 public FinanceEntry create() throws javax.ejb.CreateException;
 public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}