package se.idega.idegaweb.commune.accounting.posting.business;


public interface PostingBusiness extends com.idega.business.IBOService
{
 public void deletePostingParameter(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String extractField(java.lang.String p0,int p1,int p2,se.idega.idegaweb.commune.accounting.posting.data.PostingField p3) throws java.rmi.RemoteException;
 public java.util.Collection findAllPostingParameters() throws java.rmi.RemoteException;
 public java.lang.Object findPostingParameter(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.Object findPostingParameterByPeriode(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findPostingParametersByPeriode(java.sql.Date p0,java.sql.Date p1) throws java.rmi.RemoteException;
 public java.lang.String generateString(java.lang.String p0,java.lang.String p1,java.sql.Date p2)throws java.rmi.RemoteException,se.idega.idegaweb.commune.accounting.posting.business.PostingException, java.rmi.RemoteException;
 public java.util.Collection getAllPostingFieldsByDate(java.sql.Date p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.posting.data.PostingParameters getPostingParameter(java.sql.Date p0,int p1,int p2,int p3,int p4)throws se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.accounting.posting.data.PostingParameters getPostingParameter(java.sql.Date p0,int p1,int p2,int p3,int p4,int p5,int p6)throws se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException, java.rmi.RemoteException;
 public java.lang.String pad(java.lang.String p0,se.idega.idegaweb.commune.accounting.posting.data.PostingField p1) throws java.rmi.RemoteException;
 public void savePostingParameter(java.lang.String p0,java.sql.Date p1,java.sql.Date p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.String p11)throws se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String trim(java.lang.String p0,se.idega.idegaweb.commune.accounting.posting.data.PostingField p1) throws java.rmi.RemoteException;
 public void validateString(java.lang.String p0,java.sql.Date p1)throws se.idega.idegaweb.commune.accounting.posting.business.MissingMandatoryFieldException,se.idega.idegaweb.commune.accounting.posting.business.PostingException, java.rmi.RemoteException;
}
