package is.idega.idegaweb.golf.entity;


public interface TeeHome extends com.idega.data.IDOHome
{
 public Tee create() throws javax.ejb.CreateException;
 public Tee createLegacy();
 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Tee findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Tee findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findByFieldAndHoleNumber(int p0,int p1)throws javax.ejb.FinderException;
 public Tee findByFieldAndTeeColorAndHoleNumber(int p0,int p1,int p2)throws javax.ejb.FinderException;

}