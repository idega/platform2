package is.idega.idegaweb.golf.entity;


public interface MemberOverViewHome extends com.idega.data.IDOHome
{
 public MemberOverView create() throws javax.ejb.CreateException;
 public MemberOverView createLegacy();
 public MemberOverView findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MemberOverView findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MemberOverView findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}