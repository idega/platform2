package is.idega.idegaweb.marathon.data;


public interface RunHome extends com.idega.data.IDOHome
{
 public Run create() throws javax.ejb.CreateException;
 public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public int getNextAvailableParticipantNumber(int p0,int p1)throws com.idega.data.IDOException;

}