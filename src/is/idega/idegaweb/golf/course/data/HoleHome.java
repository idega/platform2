package is.idega.idegaweb.golf.course.data;


public interface HoleHome extends com.idega.data.IDOHome
{
 public Hole create() throws javax.ejb.CreateException;
 public Hole findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByCourse(java.lang.Object p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByCourseAndTeeColor(java.lang.Object p0,java.lang.Object p1)throws javax.ejb.FinderException;
 public Hole findHoleByCourseAndTeeColorAndNumber(java.lang.Object p0,java.lang.Object p1,int p2)throws javax.ejb.FinderException;
 public int getCoursePar(java.lang.Object p0,java.lang.Object p1)throws com.idega.data.IDOException;

}