package is.idega.idegaweb.golf.entity;


public interface MemberHome extends com.idega.data.IDOHome
{
 public Member create() throws javax.ejb.CreateException;
 public Member createLegacy();
 public Member findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Member findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Member findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public Member findMemberByIWMemberSystemUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;

}