package is.idega.idegaweb.golf.entity;


public interface GolfMembersHome extends com.idega.data.IDOHome
{
 public GolfMembers create() throws javax.ejb.CreateException;
 public GolfMembers createLegacy();
 public GolfMembers findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public GolfMembers findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public GolfMembers findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}