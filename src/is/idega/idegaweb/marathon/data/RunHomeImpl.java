package is.idega.idegaweb.marathon.data;


public class RunHomeImpl extends com.idega.data.IDOFactory implements RunHome
{
 protected Class getEntityInterfaceClass(){
  return Run.class;
 }


 public Run create() throws javax.ejb.CreateException{
  return (Run) super.createIDO();
 }


 public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Run) super.findByPrimaryKeyIDO(pk);
 }


public int getNextAvailableParticipantNumber(int p0,int p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((RunBMPBean)entity).ejbHomeGetNextAvailableParticipantNumber(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}