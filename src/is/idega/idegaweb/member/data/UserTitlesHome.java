package is.idega.idegaweb.member.data;


public interface UserTitlesHome extends com.idega.data.IDOHome
{
 public UserTitles create() throws javax.ejb.CreateException;
 public UserTitles findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBoardTitle()throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllTitles()throws javax.ejb.FinderException;

}