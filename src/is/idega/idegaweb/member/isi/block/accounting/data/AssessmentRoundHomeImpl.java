package is.idega.idegaweb.member.isi.block.accounting.data;


public class AssessmentRoundHomeImpl extends com.idega.data.IDOFactory implements AssessmentRoundHome
{
 protected Class getEntityInterfaceClass(){
  return AssessmentRound.class;
 }


 public AssessmentRound create() throws javax.ejb.CreateException{
  return (AssessmentRound) super.createIDO();
 }


public java.util.Collection findAllByClubAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AssessmentRoundBMPBean)entity).ejbFindAllByClubAndDivision(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AssessmentRound) super.findByPrimaryKeyIDO(pk);
 }



}