package se.idega.idegaweb.commune.business;


public interface CommuneCaseBusiness extends com.idega.business.IBOService
{
 public com.idega.block.process.business.CaseBusiness getCaseBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getAllCasesDefaultVisibleForUser(com.idega.user.data.User p0, int startingCase, int numberOfCases)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode[] getUserHiddenCaseCodes() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
