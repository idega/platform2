package is.idega.idegaweb.golf.entity;


public class TournamentScorecardHomeImpl extends com.idega.data.IDOFactory implements TournamentScorecardHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentScorecard.class;
 }

 public TournamentScorecard create() throws javax.ejb.CreateException{
  return (TournamentScorecard) super.idoCreate();
 }

 public TournamentScorecard createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentScorecard findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentScorecard) super.idoFindByPrimaryKey(id);
 }

 public TournamentScorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentScorecard) super.idoFindByPrimaryKey(pk);
 }

 public TournamentScorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}