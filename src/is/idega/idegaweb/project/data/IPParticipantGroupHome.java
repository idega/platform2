package is.idega.idegaweb.project.data;


public interface IPParticipantGroupHome extends com.idega.data.IDOHome
{
 public IPParticipantGroup create() throws javax.ejb.CreateException;
 public IPParticipantGroup createLegacy();
 public IPParticipantGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IPParticipantGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IPParticipantGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}