package is.idega.idegaweb.golf.tournament.business;


public interface TournamentSession extends com.idega.business.IBOSession
{
 public java.sql.Date getEndDate() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameEndDate() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameStartDate() throws java.rmi.RemoteException;
 public java.lang.String getParameterNameTournamentID() throws java.rmi.RemoteException;
 public java.sql.Date getStartDate() throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.Tournament getTournament() throws java.rmi.RemoteException;
 public int getTournamentID() throws java.rmi.RemoteException;
 public void setEndDate(java.sql.Date p0) throws java.rmi.RemoteException;
 public void setStartDate(java.sql.Date p0) throws java.rmi.RemoteException;
 public void setTournament(is.idega.idegaweb.golf.entity.Tournament p0) throws java.rmi.RemoteException;
 public void setTournamentID(int p0) throws java.rmi.RemoteException;
}
