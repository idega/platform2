package is.idega.idegaweb.member.isi.block.accounting.data;


public class AssessmentRoundHomeImpl extends com.idega.data.IDOFactory implements AssessmentRoundHome
{
 protected Class getEntityInterfaceClass(){
  return AssessmentRound.class;
 }


 public AssessmentRound create() throws javax.ejb.CreateException{
  return (AssessmentRound) super.createIDO();
 }


 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AssessmentRound) super.findByPrimaryKeyIDO(pk);
 }



}