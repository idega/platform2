package is.idega.idegaweb.golf.entity;


public class TournamentGroupHomeImpl extends com.idega.data.IDOFactory implements TournamentGroupHome
{
 protected Class getEntityInterfaceClass(){
  return TournamentGroup.class;
 }

 public TournamentGroup create() throws javax.ejb.CreateException{
  return (TournamentGroup) super.idoCreate();
 }

 public TournamentGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public TournamentGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TournamentGroup) super.idoFindByPrimaryKey(id);
 }

 public TournamentGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TournamentGroup) super.idoFindByPrimaryKey(pk);
 }

 public TournamentGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}