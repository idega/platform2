package is.idega.idegaweb.golf.entity;


public interface UnionMemberInfoHome extends com.idega.data.IDOHome
{
 public UnionMemberInfo create() throws javax.ejb.CreateException;
 public UnionMemberInfo createLegacy();
 public UnionMemberInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public UnionMemberInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public UnionMemberInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}