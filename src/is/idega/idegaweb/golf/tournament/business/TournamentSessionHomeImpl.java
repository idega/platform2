package is.idega.idegaweb.golf.tournament.business;


public class TournamentSessionHomeImpl extends com.idega.business.IBOHomeImpl implements TournamentSessionHome
{
 protected Class getBeanInterfaceClass(){
  return TournamentSession.class;
 }


 public TournamentSession create() throws javax.ejb.CreateException{
  return (TournamentSession) super.createIBO();
 }



}