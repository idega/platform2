package se.idega.idegaweb.commune.accounting.business;


public class AccountingSessionHomeImpl extends com.idega.business.IBOHomeImpl implements AccountingSessionHome
{
 protected Class getBeanInterfaceClass(){
  return AccountingSession.class;
 }


 public AccountingSession create() throws javax.ejb.CreateException{
  return (AccountingSession) super.createIBO();
 }



}