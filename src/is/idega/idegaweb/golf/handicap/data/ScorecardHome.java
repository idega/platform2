package is.idega.idegaweb.golf.handicap.data;


public interface ScorecardHome extends com.idega.data.IDOHome
{
 public Scorecard create() throws javax.ejb.CreateException;
 public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(int p0)throws javax.ejb.FinderException;

}