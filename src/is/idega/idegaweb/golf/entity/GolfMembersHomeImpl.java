package is.idega.idegaweb.golf.entity;


public class GolfMembersHomeImpl extends com.idega.data.IDOFactory implements GolfMembersHome
{
 protected Class getEntityInterfaceClass(){
  return GolfMembers.class;
 }

 public GolfMembers create() throws javax.ejb.CreateException{
  return (GolfMembers) super.idoCreate();
 }

 public GolfMembers createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public GolfMembers findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (GolfMembers) super.idoFindByPrimaryKey(id);
 }

 public GolfMembers findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GolfMembers) super.idoFindByPrimaryKey(pk);
 }

 public GolfMembers findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}