package is.idega.idegaweb.golf.handicap.data;


public interface StrokesHome extends com.idega.data.IDOHome
{
 public Strokes create(java.lang.Object p0,java.lang.Object p1)throws javax.ejb.CreateException;
 public java.util.Collection findAllByScorecard(java.lang.Object p0)throws javax.ejb.FinderException;
 public Strokes findByPrimaryKey(java.lang.Object p0,java.lang.Object p1)throws javax.ejb.FinderException;
 public Strokes findStrokesByScorecardAndHole(java.lang.Object p0,java.lang.Object p1)throws javax.ejb.FinderException;
 public Strokes findStrokesByScorecardAndHoleNumber(java.lang.Object p0,int p1)throws javax.ejb.FinderException;

}