package is.idega.idegaweb.golf.tournament.data;


public class TournamentHomeImpl extends com.idega.data.IDOFactory implements TournamentHome
{
 protected Class getEntityInterfaceClass(){
  return Tournament.class;
 }


 public Tournament create() throws javax.ejb.CreateException{
  return (Tournament) super.createIDO();
 }


 public Tournament findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tournament) super.findByPrimaryKeyIDO(pk);
 }



}