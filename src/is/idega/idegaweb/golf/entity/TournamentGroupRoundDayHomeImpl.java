package is.idega.idegaweb.golf.entity;


public class TournamentGroupRoundDayHomeImpl extends com.idega.data.IDOFactory implements TournamentGroupRoundDayHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentGroupRoundDay.class;
 }

 public TournamentGroupRoundDay create() throws javax.ejb.CreateException{
  return (TournamentGroupRoundDay) super.idoCreate();
 }

 public TournamentGroupRoundDay createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentGroupRoundDay findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentGroupRoundDay) super.idoFindByPrimaryKey(id);
 }

 public TournamentGroupRoundDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentGroupRoundDay) super.idoFindByPrimaryKey(pk);
 }

 public TournamentGroupRoundDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}