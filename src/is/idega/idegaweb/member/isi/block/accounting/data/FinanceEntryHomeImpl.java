package is.idega.idegaweb.member.isi.block.accounting.data;


public class FinanceEntryHomeImpl extends com.idega.data.IDOFactory implements FinanceEntryHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceEntry.class;
 }


 public FinanceEntry create() throws javax.ejb.CreateException{
  return (FinanceEntry) super.createIDO();
 }


 public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceEntry) super.findByPrimaryKeyIDO(pk);
 }



}