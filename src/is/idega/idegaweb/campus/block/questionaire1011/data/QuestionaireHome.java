package is.idega.idegaweb.campus.block.questionaire1011.data;


public interface QuestionaireHome extends com.idega.data.IDOHome
{
 public Questionaire create() throws javax.ejb.CreateException;
 public Questionaire findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(int p0)throws javax.ejb.FinderException;

}