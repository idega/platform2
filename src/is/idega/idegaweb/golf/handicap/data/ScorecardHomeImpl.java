package is.idega.idegaweb.golf.handicap.data;


public class ScorecardHomeImpl extends com.idega.data.IDOFactory implements ScorecardHome
{
 protected Class getEntityInterfaceClass(){
  return Scorecard.class;
 }


 public Scorecard create() throws javax.ejb.CreateException{
  return (Scorecard) super.createIDO();
 }


public java.util.Collection findAllByUser(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ScorecardBMPBean)entity).ejbFindAllByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Scorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Scorecard) super.findByPrimaryKeyIDO(pk);
 }



}