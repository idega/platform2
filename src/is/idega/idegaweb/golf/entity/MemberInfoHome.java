package is.idega.idegaweb.golf.entity;


public interface MemberInfoHome extends com.idega.data.IDOHome
{
 public MemberInfo create() throws javax.ejb.CreateException;
 public MemberInfo createLegacy();
 public MemberInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MemberInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MemberInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public MemberInfo findByMember(is.idega.idegaweb.golf.entity.Member p0)throws javax.ejb.FinderException;

}