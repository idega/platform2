package is.idega.idegaweb.golf.entity;


public class TournamentDayHomeImpl extends com.idega.data.IDOFactory implements TournamentDayHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentDay.class;
 }

 public TournamentDay create() throws javax.ejb.CreateException{
  return (TournamentDay) super.idoCreate();
 }

 public TournamentDay createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentDay findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentDay) super.idoFindByPrimaryKey(id);
 }

 public TournamentDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentDay) super.idoFindByPrimaryKey(pk);
 }

 public TournamentDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}