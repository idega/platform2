package is.idega.idegaweb.golf.entity;


public class TournamentRoundHomeImpl extends com.idega.data.IDOFactory implements TournamentRoundHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentRound.class;
 }

 public TournamentRound create() throws javax.ejb.CreateException{
  return (TournamentRound) super.idoCreate();
 }

 public TournamentRound createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentRound findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentRound) super.idoFindByPrimaryKey(id);
 }

 public TournamentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentRound) super.idoFindByPrimaryKey(pk);
 }

 public TournamentRound findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}