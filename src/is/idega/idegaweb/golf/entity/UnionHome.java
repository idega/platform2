package is.idega.idegaweb.golf.entity;


public interface UnionHome extends com.idega.data.IDOHome
{
 public Union create() throws javax.ejb.CreateException;
 public Union createLegacy();
 public Union findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Union findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Union findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllUnions()throws javax.ejb.FinderException;
 public Union findByAbbreviation(java.lang.String p0)throws javax.ejb.FinderException;
 public Union findUnionByIWMemberSystemGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException;

}