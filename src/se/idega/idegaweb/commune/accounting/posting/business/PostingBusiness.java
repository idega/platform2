package se.idega.idegaweb.commune.accounting.posting.business;


public interface PostingBusiness extends com.idega.business.IBOService
{
 public java.util.Collection findAllPostingParameters() throws java.rmi.RemoteException;
 public java.lang.Object findPostingParameter(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findPostingParametersByPeriode(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.lang.String generateString(java.lang.String p0,java.lang.String p1,java.sql.Date p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void savePostingParameter(int p0,java.sql.Date p1,java.sql.Date p2,java.sql.Date p3,java.lang.String p4,int p5,int p6,int p7,int p8,java.lang.String p9,java.lang.String p10,java.lang.String p11,java.lang.String p12,java.lang.String p13,java.lang.String p14,java.lang.String p15,java.lang.String p16,java.lang.String p17,java.lang.String p18,java.lang.String p19,java.lang.String p20,java.lang.String p21,java.lang.String p22,java.lang.String p23,java.lang.String p24)throws se.idega.idegaweb.commune.accounting.posting.business.PostingParamException,java.rmi.RemoteException, java.rmi.RemoteException;
}
