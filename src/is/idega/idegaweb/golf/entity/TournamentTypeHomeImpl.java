package is.idega.idegaweb.golf.entity;


public class TournamentTypeHomeImpl extends com.idega.data.IDOFactory implements TournamentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentType.class;
 }

 public TournamentType create() throws javax.ejb.CreateException{
  return (TournamentType) super.idoCreate();
 }

 public TournamentType createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentType findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentType) super.idoFindByPrimaryKey(id);
 }

 public TournamentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentType) super.idoFindByPrimaryKey(pk);
 }

 public TournamentType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}