package is.idega.idegaweb.golf.course.data;


public interface TeeColorHome extends com.idega.data.IDOHome
{
 public TeeColor create() throws javax.ejb.CreateException;
 public TeeColor findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}