package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareQueueHome extends com.idega.data.IDOHome
{
 public ChildCareQueue create() throws javax.ejb.CreateException;
 public ChildCareQueue findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findQueueByChild(int p0)throws javax.ejb.FinderException;
 public ChildCareQueue findQueueByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException;
 public ChildCareQueue findQueueByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException;
 public ChildCareQueue findQueueByChildChoiceNumberAndQueueType(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findQueueByProviderAndDate(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public int getNumberInQueue(int p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public int getNumberOfNotExported(int p0)throws com.idega.data.IDOException;
 public int getTotalCount(java.lang.String[] p0,boolean p1)throws com.idega.data.IDOException;

}