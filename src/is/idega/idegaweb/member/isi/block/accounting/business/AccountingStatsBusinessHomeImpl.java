package is.idega.idegaweb.member.isi.block.accounting.business;


public class AccountingStatsBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements AccountingStatsBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return AccountingStatsBusiness.class;
 }


 public AccountingStatsBusiness create() throws javax.ejb.CreateException{
  return (AccountingStatsBusiness) super.createIBO();
 }



}