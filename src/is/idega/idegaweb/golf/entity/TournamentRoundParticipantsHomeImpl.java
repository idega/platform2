package is.idega.idegaweb.golf.entity;


public class TournamentRoundParticipantsHomeImpl extends com.idega.data.IDOFactory implements TournamentRoundParticipantsHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentRoundParticipants.class;
 }

 public TournamentRoundParticipants create() throws javax.ejb.CreateException{
  return (TournamentRoundParticipants) super.idoCreate();
 }

 public TournamentRoundParticipants createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentRoundParticipants findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentRoundParticipants) super.idoFindByPrimaryKey(id);
 }

 public TournamentRoundParticipants findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentRoundParticipants) super.idoFindByPrimaryKey(pk);
 }

 public TournamentRoundParticipants findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}