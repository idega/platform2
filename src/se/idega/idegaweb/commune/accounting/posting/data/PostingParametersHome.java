package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParametersHome extends com.idega.data.IDOHome
{
 public PostingParameters create() throws javax.ejb.CreateException;
 public PostingParameters findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllPostingParameters()throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(java.sql.Date p0,int p1,int p2,java.lang.String p3,int p4,int p5,int p6)throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(int p0)throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(int p0,int p1,int p2,int p3)throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(java.sql.Date p0,int p1,int p2,java.lang.String p3,int p4,int p5,int p6,boolean p7)throws javax.ejb.FinderException;
 public PostingParameters findPostingParameter(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,java.lang.String p3,int p4,int p5,java.lang.String p6,int p7,int p8,int p9,int p10)throws javax.ejb.FinderException;
 public java.util.Collection findPostingParametersByDate(java.sql.Date p0)throws javax.ejb.FinderException;
 public java.util.Collection findPostingParametersByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findPostingParametersByPeriodAndOperationalID(java.sql.Date p0,java.sql.Date p1,String opID)throws javax.ejb.FinderException;

}