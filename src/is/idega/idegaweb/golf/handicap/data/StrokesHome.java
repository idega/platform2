package is.idega.idegaweb.golf.handicap.data;


public interface StrokesHome extends com.idega.data.IDOHome
{
 public Strokes create() throws javax.ejb.CreateException;
 public Strokes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByScorecardID(int p0)throws javax.ejb.FinderException;

}