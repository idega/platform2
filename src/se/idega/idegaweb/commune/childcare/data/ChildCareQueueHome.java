package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareQueueHome extends com.idega.data.IDOHome
{
 public ChildCareQueue create() throws javax.ejb.CreateException;
 public ChildCareQueue findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ChildCareQueue findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findQueueByChild(int p0)throws javax.ejb.FinderException;
 public ChildCareQueue findQueueByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException;

}