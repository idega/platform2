package is.idega.idegaweb.campus.block.request.data;


public interface RequestHome extends com.idega.data.IDOHome
{
 public Request create() throws javax.ejb.CreateException;
 public Request findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByUser(Integer userID) throws javax.ejb.FinderException;
 public java.util.Collection findByStatus(String status) throws javax.ejb.FinderException;
 public java.util.Collection findByType(String type) throws javax.ejb.FinderException;
 public java.util.Collection findAll() throws javax.ejb.FinderException;

}