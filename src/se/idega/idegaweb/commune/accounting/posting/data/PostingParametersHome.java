package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParametersHome extends com.idega.data.IDOHome
{
 public PostingParameters create() throws javax.ejb.CreateException;
 public PostingParameters findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllPostingParameters()throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(int p0,int p1,int p2,int p3)throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findPostingParametersByPeriode(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;

}