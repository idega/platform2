package is.idega.idegaweb.golf.entity;


public class MembersInTournamentHomeImpl extends com.idega.data.IDOFactory implements MembersInTournamentHome
{
 protected Class getEntityInterfaceClass(){
  return MembersInTournament.class;
 }

 public MembersInTournament create() throws javax.ejb.CreateException{
  return (MembersInTournament) super.idoCreate();
 }

 public MembersInTournament createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public MembersInTournament findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (MembersInTournament) super.idoFindByPrimaryKey(id);
 }

 public MembersInTournament findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MembersInTournament) super.idoFindByPrimaryKey(pk);
 }

 public MembersInTournament findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}