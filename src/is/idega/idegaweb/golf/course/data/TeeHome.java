package is.idega.idegaweb.golf.course.data;


public interface TeeHome extends com.idega.data.IDOHome
{
 public Tee create() throws javax.ejb.CreateException;
 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Tee findAllByCourse(java.lang.Object p0,java.lang.Object p1,java.lang.Object p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByCourse(java.lang.Object p0)throws javax.ejb.FinderException;

}