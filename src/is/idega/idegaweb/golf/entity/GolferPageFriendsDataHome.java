package is.idega.idegaweb.golf.entity;


public interface GolferPageFriendsDataHome extends com.idega.data.IDOHome
{
 public GolferPageFriendsData create() throws javax.ejb.CreateException;
 public GolferPageFriendsData createLegacy();
 public GolferPageFriendsData findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public GolferPageFriendsData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public GolferPageFriendsData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}