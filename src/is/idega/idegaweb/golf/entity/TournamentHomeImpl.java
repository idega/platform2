package is.idega.idegaweb.golf.entity;


public class TournamentHomeImpl extends com.idega.data.IDOFactory implements TournamentHome
{
 protected Class getEntityInterfaceClass(){
  return Tournament.class;
 }

 public Tournament create() throws javax.ejb.CreateException{
  return (Tournament) super.idoCreate();
 }

 public Tournament createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Tournament findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Tournament) super.idoFindByPrimaryKey(id);
 }

 public Tournament findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tournament) super.idoFindByPrimaryKey(pk);
 }

 public Tournament findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}