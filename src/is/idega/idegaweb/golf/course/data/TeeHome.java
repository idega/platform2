package is.idega.idegaweb.golf.course.data;


public interface TeeHome extends com.idega.data.IDOHome
{
 public Tee create() throws javax.ejb.CreateException;
 public Tee findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Tee findAllByCourse(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByCourse(int p0)throws javax.ejb.FinderException;

}