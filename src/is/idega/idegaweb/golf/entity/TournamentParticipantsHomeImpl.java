package is.idega.idegaweb.golf.entity;


public class TournamentParticipantsHomeImpl extends com.idega.data.IDOFactory implements TournamentParticipantsHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentParticipants.class;
 }

 public TournamentParticipants create() throws javax.ejb.CreateException{
  return (TournamentParticipants) super.idoCreate();
 }

 public TournamentParticipants createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentParticipants findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentParticipants) super.idoFindByPrimaryKey(id);
 }

 public TournamentParticipants findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentParticipants) super.idoFindByPrimaryKey(pk);
 }

 public TournamentParticipants findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}