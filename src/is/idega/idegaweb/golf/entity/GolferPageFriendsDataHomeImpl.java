package is.idega.idegaweb.golf.entity;


public class GolferPageFriendsDataHomeImpl extends com.idega.data.IDOFactory implements GolferPageFriendsDataHome
{
 protected Class getEntityInterfaceClass(){
  return GolferPageFriendsData.class;
 }

 public GolferPageFriendsData create() throws javax.ejb.CreateException{
  return (GolferPageFriendsData) super.idoCreate();
 }

 public GolferPageFriendsData createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public GolferPageFriendsData findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (GolferPageFriendsData) super.idoFindByPrimaryKey(id);
 }

 public GolferPageFriendsData findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (GolferPageFriendsData) super.idoFindByPrimaryKey(pk);
 }

 public GolferPageFriendsData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}