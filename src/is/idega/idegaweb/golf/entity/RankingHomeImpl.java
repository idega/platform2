package is.idega.idegaweb.golf.entity;


public class RankingHomeImpl extends com.idega.data.IDOFactory implements RankingHome
{
 protected Class getEntityInterfaceClass(){
  return Ranking.class;
 }

 public Ranking create() throws javax.ejb.CreateException{
  return (Ranking) super.idoCreate();
 }

 public Ranking createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Ranking findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Ranking) super.idoFindByPrimaryKey(id);
 }

 public Ranking findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Ranking) super.idoFindByPrimaryKey(pk);
 }

 public Ranking findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}