package is.idega.idegaweb.marathon.data;


public interface RunHome extends com.idega.data.IDOHome
{
 public Run create() throws javax.ejb.CreateException;
 public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}