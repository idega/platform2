package se.idega.idegaweb.commune.accounting.posting.business;


public interface PostingBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findAllPostingParameters() throws java.rmi.RemoteException;
 public java.lang.Object findPostingParameter(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findPostingParametersByPeriode(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String generateString(java.lang.String p0,java.lang.String p1,java.sql.Date p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
