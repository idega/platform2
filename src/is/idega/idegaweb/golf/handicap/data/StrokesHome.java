package is.idega.idegaweb.golf.handicap.data;


public interface StrokesHome extends com.idega.data.IDOHome
{
 public Strokes create() throws javax.ejb.CreateException;
 public Strokes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Strokes create(is.idega.idegaweb.golf.handicap.data.StrokesKey p0)throws javax.ejb.CreateException;
 public java.util.Collection findAllByScorecard(java.lang.Object p0)throws javax.ejb.FinderException;
 public Strokes findByPrimaryKey(is.idega.idegaweb.golf.handicap.data.StrokesKey p0)throws javax.ejb.FinderException;
 public Strokes findStrokesByScorecardAndHoleNumber(java.lang.Object p0,int p1)throws javax.ejb.FinderException;
 public int getCount(java.lang.Object p0)throws com.idega.data.IDOException;

}