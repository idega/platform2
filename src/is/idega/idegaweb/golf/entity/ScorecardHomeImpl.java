package is.idega.idegaweb.golf.entity;


public class ScorecardHomeImpl extends com.idega.data.IDOFactory implements ScorecardHome
{
 protected Class getEntityInterfaceClass(){
  return Scorecard.class;
 }

 public Scorecard create() throws javax.ejb.CreateException{
  return (Scorecard) super.idoCreate();
 }

 public Scorecard createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Scorecard findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Scorecard) super.idoFindByPrimaryKey(id);
 }

 public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Scorecard) super.idoFindByPrimaryKey(pk);
 }

 public Scorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}