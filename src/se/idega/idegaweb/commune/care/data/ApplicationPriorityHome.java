package se.idega.idegaweb.commune.care.data;


public interface ApplicationPriorityHome extends com.idega.data.IDOHome
{
 public ApplicationPriority create() throws javax.ejb.CreateException;
 public ApplicationPriority findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByPeriodAndProvider(java.sql.Date p0,java.sql.Date p1,int p2)throws javax.ejb.FinderException;

}