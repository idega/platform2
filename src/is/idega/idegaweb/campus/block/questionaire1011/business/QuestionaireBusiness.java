package is.idega.idegaweb.campus.block.questionaire1011.business;


public interface QuestionaireBusiness extends com.idega.business.IBOService
{
 public boolean hasUserAlreadyAnswered(com.idega.core.user.data.User p0) throws java.rmi.RemoteException;
 public boolean insertAnswers(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,com.idega.core.user.data.User p9) throws java.rmi.RemoteException;
}
