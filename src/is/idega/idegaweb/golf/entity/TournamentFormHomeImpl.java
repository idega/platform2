package is.idega.idegaweb.golf.entity;


public class TournamentFormHomeImpl extends com.idega.data.IDOFactory implements TournamentFormHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentForm.class;
 }

 public TournamentForm create() throws javax.ejb.CreateException{
  return (TournamentForm) super.idoCreate();
 }

 public TournamentForm createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentForm findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentForm) super.idoFindByPrimaryKey(id);
 }

 public TournamentForm findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentForm) super.idoFindByPrimaryKey(pk);
 }

 public TournamentForm findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}