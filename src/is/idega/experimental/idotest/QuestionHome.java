package is.idega.experimental.idotest;


public interface QuestionHome extends com.idega.data.IDOHome
{
 public Question create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Question findByPrimaryKey(int id) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public Question findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllQuestionsNotContaining(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllQuestionsContaining(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getNumberOfQuestions()throws javax.ejb.EJBException, java.rmi.RemoteException;

}