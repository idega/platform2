package is.idega.idegaweb.golf.tournament.business;


public class TournamentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements TournamentBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return TournamentBusiness.class;
 }


 public TournamentBusiness create() throws javax.ejb.CreateException{
  return (TournamentBusiness) super.createIBO();
 }



}