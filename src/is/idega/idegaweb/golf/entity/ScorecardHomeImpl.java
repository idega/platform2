package is.idega.idegaweb.golf.entity;


public class ScorecardHomeImpl extends com.idega.data.IDOFactory implements ScorecardHome
{
 protected Class getEntityInterfaceClass(){
  return Scorecard.class;
 }


 public Scorecard create() throws javax.ejb.CreateException{
  return (Scorecard) super.createIDO();
 }


 public Scorecard createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Scorecard) super.findByPrimaryKeyIDO(pk);
 }


 public Scorecard findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Scorecard) super.findByPrimaryKeyIDO(id);
 }


 public Scorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


public int getCountRoundsPlayedByMember(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ScorecardBMPBean)entity).ejbHomeGetCountRoundsPlayedByMember(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}