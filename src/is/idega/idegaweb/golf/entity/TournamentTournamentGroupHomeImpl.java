package is.idega.idegaweb.golf.entity;


public class TournamentTournamentGroupHomeImpl extends com.idega.data.IDOFactory implements TournamentTournamentGroupHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentTournamentGroup.class;
 }

 public TournamentTournamentGroup create() throws javax.ejb.CreateException{
  return (TournamentTournamentGroup) super.idoCreate();
 }

 public TournamentTournamentGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentTournamentGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentTournamentGroup) super.idoFindByPrimaryKey(id);
 }

 public TournamentTournamentGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentTournamentGroup) super.idoFindByPrimaryKey(pk);
 }

 public TournamentTournamentGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}